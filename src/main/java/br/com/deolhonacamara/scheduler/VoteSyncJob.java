package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.VoteBodyDto;
import br.com.deolhonacamara.api.dto.VoteResponseBodyDto;
import br.com.deolhonacamara.api.dto.VotingBodyDto;
import br.com.deolhonacamara.api.dto.VotingByIdResponseBodyDto;
import br.com.deolhonacamara.api.dto.VotingResponseBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.model.VotingEntity;
import br.com.deolhonacamara.api.repository.VotingRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import lombok.extern.log4j.Log4j2;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class VoteSyncJob {

    private static final String ANONYMOUS_KEY_DELIMITER = "|";
    private static final String UNKNOWN_DATE = "unknownDate";
    private static final String UNKNOWN_TYPE = "unknownType";

    private final VotingRepository votingRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final SyncProgressService syncProgressService;
    private final Executor syncExecutor;
    private final PropertiesConfig propertiesConfig;
    private final Mapper mapper = Mapper.INSTANCE;

    public VoteSyncJob(
            VotingRepository votingRepository,
            CamaraDeputadosService camaraDeputadosService,
            SyncProgressService syncProgressService,
            @Qualifier("syncExecutor") Executor syncExecutor,
            PropertiesConfig propertiesConfig) {
        this.votingRepository = votingRepository;
        this.camaraDeputadosService = camaraDeputadosService;
        this.syncProgressService = syncProgressService;
        this.syncExecutor = syncExecutor;
        this.propertiesConfig = propertiesConfig;
    }

    // Runs daily at 02:00 (Brasília time)
    @Scheduled(cron = "0 0 2 * * *", zone = "America/Recife")
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
                syncProgressService.getCurrentProgress("Votings")
                    .filter(SyncProgressEntity::isRunning)
                    .ifPresent(exec -> syncProgressService.markExecutionFailed("Votings", exec.getExecutionId()));
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
                    processSingleVoting(votingDto);
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

    private void processSingleVoting(VotingBodyDto votingDto) {
        try {
            VotingByIdResponseBodyDto voting = camaraDeputadosService.getVotingById(votingDto.getId());
            if (voting != null && voting.getBody() != null) {
                var body = voting.getBody();
                VotingEntity votingEntity = mapper.toEntity(body);
                votingRepository.upsertVote(votingEntity);

                processVotesInVoting(votingDto.getId());
            }
        } catch (Exception e) {
            log.error("Error processing voting {}: ", votingDto.getId(), e);
        }
    }

    private void processVotesInVoting(String votingId) {
        try {
            VoteResponseBodyDto votesResponse = camaraDeputadosService.getVotesInVoting(votingId);
            if (votesResponse == null || votesResponse.getData() == null) {
                log.info("No vote data found for voting {}, skipping vote processing", votingId);
                return;
            }

            log.info("Processing {} votes for voting {}", votesResponse.getData().size(), votingId);

            int maxParallel = Math.max(1, propertiesConfig.getVoteSyncMaxParallelTasks());
            Semaphore limiter = new Semaphore(maxParallel);
            List<CompletableFuture<Void>> voteTasks = new ArrayList<>();
            for (VoteBodyDto voteBodyDto : votesResponse.getData()) {
                try {
                    limiter.acquire();
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    log.error("Vote save interrupted while waiting for permit for voting {}: ", votingId, interruptedException);
                    break;
                }

                CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                    try {
                        saveVote(votingId, voteBodyDto);
                    } finally {
                        limiter.release();
                    }
                }, syncExecutor).exceptionally(ex -> {
                    log.error("Async error saving vote for voting {}: ", votingId, ex);
                    return null;
                });

                voteTasks.add(task);
            }

            try {
                CompletableFuture.allOf(voteTasks.toArray(new CompletableFuture[0])).join();
            } catch (Exception aggregateException) {
                log.error("Error waiting vote tasks completion for voting {}", votingId, aggregateException);
            }
        } catch (Exception e) {
            log.error("Error processing votes for voting {}: ", votingId, e);
        }
    }

    private void saveVote(String votingId, VoteBodyDto voteBodyDto) {
        try {
            // Vote-to-deputy association uses the deputado.id field returned by the Câmara API
            Integer deputyId = voteBodyDto.getDeputado() != null ? voteBodyDto.getDeputado().getId() : null;
            String anonymousKey = votingId + ANONYMOUS_KEY_DELIMITER
                    + Objects.toString(voteBodyDto.getDataRegistroVoto(), UNKNOWN_DATE)
                    + ANONYMOUS_KEY_DELIMITER
                    + Objects.toString(voteBodyDto.getTipoVoto(), UNKNOWN_TYPE);
            String voteId = deputyId != null
                    ? votingId + "-" + deputyId
                    : votingId + "-anon-" + UUID.nameUUIDFromBytes(anonymousKey.getBytes(StandardCharsets.UTF_8));
            var entity = mapper.toEntity(voteBodyDto, voteId, votingId);
            votingRepository.saveVote(entity);
        } catch (Exception e) {
            log.error("Error saving vote for voting {}: ", votingId, e);
        }
    }
}
