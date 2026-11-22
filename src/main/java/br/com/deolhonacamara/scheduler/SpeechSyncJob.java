package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.SpeechEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.SpeechRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncExecutionGate;
import br.com.deolhonacamara.api.service.SyncFlows;
import br.com.deolhonacamara.api.service.SyncHalt;
import br.com.deolhonacamara.api.service.SyncProgressService;
import br.com.deolhonacamara.api.service.SyncRunControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
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
    private final SyncExecutionGate syncExecutionGate;
    private final SyncRunControl syncRunControl;
    private final Mapper mapper = Mapper.INSTANCE;


    public void syncSpeeches() {
        if (!syncExecutionGate.enter(SyncFlows.SPEECHES)) {
            return;
        }
        log.info("Starting speech synchronization from Câmara API...");

        try {
            // Get or create current execution for Speeches flow
            SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("Speeches");

            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync speeches", politicians.size());

            int totalSpeeches = currentExecution.getProcessedCount() == null ? 0 : currentExecution.getProcessedCount();
            int errors = currentExecution.getErrorCount() == null ? 0 : currentExecution.getErrorCount();
            int alreadyDone = currentExecution.getCurrentPage() == null ? 0 : currentExecution.getCurrentPage();
            int processedPoliticians = 0;
            int totalPoliticians = politicians.size();

            for (PoliticianEntity politician : politicians) {
                processedPoliticians++;
                if (processedPoliticians <= alreadyDone) {
                    continue;
                }
                syncRunControl.ensureRunning(SyncFlows.SPEECHES);
                try {
                    log.info("Processing politician {}/{}: {} (ID: {})", processedPoliticians, totalPoliticians, politician.getName(), politician.getId());

                    var speechesResponse = camaraDeputadosService.getSpeeches(politician.getId());
                    if (speechesResponse != null && speechesResponse.getData() != null) {
                        for (SpeechBodyDto speechDto : speechesResponse.getData()) {
                            SpeechEntity speech = mapper.toDto(politician.getId(), speechDto);
                            speechRepository.upsertSpeech(speech);
                            totalSpeeches++;
                        }
                    }

                    syncProgressService.checkpoint(currentExecution, processedPoliticians, totalPoliticians,
                            totalSpeeches, errors, String.valueOf(politician.getId()), null, null);

                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} speeches synced so far", processedPoliticians, totalSpeeches);
                    }
                } catch (Exception e) {
                    errors++;
                    syncProgressService.checkpoint(currentExecution, processedPoliticians, totalPoliticians,
                            totalSpeeches, errors, String.valueOf(politician.getId()), null, null);
                    log.error("Error syncing speeches for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            syncRunControl.ensureRunning(SyncFlows.SPEECHES);
            syncProgressService.markExecutionCompleted("Speeches", currentExecution.getExecutionId());
            log.info("Speech synchronization completed: {} speeches synced for {} politicians.", totalSpeeches, processedPoliticians);

        } catch (SyncHalt halt) {
            syncProgressService.getCurrentProgress("Speeches")
                    .filter(SyncProgressEntity::isRunning)
                    .ifPresent(exec -> syncProgressService.markExecutionHalted("Speeches", exec.getExecutionId(), halt.getStatus()));
            log.info("Speech synchronization {}", halt.getStatus());
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
        } finally {
            syncExecutionGate.release(SyncFlows.SPEECHES);
        }
    }
}
