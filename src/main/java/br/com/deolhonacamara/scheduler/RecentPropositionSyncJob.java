package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.dto.PropositionListResponseBodyDto;
import br.com.deolhonacamara.api.dto.PropositionResponseBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Synchronizes propositions and their tramitations from the last N days
 * (configurable via {@code proposition.recent.sync.days}, default 3).
 *
 * <p>This job is meant to run daily and process a small, recent data set, making
 * it considerably faster than the full {@link PropositionSyncJob} and
 * {@link PropositionTramitationSyncJob} executions.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class RecentPropositionSyncJob {

    private static final String FLOW_NAME = "RecentPropositions";

    private final PropositionRepository propositionRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final PropositionTramitationService propositionTramitationService;
    private final SyncProgressService syncProgressService;
    private final PropertiesConfig propertiesConfig;

    @Qualifier("syncExecutor")
    private final Executor syncExecutor;

    // Runs daily at 06:00 (Recife/BRT time, UTC-3), after the full sync jobs (04:00 and 05:00)
    @Scheduled(cron = "0 0 6 * * *", zone = "America/Recife")
    public void syncRecentPropositions() {
        log.info("Starting recent proposition synchronization from Câmara API...");

        SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution(FLOW_NAME);

        try {
            int days = propertiesConfig.getPropositionRecentSyncDays();

            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(days);

            log.info("Syncing propositions presented/updated between {} and {}", startDate, endDate);

            int page = 1;
            while (true) {
                PropositionListResponseBodyDto response = fetchRecentPropositions(startDate, endDate, page);

                if (response == null || response.getData() == null || response.getData().isEmpty()) {
                    log.info("No more propositions to process at page {}", page);
                    break;
                }

                log.info("Processing {} propositions from page {}", response.getData().size(), page);

                List<CompletableFuture<Void>> tasks = response.getData().stream()
                        .map(dto -> CompletableFuture
                                .runAsync(() -> savePropositionAndTramitations(dto.getId(), startDate, endDate),
                                        syncExecutor)
                                .exceptionally(ex -> {
                                    log.error("Unexpected error processing proposition {}: {}",
                                            dto.getId(), ex.getMessage(), ex);
                                    return null;
                                }))
                        .toList();

                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

                syncProgressService.updateCurrentPage(FLOW_NAME, currentExecution.getExecutionId(), page);

                Integer lastPage = getLastPageNumber(response);
                if (page >= lastPage) {
                    break;
                }
                page++;
            }

            syncProgressService.markExecutionCompleted(FLOW_NAME, currentExecution.getExecutionId());
            log.info("Recent proposition synchronization completed successfully");

        } catch (Exception e) {
            log.error("Error during recent proposition synchronization: ", e);
            try {
                syncProgressService.markExecutionFailed(FLOW_NAME, currentExecution.getExecutionId());
            } catch (Exception ex) {
                log.error("Error marking {} execution as failed: {}", FLOW_NAME, ex.getMessage());
            }
        }
    }

    private PropositionListResponseBodyDto fetchRecentPropositions(LocalDate startDate, LocalDate endDate, int page) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("dataApresentacaoInicio", startDate.toString());
            params.put("dataApresentacaoFim", endDate.toString());
            params.put("ordem", "DESC");
            params.put("ordenarPor", "id");
            params.put("pagina", page);
            return camaraDeputadosService.getPropositions(params);
        } catch (Exception e) {
            log.warn("Error fetching recent propositions for page {}: {}", page, e.getMessage());
            return null;
        }
    }

    private void savePropositionAndTramitations(Integer propositionId, LocalDate startDate, LocalDate endDate) {
        try {
            PropositionResponseBodyDto fullResponse = camaraDeputadosService.getPropositionsById(propositionId);

            if (fullResponse == null || fullResponse.getData() == null) {
                log.warn("Empty response when fetching full details for proposition {}", propositionId);
                return;
            }

            PropositionBodyDto fullDto = fullResponse.getData();
            PropositionEntity entity = Mapper.INSTANCE.toEntity(fullDto);

            log.info("Saving recent proposition ID {}", entity.getId());
            propositionRepository.upsertProposition(entity);

            syncTramitations(propositionId, startDate, endDate);

        } catch (Exception e) {
            log.error("Error processing proposition {}: {}", propositionId, e.getMessage(), e);
        }
    }

    private void syncTramitations(Integer propositionId, LocalDate startDate, LocalDate endDate) {
        try {
            int page = 1;
            while (true) {
                var tramResponse = camaraDeputadosService
                        .getTramitacoesByPropositionIdWithPage(propositionId, startDate, endDate, page);

                if (tramResponse == null || tramResponse.getData() == null || tramResponse.getData().isEmpty()) {
                    log.info("No tramitations found for proposition {} on page {}", propositionId, page);
                    break;
                }

                List<PropositionTramitationEntity> trams = tramResponse.getData().stream()
                        .map(s -> PropositionTramitationEntity.builder()
                                .propositionId(propositionId)
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

                propositionTramitationService.upsertTramitationEntities(trams);
                log.info("Saved {} tramitations for proposition {} on page {}", trams.size(), propositionId, page);

                page++;
            }
        } catch (Exception e) {
            log.error("Error syncing tramitations for proposition {}: {}", propositionId, e.getMessage(), e);
        }
    }

    private Integer getLastPageNumber(PropositionListResponseBodyDto response) {
        if (response.getLinks() == null) {
            return 1;
        }
        return response.getLinks().stream()
                .map(link -> link.getNumberLastPage())
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(1);
    }
}
