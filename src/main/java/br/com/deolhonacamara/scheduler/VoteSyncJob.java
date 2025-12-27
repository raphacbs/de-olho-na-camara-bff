package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.VoteBodyDto;
import br.com.deolhonacamara.api.dto.VoteResponseBodyDto;
import br.com.deolhonacamara.api.dto.VotingBodyDto;
import br.com.deolhonacamara.api.dto.VotingByIdResponseBodyDto;
import br.com.deolhonacamara.api.dto.VotingResponseBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.model.VotingEntity;
import br.com.deolhonacamara.api.repository.VotingRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class VoteSyncJob {

    private final VotingRepository votingRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final SyncProgressService syncProgressService;
    private final Mapper mapper = Mapper.INSTANCE;

    // Runs daily at 02:00 (Brasília time)
    @Scheduled(cron = "0 0 2 * * *", zone = "America/Sao_Paulo")
    public void syncVotes() {
        // Schedule uses last 3 months as default
        syncVotes(null, null);
    }

    public void syncVotes(LocalDate startDate, LocalDate endDate) {
        LocalDate effectiveStartDate = startDate != null ? startDate : LocalDate.now().minusMonths(3);
        LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.now();

        log.info("Starting vote synchronization from Câmara API ({} to {})...",
                effectiveStartDate, effectiveEndDate);

        try {
            // Get or create current execution for Votings flow
            SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("Votings");
            Integer startPage = currentExecution.getTotalPages() == null ?
                    1 : currentExecution.getCurrentPage() + 1; // Start from page 1 if new execution, otherwise continue from next page

            log.info("Processing vote synchronization - Execution: {}, Starting from page: {} (last processed: {})",
                    currentExecution.getExecutionId(), startPage, currentExecution.getCurrentPage());

            processVotingPages(startPage, currentExecution, effectiveStartDate, effectiveEndDate);
            log.info("Vote synchronization completed for execution {}", currentExecution.getExecutionId());
        } catch (Exception e) {
            log.error("Error syncing Votings: ", e);
            // Mark execution as failed if something goes wrong
            try {
                SyncProgressEntity currentExecution = syncProgressService.getCurrentProgress("Votings")
                    .filter(SyncProgressEntity::isRunning)
                    .orElse(null);
                if (currentExecution != null) {
                    syncProgressService.markExecutionFailed("Votings", currentExecution.getExecutionId());
                }
            } catch (Exception e2) {
                log.error("Error marking execution as failed: ", e2);
            }
        }
    }

    private VotingResponseBodyDto getVotingLastMonthsWithPage(Integer page, LocalDate startDate, LocalDate endDate) {
        try {
            return camaraDeputadosService.getVotingLastMonthsWithPage(startDate, endDate, page);
        } catch (Exception e) {
            log.warn("Error getting votings for page {}: {}", page, e.getMessage());
            return null;
        }
    }

    private Integer getLastPageNumber(VotingResponseBodyDto response) {
        if (response != null && response.getLinks() != null) {
            for (var link : response.getLinks()) {
                Integer lastPage = link.getNumberLastPage();
                if (lastPage != null) {
                    return lastPage;
                }
            }
        }
        return 1;
    }

    private void processVotingPages(Integer page, SyncProgressEntity currentExecution, LocalDate startDate, LocalDate endDate) {
        try {
            VotingResponseBodyDto votingsResponse = getVotingLastMonthsWithPage(page, startDate, endDate);
            if (votingsResponse == null) {
                log.warn("Failed to get votings for page {}, will retry once more", page);
                // Try one more time for this page before giving up
                votingsResponse = getVotingLastMonthsWithPage(page, startDate, endDate);
                if (votingsResponse == null) {
                    log.error("Failed to get votings for page {} after retry, skipping to next page", page);
                    // Continue to next page instead of stopping completely
                    if (page < 1000) {
                        processVotingPages(page + 1, currentExecution, startDate, endDate);
                    }
                    return;
                }
            }

            Integer lastPage = getLastPageNumber(votingsResponse);
            log.info("Processing page {}/{}", page, lastPage);

            // Set total pages on first response
            if (currentExecution.getTotalPages() == null) {
                syncProgressService.updateTotalPages("Votings", currentExecution.getExecutionId(), lastPage);
                log.info("Set total pages to {} for execution {}", lastPage, currentExecution.getExecutionId());
            }

            if (votingsResponse.getData() != null) {
                log.info("Found {} votings in page {}", votingsResponse.getData().size(), page);

                for (VotingBodyDto votingDto : votingsResponse.getData()) {
                    try {
                        // busca as votações por id na api da camara
                        VotingByIdResponseBodyDto voting = camaraDeputadosService.getVotingById(votingDto.getId());
                        if (voting != null && voting.getBody() != null) {
                            var body = voting.getBody();
                            VotingEntity votingEntity = mapper.toEntity(body);
                            votingRepository.upsertVote(votingEntity);

                            // Process Votings with pagination
                            processVotesInVoting(votingDto.getId(), 1);
                        }
                    } catch (Exception e) {
                        log.error("Error processing voting {}: ", votingDto.getId(), e);
                    }
                }

                if (page < lastPage) {
                    // Update progress before moving to next page
                    syncProgressService.updateCurrentPage("Votings", currentExecution.getExecutionId(), page);
                    log.info("Starting processing of next page {} for votings", page + 1);
                    processVotingPages(page + 1, currentExecution, startDate, endDate);
                } else {
                    // Update final progress and mark as completed when reaching the last page
                    syncProgressService.updateCurrentPage("Votings", currentExecution.getExecutionId(), page);
                    syncProgressService.markExecutionCompleted("Votings", currentExecution.getExecutionId());
                }
            } else {
                log.info("No voting data found in page {}, this likely means we've reached the end of available votings", page);
                // If no data is returned for votings, it means we've reached the end, mark as completed
                syncProgressService.markExecutionCompleted("Votings", currentExecution.getExecutionId());
            }
        } catch (Exception e) {
            log.error("Error processing voting page {}: ", page, e);
            // Try to continue with next page, but with limits to prevent infinite recursion
            if (page < 1000) {  // Reasonable limit to prevent infinite recursion
                try {
                    log.info("Attempting to continue with next page {} after error", page + 1);
                    processVotingPages(page + 1, currentExecution, startDate, endDate);
                } catch (Exception e2) {
                    log.error("Failed to continue processing after page {} error: ", page, e2);
                }
            } else {
                log.warn("Reached maximum page limit ({}), stopping processing to prevent infinite recursion", page);
                // Mark execution as failed when reaching the limit
                syncProgressService.markExecutionFailed("Votings", currentExecution.getExecutionId());
            }
        }
    }

    private VoteResponseBodyDto getVotesInVotingWithPage(String votingId, Integer page) {
        try {
            return camaraDeputadosService.getVotesInVotingWithPage(votingId, page);
        } catch (Exception e) {
            log.warn("Error getting votes for voting {} page {}: {}", votingId, page, e.getMessage());
            return null;
        }
    }

    private Integer getLastPageNumberForVotes(VoteResponseBodyDto response) {
        if (response != null && response.getLinks() != null) {
            for (var link : response.getLinks()) {
                Integer lastPage = link.getNumberLastPage();
                if (lastPage != null) {
                    return lastPage;
                }
            }
        }
        return 1;
    }

    private void processVotesInVoting(String votingId, Integer page) {
        try {
            VoteResponseBodyDto votesResponse = getVotesInVotingWithPage(votingId, page);
            if (votesResponse == null) {
                log.warn("Failed to get votes for voting {} page {}, will retry once more", votingId, page);
                // Try one more time for this page before giving up
                votesResponse = getVotesInVotingWithPage(votingId, page);
                if (votesResponse == null) {
                    log.error("Failed to get votes for voting {} page {} after retry, skipping to next page", votingId, page);
                    // Continue to next page instead of stopping completely
                    if (page < 1000) {
                        processVotesInVoting(votingId, page + 1);
                    }
                    return;
                }
            }

            if (votesResponse.getData() != null) {
            log.info("Processing {} votes for voting {} in page {}", votesResponse.getData().size(), votingId, page);

            for (VoteBodyDto voteBodyDto : votesResponse.getData()) {
                try {
                    var entity = mapper.toEntity(voteBodyDto, votingId, votingId);
                    votingRepository.saveVote(entity);
                } catch (Exception e) {
                    log.error("Error saving vote for voting {}: ", votingId, e);
                }
            }

                Integer lastPage = getLastPageNumberForVotes(votesResponse);
                if (page < lastPage) {
                    log.info("Starting processing of next page {} for votes in voting {}", page + 1, votingId);
                    processVotesInVoting(votingId, page + 1);
                }
            } else {
                log.info("No vote data found for voting {} page {}, this likely means we've reached the end of available data", votingId, page);
                // If no data is returned, it means we've reached the end, don't continue to next page
            }
        } catch (Exception e) {
            log.error("Error processing votes for voting {} page {}: ", votingId, page, e);
            // Continue to next page even if current page failed completely
            // Add protection against infinite recursion
            if (page < 1000) {  // Reasonable limit to prevent infinite recursion
                try {
                    log.info("Attempting to continue with next page {} for voting {} after error", page + 1, votingId);
                    processVotesInVoting(votingId, page + 1);
                } catch (Exception e2) {
                    log.error("Failed to continue processing votes for voting {} after page {} error: ", votingId, page, e2);
                }
            } else {
                log.warn("Reached maximum page limit ({}) for voting {}, stopping processing to prevent infinite recursion", page, votingId);
            }
        }
    }
}
