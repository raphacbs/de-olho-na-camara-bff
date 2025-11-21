package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.PoliticianVoteBodyDto;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
import br.com.deolhonacamara.api.model.VoteEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.VoteRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class VoteSyncJob {

    private final PoliticianRepository politicianRepository;
    private final VoteRepository voteRepository;
    private final CamaraDeputadosService camaraDeputadosService;

    // Runs daily at 02:00 (Brasília time)
    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
    public void syncVotes() {
        log.info("Starting vote synchronization from Câmara API...");

        try {
            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync votes", politicians.size());

            int totalVotes = 0;
            int processedPoliticians = 0;
            Set<String> processedVoteIds = new HashSet<>();

            for (PoliticianEntity politician : politicians) {
                try {
                    var votesResponse = camaraDeputadosService.getVotes(politician.getId());
                    
                    if (votesResponse != null && votesResponse.getData() != null) {
                        for (PoliticianVoteBodyDto voteDto : votesResponse.getData()) {
                            // Process vote only once
                            if (!processedVoteIds.contains(voteDto.getVoteId())) {
                                // Note: Vote details need to be fetched separately or included in response
                                // For now, we'll create a basic vote entity
                                VoteEntity vote = VoteEntity.builder()
                                        .id(voteDto.getVoteId())
                                        .build();
                                voteRepository.upsertVote(vote);
                                processedVoteIds.add(voteDto.getVoteId());
                            }

                            // Link politician to vote
                            PoliticianVoteEntity politicianVote = PoliticianVoteEntity.builder()
                                    .voteId(voteDto.getVoteId())
                                    .politicianId(politician.getId())
                                    .voteOption(voteDto.getVoteOption())
                                    .build();
                            voteRepository.upsertPoliticianVote(politicianVote);
                            totalVotes++;
                        }
                    }

                    processedPoliticians++;
                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} votes synced so far", processedPoliticians, totalVotes);
                    }
                } catch (Exception e) {
                    log.error("Error syncing votes for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            log.info("Vote synchronization completed: {} votes synced for {} politicians.", totalVotes, processedPoliticians);

        } catch (Exception e) {
            log.error("Error syncing votes: ", e);
        }
    }
}

