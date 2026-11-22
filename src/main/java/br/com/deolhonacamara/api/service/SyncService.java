package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.scheduler.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Log4j2
public class SyncService {

    private final PoliticianSyncJob politicianSyncJob;
    private final ExpenseSyncJob expenseSyncJob;
    private final VoteSyncJob voteSyncJob;
    private final SpeechSyncJob speechSyncJob;
    private final PropositionSyncJob propositionSyncJob;
    private final PresenceSyncJob presenceSyncJob;
    private final PropositionTramitationSyncJob propositionTramitationSyncJob;
    private final RecentPropositionSyncJob recentPropositionSyncJob;
    private final SyncExecutionGate syncExecutionGate;
    private final SyncRunControl syncRunControl;

    @Async("syncExecutor")
    public void syncAll() {
        log.info("Starting full synchronization of all features...");
        if (!syncExecutionGate.runFull(this::runSyncAll)) {
            log.warn("Full sync skipped because another flow is running");
        }
    }

    private void runSyncAll() {
        try {
            boolean stopped = false;
            stopped = runStep(stopped, "politicians", politicianSyncJob::syncPoliticians);
            if (!stopped) {
                Thread.sleep(2000);
            }
            stopped = runStep(stopped, "expenses", expenseSyncJob::syncExpenses);
            stopped = runStep(stopped, "votes", () -> voteSyncJob.syncVotes());
            stopped = runStep(stopped, "speeches", speechSyncJob::syncSpeeches);
            stopped = runStep(stopped, "propositions", propositionSyncJob::syncPropositions);
            log.info(stopped ? "Full synchronization stopped by Cabine" : "Full synchronization completed successfully!");
        } catch (Exception e) {
            log.error("Error during full synchronization: ", e);
            throw new RuntimeException("Synchronization failed", e);
        } finally {
            syncRunControl.clear(SyncFlows.ALL);
        }
    }

    private boolean runStep(boolean stopped, String step, Runnable job) {
        if (stopped || syncRunControl.isRequested(SyncFlows.ALL)) {
            log.info("Skipping {} because the full sync was stopped", step);
            return true;
        }
        job.run();
        return syncRunControl.isRequested(SyncFlows.ALL);
    }

    @Async("syncExecutor")
    public void syncPoliticians() {
        log.info("Starting politician synchronization...");
        politicianSyncJob.syncPoliticians();
    }

    @Async("syncExecutor")
    public void syncExpenses() {
        log.info("Starting expense synchronization...");
        expenseSyncJob.syncExpenses();
    }

    @Async("syncExecutor")
    public void syncVotes() {
        log.info("Starting vote synchronization...");
        voteSyncJob.syncVotes(null, null);
    }

    @Async("syncExecutor")
    public void syncVotes(LocalDate startDate, LocalDate endDate) {
        log.info("Starting vote synchronization with date range: {} to {}", startDate, endDate);
        voteSyncJob.syncVotes(startDate, endDate);
    }

    @Async("syncExecutor")
    public void syncSpeeches() {
        log.info("Starting speech synchronization...");
        speechSyncJob.syncSpeeches();
    }

    @Async("syncExecutor")
    public void syncPropositions() {
        log.info("Starting proposition synchronization...");
        propositionSyncJob.syncPropositions();
    }

    @Async("syncExecutor")
    public void syncPresence() {
        log.warn("Presence synchronization is disabled: endpoint /deputados/{id}/presencas does not exist in API REST v2");
        presenceSyncJob.syncPresence(); // Will only log a warning
    }

    @Async("syncExecutor")
    public void syncTramitations() {
        log.info("Starting tramitation synchronization...");
        this.propositionTramitationSyncJob.syncPropositionTramitations();
    }

    @Async("syncExecutor")
    public void syncRecentPropositions() {
        log.info("Starting recent proposition synchronization...");
        recentPropositionSyncJob.syncRecentPropositions();
    }
}

