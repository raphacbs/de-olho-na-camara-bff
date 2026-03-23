package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.SpeechEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.SpeechRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class SpeechSyncJob {

    private final PoliticianRepository politicianRepository;
    private final SpeechRepository speechRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final SyncProgressService syncProgressService;
    private final Mapper mapper = Mapper.INSTANCE;


    // Runs daily at 03:00 (Brasília time)
    @Scheduled(cron = "0 0 3 * * *", zone = "America/Sao_Paulo")
    public void syncSpeeches() {
        log.info("Starting speech synchronization from Câmara API...");

        try {
            // Get or create current execution for Speeches flow
            SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("Speeches");

            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync speeches", politicians.size());

            int totalSpeeches = 0;
            int processedPoliticians = 0;
            int totalPoliticians = politicians.size();

            // Set total pages (total politicians) on first response for this execution
            if (currentExecution.getTotalPages() == null) {
                syncProgressService.updateTotalPages("Speeches", currentExecution.getExecutionId(), totalPoliticians);
                log.info("Set total pages to {} for speeches execution {}", totalPoliticians, currentExecution.getExecutionId());
            }

            for (PoliticianEntity politician : politicians) {
                try {
                    // Update progress for current politician (increment before processing to match Proposition job behavior)
                    processedPoliticians++;
                    log.info("Processing politician {}/{}: {} (ID: {})", processedPoliticians, totalPoliticians, politician.getName(), politician.getId());

                    var speechesResponse = camaraDeputadosService.getSpeeches(politician.getId());
                    if (speechesResponse != null && speechesResponse.getData() != null) {
                        for (SpeechBodyDto speechDto : speechesResponse.getData()) {
                            SpeechEntity speech = mapper.toDto(politician.getId(), speechDto);
                            speechRepository.upsertSpeech(speech);
                            totalSpeeches++;
                        }
                    }

                    // Persist current page (politician processed)
                    syncProgressService.updateCurrentPage("Speeches", currentExecution.getExecutionId(), processedPoliticians);

                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} speeches synced so far", processedPoliticians, totalSpeeches);
                    }
                } catch (Exception e) {
                    log.error("Error syncing speeches for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            // Mark execution as completed
            syncProgressService.markExecutionCompleted("Speeches", currentExecution.getExecutionId());
            log.info("Speech synchronization completed: {} speeches synced for {} politicians.", totalSpeeches, processedPoliticians);

        } catch (Exception e) {
            log.error("Error syncing speeches: ", e);
            // Mark execution as failed if something goes wrong
            try {
                syncProgressService.getCurrentProgress("Speeches")
                    .filter(SyncProgressEntity::isRunning)
                    .ifPresent(exec -> syncProgressService.markExecutionFailed("Speeches", exec.getExecutionId()));
            } catch (Exception e2) {
                log.error("Error marking execution as failed: ", e2);
            }
        }
    }
}
