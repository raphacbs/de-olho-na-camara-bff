package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.PropositionTramitationEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.PropositionTramitationService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class PropositionTramitationSyncJob {

    private final PropositionRepository propositionRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final PropositionTramitationService propositionTramitationService;
    private final SyncProgressService syncProgressService;
    private final PropertiesConfig propertiesConfig;
    // Defined in AsyncConfig#syncExecutor
    @Qualifier("syncExecutor")
    private final Executor syncExecutor;

    // Runs daily at 05:00 (Brasília time) to avoid overlapping with propositions job
    @Scheduled(cron = "0 0 5 * * *", zone = "America/Recife")
    public void syncPropositionTramitations() {
        log.info("Starting proposition tramitation synchronization from Câmara API...");

        // Get or create the current execution
        SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("PropositionTramitations");

        // Get persisted progress to decide where to resume
        Optional<SyncProgressEntity> persisted = syncProgressService.getCurrentProgress("PropositionTramitations");
        Integer resumePropositionId = persisted.map(SyncProgressEntity::getLastPropositionId).map(Math::toIntExact).orElse(null);
        Long resumeTramitacaoId = persisted.map(SyncProgressEntity::getLastTramitacaoId).orElse(null);

        try {
            // Decide initial lastProcessedId for proposition query
            int lastProcessedId;
            if (resumePropositionId != null) {
                // If we have a last tramitacao id, we want to re-include that proposition in the query so we can resume inside it
                if (resumeTramitacaoId != null) {
                    lastProcessedId = Math.max(0, resumePropositionId.intValue() - 1);
                } else {
                    // No mid-proposition resume, start after the last processed proposition
                    lastProcessedId = resumePropositionId.intValue();
                }
            } else {
                lastProcessedId = currentExecution.getCurrentPage() == null ? 0 : currentExecution.getCurrentPage();
            }

            int chunkSize = propertiesConfig.getPropositionSyncChunkSize() == null ? 500 : propertiesConfig.getPropositionSyncChunkSize();
            int configuredInFlight = propertiesConfig.getPropositionSyncMaxInFlight() == null ? 50 : propertiesConfig.getPropositionSyncMaxInFlight();
            int inFlightLimit = Math.min(configuredInFlight, chunkSize);

            while (true) {
                List<PropositionEntity> props = propositionRepository.findPropositionsAfterId(lastProcessedId, chunkSize);
                if (props == null || props.isEmpty()) {
                    log.info("No more propositions to process for tramitações (lastProcessedId={})", lastProcessedId);
                    break;
                }

                log.info("Processing {} propositions for tramitações (startId={})", props.size(), lastProcessedId + 1);

                Integer resumedPropositionId = null;
                int maxIdInChunk = lastProcessedId;

                // If we are resuming inside a specific proposition, process it synchronously first
                // to ensure the resume pointer is cleared deterministically before dispatching async work
                if (resumePropositionId != null && resumeTramitacaoId != null) {
                    final Integer resumePropIdSnapshot = resumePropositionId;
                    PropositionEntity toResume = props.stream()
                            .filter(p -> Objects.equals(p.getId(), resumePropIdSnapshot))
                            .findFirst()
                            .orElse(null);
                    if (toResume != null) {
                        processPropositionTramitations(currentExecution, toResume, resumeTramitacaoId, true);
                        if (toResume.getId() != null) {
                            maxIdInChunk = Math.max(maxIdInChunk, toResume.getId());
                            resumedPropositionId = toResume.getId();
                        }
                        resumePropositionId = null;
                        resumeTramitacaoId = null;
                    }
                }

                maxIdInChunk = Math.max(maxIdInChunk,
                        props.stream()
                                .map(PropositionEntity::getId)
                                .filter(Objects::nonNull)
                                .max(Integer::compareTo)
                                .orElse(maxIdInChunk));

                final Integer resumedIdSnapshot = resumedPropositionId;
                List<PropositionEntity> propsToProcess = resumedIdSnapshot == null
                        ? props
                        : props.stream().filter(p -> !Objects.equals(p.getId(), resumedIdSnapshot)).toList();

                List<CompletableFuture<Void>> tasks = new ArrayList<>();

                for (PropositionEntity proposition : propsToProcess) {
                    tasks.add(CompletableFuture.runAsync(
                                    () -> processPropositionTramitations(currentExecution, proposition, null, false),
                                    syncExecutor
                            )
                            .exceptionally(ex -> {
                                log.error("Unexpected error syncing tramitacoes for proposition {}: {}", proposition.getId(), ex.getMessage(), ex);
                                return null;
                            }));

                    if (tasks.size() >= inFlightLimit) {
                        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
                        tasks.clear();
                    }
                }

                if (!tasks.isEmpty()) {
                    CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
                }

                // persist progress using maxIdInChunk so we can resume after failures
                try {
                    syncProgressService.updateCurrentPage("PropositionTramitations", currentExecution.getExecutionId(), maxIdInChunk);
                    // Also save more explicit last proposition progress (id and timestamp)
                    syncProgressService.updateLastPropositionProgress("PropositionTramitations", currentExecution.getExecutionId(), (long) maxIdInChunk, null, LocalDateTime.now());
                    lastProcessedId = maxIdInChunk;
                } catch (Exception e) {
                    log.warn("Failed to update sync progress after processing chunk ending at {}: {}", maxIdInChunk, e.getMessage());
                }

                // continue with next chunk starting after lastProcessedId
            }

            syncProgressService.markExecutionCompleted("PropositionTramitations", currentExecution.getExecutionId());
            log.info("Proposition tramitation synchronization completed");

        } catch (Exception e) {
            log.error("Error syncing proposition tramitations: ", e);
            try {
                syncProgressService.markExecutionFailed("PropositionTramitations", currentExecution.getExecutionId());
            } catch (Exception ex) {
                log.error("Error marking PropositionTramitations execution as failed: {}", ex.getMessage());
            }
        }
    }

    private void processPropositionTramitations(SyncProgressEntity currentExecution,
                                                PropositionEntity proposition,
                                                Long resumeTramitacaoId,
                                                boolean resumeInsideThisProposition) {
        try {
            LocalDate today = LocalDate.now();
            int pageTram = 1;
            boolean stillResuming = resumeInsideThisProposition;
            final Long resumeSeqSnapshot = resumeTramitacaoId;

            while (true) {
                log.info("Searching tramitações for proposition {} (page {})", proposition.getId(), pageTram);
                var tramsResp = camaraDeputadosService.getTramitacoesByPropositionIdWithPage(proposition.getId(), today, today, pageTram);
                if (tramsResp == null || tramsResp.getData() == null || tramsResp.getData().isEmpty()) {
                    log.info("Tramitations for proposition {} exhausted at page {}", proposition.getId(), pageTram);
                    break;
                }

                // Convert to entities
                List<PropositionTramitationEntity> trams = tramsResp.getData().stream().map(s -> PropositionTramitationEntity.builder()
                                .propositionId(proposition.getId())
                                .dateTime(s.getDateTime())
                                .sequence(s.getSequence())
                                .orgAcronym(s.getOrganAcronym())
                                .orgUri(s.getOrganUri())
                                .lastReporterUri(s.getLastReporterUri())
                                .regime(s.getRegime())
                                .tramitationDescription(s.getTramitationDescription())
                                .tramitationTypeCode(s.getTramitationTypeCode())
                                .situationDescription(s.getSituationDescription())
                                .situationCode(s.getSituationCode())
                                .dispatch(s.getDispatch())
                                .url(s.getUrl())
                                .scope(s.getScope())
                                .appreciation(s.getAppreciation())
                                .build())
                        .collect(Collectors.toList());

                // If resuming inside this proposition, filter out sequences already processed
                if (stillResuming && resumeSeqSnapshot != null) {
                    long resumeSeq = resumeSeqSnapshot;
                    List<PropositionTramitationEntity> filtered = trams.stream()
                            .filter(t -> t.getSequence() == null || t.getSequence() > resumeSeq)
                            .collect(Collectors.toList());
                    trams = filtered;
                    if (filtered.isEmpty()) {
                        log.info("All tramitações on page {} for proposition {} were already processed (resume sequence={})", pageTram, proposition.getId(), resumeSeq);
                    }
                }

                // Persist found tramitações
                if (trams != null && !trams.isEmpty()) {
                    log.info("Found {} tramitações for proposition {} on page {} after filtering", trams.size(), proposition.getId(), pageTram);
                    propositionTramitationService.upsertTramitationEntities(trams);

                    Optional<Integer> maxSeqOpt = trams.stream().map(PropositionTramitationEntity::getSequence).filter(Objects::nonNull).max(Integer::compareTo);
                    Long lastSeq = maxSeqOpt.map(Integer::longValue).orElse(null);

                    try {
                        syncProgressService.updateLastPropositionProgress("PropositionTramitations", currentExecution.getExecutionId(), proposition.getId().longValue(), lastSeq, LocalDateTime.now());
                    } catch (Exception e) {
                        log.warn("Failed to persist last proposition/tramitacao progress for proposition {} page {}: {}", proposition.getId(), pageTram, e.getMessage());
                    }

                    stillResuming = false;
                } else {
                    try {
                        syncProgressService.updateLastPropositionProgress("PropositionTramitations", currentExecution.getExecutionId(), proposition.getId().longValue(), resumeTramitacaoId, LocalDateTime.now());
                    } catch (Exception e) {
                        log.debug("Failed to persist check progress for proposition {} page {}: {}", proposition.getId(), pageTram, e.getMessage());
                    }
                }

                pageTram++;
            }

        } catch (Exception e) {
            log.error("Error syncing tramitacoes for proposition {}: {}", proposition.getId(), e.getMessage(), e);
        }
    }
}
