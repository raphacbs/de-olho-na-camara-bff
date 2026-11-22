package br.com.deolhonacamara.api.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SyncExecutionGateTest {

    @Test
    void blocksTheSameFlowUntilRelease() {
        SyncExecutionGate gate = new SyncExecutionGate();

        assertTrue(gate.acquire(SyncFlows.EXPENSES).isEmpty());
        assertTrue(gate.acquire(SyncFlows.EXPENSES).isPresent());

        gate.release(SyncFlows.EXPENSES);
        assertTrue(gate.acquire(SyncFlows.EXPENSES).isEmpty());
    }

    @Test
    void blocksChildWhileParentRunsAndParentWhileChildRuns() {
        SyncExecutionGate gate = new SyncExecutionGate();
        gate.acquire(SyncFlows.POLITICIANS);

        assertTrue(gate.acquire(SyncFlows.EXPENSES).isPresent());
        assertTrue(gate.acquire(SyncFlows.SPEECHES).isPresent());
        assertTrue(gate.acquire(SyncFlows.PROPOSITIONS).isPresent());
        assertTrue(gate.acquire(SyncFlows.VOTINGS).isEmpty());

        gate.release(SyncFlows.POLITICIANS);
        gate.release(SyncFlows.VOTINGS);
        gate.acquire(SyncFlows.EXPENSES);

        assertTrue(gate.acquire(SyncFlows.POLITICIANS).isPresent());
    }

    @Test
    void blocksPropositionFamilyWhileAnyOfThemRuns() {
        SyncExecutionGate gate = new SyncExecutionGate();
        gate.acquire(SyncFlows.PROPOSITIONS);

        assertTrue(gate.acquire(SyncFlows.TRAMITATIONS).isPresent());
        assertTrue(gate.acquire(SyncFlows.RECENT_PROPOSITIONS).isPresent());

        gate.release(SyncFlows.PROPOSITIONS);
        gate.acquire(SyncFlows.RECENT_PROPOSITIONS);

        assertTrue(gate.acquire(SyncFlows.TRAMITATIONS).isPresent());
        assertFalse(gate.enter(SyncFlows.TRAMITATIONS));
    }

    @Test
    void fullSyncOwnsTheInstanceAndStillRunsItsOwnSteps() {
        SyncExecutionGate gate = new SyncExecutionGate();

        assertTrue(gate.runFull(() -> {
            assertTrue(gate.blockReason(SyncFlows.VOTINGS).isPresent());
            assertTrue(gate.enter(SyncFlows.POLITICIANS));
            gate.release(SyncFlows.POLITICIANS);
            assertTrue(gate.enter(SyncFlows.EXPENSES));
            gate.release(SyncFlows.EXPENSES);
        }));

        assertTrue(gate.blockReason(SyncFlows.VOTINGS).isEmpty());
        gate.acquire(SyncFlows.VOTINGS);
        assertFalse(gate.runFull(() -> {
        }));
    }
}
