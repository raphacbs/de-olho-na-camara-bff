package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.SpeechEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.SpeechRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
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
    private final Mapper mapper = Mapper.INSTANCE;


    // Runs daily at 03:00 (Brasília time)
    @Scheduled(cron = "0 0 3 * * *", zone = "America/Sao_Paulo")
    public void syncSpeeches() {
        log.info("Starting speech synchronization from Câmara API...");

        try {
            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync speeches", politicians.size());

            int totalSpeeches = 0;
            int processedPoliticians = 0;

            for (PoliticianEntity politician : politicians) {
                try {
                    var speechesResponse = camaraDeputadosService.getSpeeches(politician.getId());
                    if (speechesResponse != null && speechesResponse.getData() != null) {
                        for (SpeechBodyDto speechDto : speechesResponse.getData()) {
                            SpeechEntity speech = mapper.toDto(politician.getId(), speechDto);
                            speechRepository.upsertSpeech(speech);
                            totalSpeeches++;
                        }
                    }

                    processedPoliticians++;
                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} speeches synced so far", processedPoliticians, totalSpeeches);
                    }
                } catch (Exception e) {
                    log.error("Error syncing speeches for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            log.info("Speech synchronization completed: {} speeches synced for {} politicians.", totalSpeeches, processedPoliticians);

        } catch (Exception e) {
            log.error("Error syncing speeches: ", e);
        }
    }
}

