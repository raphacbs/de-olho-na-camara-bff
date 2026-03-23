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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    // Runs daily at 05:00 (Brasília time) to avoid overlapping with propositions job
    @Scheduled(cron = "0 0 5 * * *", zone = "America/Sao_Paulo")
    public void syncPropositionTramitations() {
        log.info("Starting proposition tramitation synchronization from Câmara API...");

        // Get or create the current execution
        SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("PropositionTramitations");

        // Get persisted progress to decide where to resume
        Optional<SyncProgressEntity> persisted = syncProgressService.getCurrentProgress("PropositionTramitations");
        Long resumePropositionId = persisted.map(SyncProgressEntity::getLastPropositionId).orElse(null);
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

            while (true) {
                List<PropositionEntity> props = propositionRepository.findPropositionsAfterId(lastProcessedId, chunkSize);
                if (props == null || props.isEmpty()) {
                    log.info("No more propositions to process for tramitações (lastProcessedId={})", lastProcessedId);
                    break;
                }

                log.info("Processing {} propositions for tramitações (startId={})", props.size(), lastProcessedId + 1);

                int maxIdInChunk = lastProcessedId;

                for (PropositionEntity proposition : props) {
                    try {
                        // update max id seen in this chunk
                        if (proposition.getId() != null && proposition.getId() > maxIdInChunk) {
                            maxIdInChunk = proposition.getId();
                        }

                        LocalDate today = LocalDate.now();

                        // Determine if we need to resume inside this proposition
                        boolean resumeInsideThisProposition = resumePropositionId != null && Objects.equals(proposition.getId(), resumePropositionId.intValue()) && resumeTramitacaoId != null;

                        int pageTram = 1;
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
                            if (resumeInsideThisProposition && resumeTramitacaoId != null) {
                                int resumeSeq = resumeTramitacaoId.intValue();
                                List<PropositionTramitationEntity> filtered = trams.stream()
                                        .filter(t -> t.getSequence() == null || t.getSequence() > resumeSeq)
                                        .collect(Collectors.toList());
                                trams = filtered;
                                // If we filtered some, log it
                                if (filtered.isEmpty()) {
                                    log.info("All tramitações on page {} for proposition {} were already processed (resume sequence={})", pageTram, proposition.getId(), resumeSeq);
                                }
                            }

                            // Persist found tramitações
                            if (trams != null && !trams.isEmpty()) {
                                log.info("Found {} tramitações for proposition {} on page {} after filtering", trams.size(), proposition.getId(), pageTram);
                                propositionTramitationService.upsertTramitationEntities(trams);

                                // compute max sequence processed in this page to persist as lastTramitacaoId
                                Optional<Integer> maxSeqOpt = trams.stream().map(PropositionTramitationEntity::getSequence).filter(Objects::nonNull).max(Integer::compareTo);
                                Long lastSeq = maxSeqOpt.map(Integer::longValue).orElse(null);

                                try {
                                    syncProgressService.updateLastPropositionProgress("PropositionTramitations", currentExecution.getExecutionId(), proposition.getId().longValue(), lastSeq, LocalDateTime.now());
                                } catch (Exception e) {
                                    log.warn("Failed to persist last proposition/tramitacao progress for proposition {} page {}: {}", proposition.getId(), pageTram, e.getMessage());
                                }

                                // If we were resuming inside this proposition and have processed sequences beyond resume, unset resume flags
                                if (resumeInsideThisProposition) {
                                    resumeTramitacaoId = null;
                                    resumePropositionId = null;
                                    resumeInsideThisProposition = false;
                                }
                            } else {
                                // Even if nothing new was processed on this page, persist the fact we checked this proposition page
                                try {
                                    syncProgressService.updateLastPropositionProgress("PropositionTramitations", currentExecution.getExecutionId(), proposition.getId().longValue(), resumeTramitacaoId, LocalDateTime.now());
                                } catch (Exception e) {
                                    log.debug("Failed to persist check progress for proposition {} page {}: {}", proposition.getId(), pageTram, e.getMessage());
                                }
                                // continue to next page
                            }

                            pageTram++;
                        }

                    } catch (Exception e) {
                        log.error("Error syncing tramitacoes for proposition {}: {}", proposition.getId(), e.getMessage());
                    }
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
}
