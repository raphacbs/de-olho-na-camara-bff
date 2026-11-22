package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.model.SyncScheduleEntity;
import br.com.deolhonacamara.api.repository.SyncScheduleRepository;
import br.com.deolhonacamara.api.service.SyncFlows;
import br.com.deolhonacamara.api.service.SyncService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
@Log4j2
public class SyncScheduleRegistrar {

    private final SyncScheduleRepository syncScheduleRepository;
    private final SyncService syncService;
    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    private final ZoneId zone = ZoneId.of(SyncFlows.ZONE);

    @jakarta.annotation.PostConstruct
    public void start() {
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("cabine-cron-");
        scheduler.initialize();
        try {
            seedDefaults();
            reload();
        } catch (Exception e) {
            log.error("Tabela sync_schedule indisponível. Agendamentos ficam só com o horário padrão até a migration V20.", e);
            SyncFlows.DEFAULT_CRONS.forEach((flow, cron) -> arm(flow, cron, true));
        }
    }

    @PreDestroy
    public void stop() {
        tasks.values().forEach(task -> task.cancel(false));
        scheduler.shutdown();
    }

    public void reload() {
        for (SyncScheduleEntity schedule : syncScheduleRepository.findAll()) {
            arm(schedule.getFlowName(), schedule.getCronExpression(), schedule.isEnabled());
        }
    }

    public Optional<SyncScheduleEntity> find(String flow) {
        return syncScheduleRepository.findById(flow);
    }

    public void update(String flow, String cron, boolean enabled) {
        SyncScheduleEntity schedule = syncScheduleRepository.findById(flow).orElseGet(() -> SyncScheduleEntity.builder()
                .flowName(flow)
                .build());
        schedule.setCronExpression(cron);
        schedule.setEnabled(enabled);
        schedule.setUpdatedAt(LocalDateTime.now());
        syncScheduleRepository.save(schedule);
        arm(flow, cron, enabled);
    }

    private void seedDefaults() {
        SyncFlows.DEFAULT_CRONS.forEach((flow, cron) -> {
            if (syncScheduleRepository.findById(flow).isEmpty()) {
                syncScheduleRepository.save(SyncScheduleEntity.builder()
                        .flowName(flow)
                        .cronExpression(cron)
                        .enabled(true)
                        .updatedAt(LocalDateTime.now())
                        .build());
            }
        });
    }

    private void arm(String flow, String cron, boolean enabled) {
        ScheduledFuture<?> previous = tasks.remove(flow);
        if (previous != null) {
            previous.cancel(false);
        }
        if (!enabled || !SyncFlows.DEFAULT_CRONS.containsKey(flow)) {
            log.info("Schedule disabled for {}", flow);
            return;
        }
        tasks.put(flow, scheduler.schedule(() -> dispatch(flow), new CronTrigger(cron, zone)));
        log.info("Scheduled {} at {} ({})", flow, cron, zone);
    }

    private void dispatch(String flow) {
        switch (flow) {
            case SyncFlows.POLITICIANS -> syncService.syncPoliticians();
            case SyncFlows.EXPENSES -> syncService.syncExpenses();
            case SyncFlows.VOTINGS -> syncService.syncVotes();
            case SyncFlows.SPEECHES -> syncService.syncSpeeches();
            case SyncFlows.PROPOSITIONS -> syncService.syncPropositions();
            case SyncFlows.TRAMITATIONS -> syncService.syncTramitations();
            case SyncFlows.RECENT_PROPOSITIONS -> syncService.syncRecentPropositions();
            default -> log.warn("No dispatcher for flow {}", flow);
        }
    }
}
