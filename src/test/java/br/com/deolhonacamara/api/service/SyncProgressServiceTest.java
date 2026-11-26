package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.SyncProgressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncProgressServiceTest {

    @Mock
    private SyncProgressRepository syncProgressRepository;

    @InjectMocks
    private SyncProgressService syncProgressService;

    @Test
    void shouldCreateNewExecutionWhenNoProgressExists() {
        when(syncProgressRepository.findFirstByFlowNameOrderByStartTimeDesc("flow")).thenReturn(Optional.empty());
        when(syncProgressRepository.save(any(SyncProgressEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SyncProgressEntity execution = syncProgressService.getOrCreateCurrentExecution("flow");

        assertEquals("flow", execution.getFlowName());
        assertEquals("running", execution.getStatus());
        assertEquals(1, execution.getCurrentPage());
        assertNotNull(execution.getExecutionId());
        verify(syncProgressRepository).save(any(SyncProgressEntity.class));
    }

    @Test
    void shouldResumeExistingRunningExecution() {
        SyncProgressEntity existing = SyncProgressEntity.builder()
                .flowName("flow")
                .executionId("exec-1")
                .status("running")
                .currentPage(7)
                .build();
        when(syncProgressRepository.findFirstByFlowNameOrderByStartTimeDesc("flow")).thenReturn(Optional.of(existing));

        SyncProgressEntity execution = syncProgressService.getOrCreateCurrentExecution("flow");

        assertSame(existing, execution);
        verify(syncProgressRepository, never()).save(any(SyncProgressEntity.class));
    }

    @Test
    void shouldUpdateLastPropositionProgressForExistingExecution() {
        SyncProgressEntity progress = SyncProgressEntity.builder()
                .flowName("flow")
                .executionId("exec-1")
                .status("running")
                .build();
        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 10, 12, 30);
        when(syncProgressRepository.findByFlowNameAndExecutionId("flow", "exec-1")).thenReturn(Optional.of(progress));

        syncProgressService.updateLastPropositionProgress("flow", "exec-1", 99L, 7L, updatedAt);

        ArgumentCaptor<SyncProgressEntity> captor = ArgumentCaptor.forClass(SyncProgressEntity.class);
        verify(syncProgressRepository).save(captor.capture());
        SyncProgressEntity saved = captor.getValue();
        assertEquals(99L, saved.getLastPropositionId());
        assertEquals(7L, saved.getLastTramitacaoId());
        assertEquals(updatedAt, saved.getLastPropositionUpdatedAt());
        assertNotNull(saved.getLastUpdated());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldMarkExecutionAsCompleted() {
        SyncProgressEntity progress = SyncProgressEntity.builder()
                .flowName("flow")
                .executionId("exec-1")
                .status("running")
                .build();
        when(syncProgressRepository.findByFlowNameAndExecutionId("flow", "exec-1")).thenReturn(Optional.of(progress));

        syncProgressService.markExecutionCompleted("flow", "exec-1");

        assertTrue(progress.isCompleted());
        assertNotNull(progress.getEndTime());
        assertNotNull(progress.getUpdatedAt());
        verify(syncProgressRepository).save(progress);
    }
}
