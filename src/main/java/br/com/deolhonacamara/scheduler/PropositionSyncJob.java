package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.PropositionRepository;
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
public class PropositionSyncJob {

    private final PoliticianRepository politicianRepository;
    private final PropositionRepository propositionRepository;
    private final CamaraDeputadosService camaraDeputadosService;

    // Runs daily at 04:00 (Brasília time)
    @Scheduled(cron = "0 0 4 * * *", zone = "America/Sao_Paulo")
    public void syncPropositions() {
        log.info("Starting proposition synchronization from Câmara API...");

        try {
            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync propositions", politicians.size());

            int totalPropositions = 0;
            int processedPoliticians = 0;

            for (PoliticianEntity politician : politicians) {
                try {
                    var propositionsResponse = camaraDeputadosService.getPropositions(politician.getId());
                    
                    if (propositionsResponse != null && propositionsResponse.getData() != null) {
                        for (PropositionBodyDto propositionDto : propositionsResponse.getData()) {
                            PropositionEntity proposition = PropositionEntity.builder()
                                    .id(propositionDto.getId())
                                    .type(propositionDto.getType())
                                    .number(propositionDto.getNumber())
                                    .year(propositionDto.getYear())
                                    .summary(propositionDto.getSummary())
                                    .presentationDate(propositionDto.getPresentationDate())
                                    .status(propositionDto.getStatus())
                                    .build();
                            propositionRepository.upsertProposition(proposition);
                            propositionRepository.linkPoliticianToProposition(politician.getId(), proposition.getId());
                            totalPropositions++;
                        }
                    }

                    processedPoliticians++;
                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} propositions synced so far", processedPoliticians, totalPropositions);
                    }
                } catch (Exception e) {
                    log.error("Error syncing propositions for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            log.info("Proposition synchronization completed: {} propositions synced for {} politicians.", totalPropositions, processedPoliticians);

        } catch (Exception e) {
            log.error("Error syncing propositions: ", e);
        }
    }
}

