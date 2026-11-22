package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.model.SyncScheduleEntity;
import br.com.deolhonacamara.api.repository.SyncProgressRepository;
import br.com.deolhonacamara.scheduler.SyncScheduleRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CabineService {

    public static final String OPERATOR_EMAIL = "cabine@deolhonacamara.local";

    private static final List<FlowDef> FLOWS = List.of(
            new FlowDef("all", SyncFlows.ALL, "Carga completa", "manual"),
            new FlowDef("politicians", SyncFlows.POLITICIANS, "Deputados", "23:00"),
            new FlowDef("expenses", SyncFlows.EXPENSES, "Despesas", "01:00"),
            new FlowDef("votes", SyncFlows.VOTINGS, "Votações", "02:00"),
            new FlowDef("speeches", SyncFlows.SPEECHES, "Discursos", "03:00"),
            new FlowDef("propositions", SyncFlows.PROPOSITIONS, "Proposições", "04:00"),
            new FlowDef("tramitations", SyncFlows.TRAMITATIONS, "Tramitações", "05:00"),
            new FlowDef("recent", SyncFlows.RECENT_PROPOSITIONS, "Proposições recentes", "06:00")
    );

    private final SyncExecutionGate syncExecutionGate;
    private final SyncRunControl syncRunControl;
    private final SyncProgressRepository syncProgressRepository;
    private final SyncService syncService;
    private final SyncScheduleRegistrar syncScheduleRegistrar;

    public Board board() {
        List<FlowCard> cards = FLOWS.stream()
                .map(flow -> {
                    ScheduleView schedule = scheduleOf(flow);
                    boolean paused = latest(flow.flow()).map(SyncProgressEntity::isPaused).orElse(false);
                    return new FlowCard(
                            flow.code(),
                            flow.label(),
                            schedule.time(),
                            schedule.enabled(),
                            syncExecutionGate.isRunning(flow.flow()),
                            paused,
                            syncExecutionGate.blockReason(flow.flow()).map(this::describe).orElse(null)
                    );
                })
                .toList();
        List<ExecutionRow> executions = syncProgressRepository.findRecent(40).stream()
                .map(this::toRow)
                .toList();
        return new Board(cards, executions);
    }

    public void start(String code) {
        FlowDef flow = find(code);
        syncExecutionGate.blockReason(flow.flow()).map(this::describe).ifPresent(reason -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, reason);
        });
        syncRunControl.clear(flow.flow());
        switch (flow.code()) {
            case "all" -> syncService.syncAll();
            case "politicians" -> syncService.syncPoliticians();
            case "expenses" -> syncService.syncExpenses();
            case "votes" -> syncService.syncVotes();
            case "speeches" -> syncService.syncSpeeches();
            case "propositions" -> syncService.syncPropositions();
            case "tramitations" -> syncService.syncTramitations();
            case "recent" -> syncService.syncRecentPropositions();
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fluxo desconhecido");
        }
    }

    public void updateSchedule(String code, String time, boolean enabled) {
        FlowDef flow = find(code);
        if (SyncFlows.ALL.equals(flow.flow())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A carga completa não tem horário");
        }
        final String cron;
        try {
            cron = SyncCron.fromTime(time);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        syncScheduleRegistrar.update(flow.flow(), cron, enabled);
    }

    public void pause(String code) {
        FlowDef flow = find(code);
        if (syncExecutionGate.isRunning(flow.flow())) {
            syncRunControl.request(flow.flow(), SyncRunControl.PAUSE);
            return;
        }
        SyncProgressEntity last = latest(flow.flow()).orElse(null);
        if (last != null && last.isRunning()) {
            syncProgressServiceHalt(last, "paused");
            return;
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há execução em andamento");
    }

    public void cancel(String code) {
        FlowDef flow = find(code);
        if (syncExecutionGate.isRunning(flow.flow())) {
            syncRunControl.request(flow.flow(), SyncRunControl.CANCEL);
            return;
        }
        SyncProgressEntity last = latest(flow.flow()).orElse(null);
        if (last != null && (last.isRunning() || last.isPaused())) {
            syncProgressServiceHalt(last, "cancelled");
            return;
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há execução para cancelar");
    }

    private void syncProgressServiceHalt(SyncProgressEntity last, String status) {
        syncProgressRepository.save(halted(last, status));
    }

    private SyncProgressEntity halted(SyncProgressEntity last, String status) {
        last.setStatus(status);
        last.setEndTime("paused".equals(status) ? null : LocalDateTime.now());
        last.setLastUpdated(LocalDateTime.now());
        last.setUpdatedAt(LocalDateTime.now());
        return last;
    }

    private java.util.Optional<SyncProgressEntity> latest(String flow) {
        if (SyncFlows.ALL.equals(flow)) {
            return java.util.Optional.empty();
        }
        return syncProgressRepository.findFirstByFlowNameOrderByStartTimeDesc(flow);
    }

    private ScheduleView scheduleOf(FlowDef flow) {
        if (SyncFlows.ALL.equals(flow.flow())) {
            return new ScheduleView("manual", false);
        }
        String fallback = SyncCron.toTime(SyncFlows.DEFAULT_CRONS.get(flow.flow()));
        try {
            return syncScheduleRegistrar.find(flow.flow())
                    .map(this::toView)
                    .orElse(new ScheduleView(fallback, true));
        } catch (RuntimeException e) {
            return new ScheduleView(fallback, true);
        }
    }

    private ScheduleView toView(SyncScheduleEntity schedule) {
        return new ScheduleView(SyncCron.toTime(schedule.getCronExpression()), schedule.isEnabled());
    }

    private FlowDef find(String code) {
        return FLOWS.stream()
                .filter(item -> item.code().equals(code))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fluxo desconhecido"));
    }

    private ExecutionRow toRow(SyncProgressEntity entity) {
        return new ExecutionRow(
                entity.getId(),
                labelOf(entity.getFlowName()),
                entity.getStatus(),
                entity.getCurrentPage(),
                entity.getTotalPages(),
                entity.getProcessedCount(),
                entity.getErrorCount(),
                entity.getLastCursor(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getLastUpdated()
        );
    }

    private String describe(String reason) {
        String already = " is already running";
        String waiting = "waiting for ";
        if (reason.endsWith(already)) {
            return labelOf(reason.substring(0, reason.length() - already.length())) + " já está em execução";
        }
        if (reason.startsWith(waiting)) {
            return "Aguardando " + labelOf(reason.substring(waiting.length()));
        }
        return reason;
    }

    private String labelOf(String flow) {
        return FLOWS.stream()
                .filter(item -> item.flow().equals(flow))
                .map(FlowDef::label)
                .findFirst()
                .orElse(flow);
    }

    public record Board(List<FlowCard> flows, List<ExecutionRow> executions) {
    }

    public record FlowCard(String code, String label, String schedule, boolean enabled, boolean running, boolean paused, String blockedBy) {
    }

    private record ScheduleView(String time, boolean enabled) {
    }

    public record ExecutionRow(
            Long id,
            String flow,
            String status,
            Integer currentPage,
            Integer totalPages,
            Integer processedCount,
            Integer errorCount,
            String lastCursor,
            LocalDateTime startTime,
            LocalDateTime endTime,
            LocalDateTime lastUpdated
    ) {
    }

    private record FlowDef(String code, String flow, String label, String schedule) {
    }
}
