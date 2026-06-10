package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.dto.PropositionTramitationResponseBodyDto;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.PropositionTramitationService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropositionTramitationSyncJobTest {

    @Mock
    private PropositionRepository propositionRepository;

    @Mock
    private CamaraDeputadosService camaraDeputadosService;

    @Mock
    private PropositionTramitationService propositionTramitationService;

    @Mock
    private SyncProgressService syncProgressService;

    @Mock
    private PropertiesConfig propertiesConfig;

    private final Executor syncExecutor = Runnable::run;

    @Test
    void shouldResumeFromSavedPropositionAndSequence() {
        PropositionTramitationSyncJob job = new PropositionTramitationSyncJob(
                propositionRepository,
                camaraDeputadosService,
                propositionTramitationService,
                syncProgressService,
                propertiesConfig,
                syncExecutor
        );

        SyncProgressEntity currentExecution = SyncProgressEntity.builder()
                .executionId("exec-1")
                .currentPage(0)
                .build();
        SyncProgressEntity persisted = SyncProgressEntity.builder()
                .lastPropositionId(10L)
                .lastTramitacaoId(4L)
                .build();
        PropositionEntity proposition = PropositionEntity.builder().id(10).build();

        when(syncProgressService.getOrCreateCurrentExecution("PropositionTramitations")).thenReturn(currentExecution);
        when(syncProgressService.getCurrentProgress("PropositionTramitations")).thenReturn(Optional.of(persisted));
        when(propertiesConfig.getPropositionSyncChunkSize()).thenReturn(100);
        when(propertiesConfig.getPropositionSyncMaxInFlight()).thenReturn(1);
        when(propositionRepository.findPropositionsAfterId(9, 100)).thenReturn(List.of(proposition));
        when(propositionRepository.findPropositionsAfterId(10, 100)).thenReturn(List.of());
        when(camaraDeputadosService.getTramitacoesByPropositionIdWithPage(eq(10), any(LocalDate.class), any(LocalDate.class), eq(1)))
                .thenReturn(new PropositionTramitationResponseBodyDto(List.of(
                        new PropositionBodyDto.PropositionStatusDto(LocalDateTime.of(2026, 1, 1, 10, 0), 3, "CCJ", "uri-1", "rel-1", "regime", "desc-1", "type-1", "sit-1", "code-1", "dispatch-1", "url-1", "scope-1", "app-1"),
                        new PropositionBodyDto.PropositionStatusDto(LocalDateTime.of(2026, 1, 1, 11, 0), 5, "CCJ", "uri-2", "rel-2", "regime", "desc-2", "type-2", "sit-2", "code-2", "dispatch-2", "url-2", "scope-2", "app-2")
                )));
        when(camaraDeputadosService.getTramitacoesByPropositionIdWithPage(eq(10), any(LocalDate.class), any(LocalDate.class), eq(2)))
                .thenReturn(new PropositionTramitationResponseBodyDto(List.of()));

        job.syncPropositionTramitations();

        verify(propositionRepository).findPropositionsAfterId(9, 100);
        verify(propositionTramitationService).upsertTramitationEntities(argThat(argThatContainsOnlySequence(5)));
        verify(syncProgressService).updateCurrentPage("PropositionTramitations", "exec-1", 10);
        verify(syncProgressService, times(2)).updateLastPropositionProgress(eq("PropositionTramitations"), eq("exec-1"), eq(10L), any(), any(LocalDateTime.class));
        verify(syncProgressService).markExecutionCompleted("PropositionTramitations", "exec-1");
    }

    private ArgumentMatcher<List<br.com.deolhonacamara.api.model.PropositionTramitationEntity>> argThatContainsOnlySequence(int expectedSequence) {
        return list -> list != null
                && list.size() == 1
                && list.get(0).getPropositionId().equals(10)
                && list.get(0).getSequence().equals(expectedSequence);
    }
}
