package br.com.deolhonacamara.api.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SyncCronTest {

    @Test
    void convertsDailyTimeToSpringCron() {
        assertEquals("0 30 1 * * *", SyncCron.fromTime("01:30"));
        assertEquals("01:30", SyncCron.toTime("0 30 1 * * *"));
    }

    @Test
    void rejectsAnImpossibleTime() {
        assertThrows(IllegalArgumentException.class, () -> SyncCron.fromTime("25:00"));
    }
}

class SyncRunControlTest {

    @Test
    void pauseOfTheFullSyncStopsTheCurrentChild() {
        SyncRunControl control = new SyncRunControl();
        control.request(SyncFlows.ALL, SyncRunControl.PAUSE);

        assertTrue(control.isRequested(SyncFlows.EXPENSES));
        SyncHalt halt = assertThrows(SyncHalt.class, () -> control.ensureRunning(SyncFlows.EXPENSES));
        assertEquals("paused", halt.getStatus());
    }
}
