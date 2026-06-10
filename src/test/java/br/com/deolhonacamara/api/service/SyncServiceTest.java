package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.scheduler.ExpenseSyncJob;
import br.com.deolhonacamara.scheduler.PoliticianSyncJob;
import br.com.deolhonacamara.scheduler.PresenceSyncJob;
import br.com.deolhonacamara.scheduler.PropositionSyncJob;
import br.com.deolhonacamara.scheduler.PropositionTramitationSyncJob;
import br.com.deolhonacamara.scheduler.SpeechSyncJob;
import br.com.deolhonacamara.scheduler.VoteSyncJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SyncServiceTest {

    @Mock
    private PoliticianSyncJob politicianSyncJob;
    @Mock
    private ExpenseSyncJob expenseSyncJob;
    @Mock
    private VoteSyncJob voteSyncJob;
    @Mock
    private SpeechSyncJob speechSyncJob;
    @Mock
    private PropositionSyncJob propositionSyncJob;
    @Mock
    private PresenceSyncJob presenceSyncJob;
    @Mock
    private PropositionTramitationSyncJob propositionTramitationSyncJob;

    @InjectMocks
    private SyncService syncService;

    @Test
    void shouldRunSyncAllInOrder() {
        syncService.syncAll();

        InOrder inOrder = inOrder(
                politicianSyncJob,
                expenseSyncJob,
                voteSyncJob,
                speechSyncJob,
                propositionSyncJob,
                presenceSyncJob
        );
        inOrder.verify(politicianSyncJob).syncPoliticians();
        inOrder.verify(expenseSyncJob).syncExpenses();
        inOrder.verify(voteSyncJob).syncVotes();
        inOrder.verify(speechSyncJob).syncSpeeches();
        inOrder.verify(propositionSyncJob).syncPropositions();
        inOrder.verify(presenceSyncJob).syncPresence();

        verifyNoInteractions(propositionTramitationSyncJob);
    }

    @Test
    void shouldWrapAndStopWhenSyncAllFails() {
        doThrow(new RuntimeException("failure")).when(politicianSyncJob).syncPoliticians();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> syncService.syncAll());

        assertEquals("Synchronization failed", exception.getMessage());
        verifyNoInteractions(expenseSyncJob, voteSyncJob, speechSyncJob, propositionSyncJob, presenceSyncJob, propositionTramitationSyncJob);
    }
}
