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

    @Async("syncExecutor")
    public void syncAll() {
        log.info("Starting full synchronization of all features...");
        
        try {
            // Sync politicians first (required for other syncs)
            log.info("Step 1/6: Syncing politicians...");
            politicianSyncJob.syncPoliticians();
            
            // Wait a bit to ensure politicians are saved
            Thread.sleep(2000);
            
            // Sync expenses
            log.info("Step 2/6: Syncing expenses...");
            expenseSyncJob.syncExpenses();
            
            // Sync votes
            log.info("Step 3/6: Syncing votes...");
            voteSyncJob.syncVotes();
            
            // Sync speeches
            log.info("Step 4/6: Syncing speeches...");
            speechSyncJob.syncSpeeches();
            
            // Sync propositions
            log.info("Step 5/6: Syncing propositions...");
            propositionSyncJob.syncPropositions();
            
            // Sync presence - DISABLED: endpoint does not exist in API REST v2
            log.warn("Step 6/6: Presence sync skipped - endpoint /deputados/{id}/presencas does not exist in API REST v2");
            presenceSyncJob.syncPresence(); // Will only log a warning
            
            log.info("Full synchronization completed successfully!");
            
        } catch (Exception e) {
            log.error("Error during full synchronization: ", e);
            throw new RuntimeException("Synchronization failed", e);
        }
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
}

