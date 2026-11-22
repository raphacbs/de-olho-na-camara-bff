package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.SyncProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SyncProgressService {

    private final SyncProgressRepository syncProgressRepository;

    /**
     * Get or create the current execution for a specific flow
     * If the last execution is completed or doesn't exist, creates a new one
     */
    @Transactional
    public SyncProgressEntity getOrCreateCurrentExecution(String flowName) {
        Optional<SyncProgressEntity> lastExecution = syncProgressRepository.findFirstByFlowNameOrderByStartTimeDesc(flowName);

        if (lastExecution.isPresent() && lastExecution.get().isPaused()) {
            SyncProgressEntity paused = lastExecution.get();
            paused.setStatus("running");
            paused.setEndTime(null);
            paused.setLastUpdated(LocalDateTime.now());
            paused.setUpdatedAt(LocalDateTime.now());
            SyncProgressEntity saved = syncProgressRepository.save(paused);
            log.info("Resuming paused execution {} for flow {} from page {}",
                    saved.getExecutionId(), flowName, saved.getCurrentPage());
            return saved;
        }

        // Concluída, cancelada ou falha abre outra. Pausada continua do ponto gravado.
        if (lastExecution.isEmpty() || lastExecution.get().isCompleted()
                || lastExecution.get().isFailed() || lastExecution.get().isCancelled()) {
            String executionId = UUID.randomUUID().toString();
            SyncProgressEntity newExecution = SyncProgressEntity.builder()
                    .flowName(flowName)
                    .executionId(executionId)
                    .status("running")
                    .currentPage(0)
                    .processedCount(0)
                    .errorCount(0)
                    .startTime(LocalDateTime.now())
                    .lastUpdated(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            SyncProgressEntity saved = syncProgressRepository.save(newExecution);
            log.info("Created new execution {} for flow {}", executionId, flowName);
            return saved;
        }

        // Return the existing running execution
        log.info("Resuming existing execution {} for flow {} from page {}",
                lastExecution.get().getExecutionId(), flowName, lastExecution.get().getCurrentPage());
        return lastExecution.get();
    }

    /**
     * Grava página, totais e o último item numa única escrita.
     * totalPages só entra se ainda estiver vazio, para um fluxo não resetar o total de outro.
     * Argumentos nulos não apagam o que já estava na execução.
     */
    @Transactional
    public void checkpoint(SyncProgressEntity execution,
                           Integer currentPage,
                           Integer totalPages,
                           Integer processedCount,
                           Integer errorCount,
                           String lastCursor,
                           Long lastPropositionId,
                           Long lastTramitacaoId) {
        if (totalPages != null && execution.getTotalPages() == null) {
            execution.setTotalPages(totalPages);
        }
        if (currentPage != null) {
            execution.setCurrentPage(currentPage);
        }
        if (processedCount != null) {
            execution.setProcessedCount(processedCount);
        }
        if (errorCount != null) {
            execution.setErrorCount(errorCount);
        }
        if (lastCursor != null) {
            execution.setLastCursor(lastCursor);
        }
        if (lastPropositionId != null) {
            execution.setLastPropositionId(lastPropositionId);
            execution.setLastPropositionUpdatedAt(LocalDateTime.now());
        }
        if (lastTramitacaoId != null) {
            execution.setLastTramitacaoId(lastTramitacaoId);
        }
        LocalDateTime now = LocalDateTime.now();
        execution.setLastUpdated(now);
        execution.setUpdatedAt(now);
        syncProgressRepository.save(execution);
    }

    /**
     * Update the current page for a specific execution
     */
    @Transactional
    public void updateCurrentPage(String flowName, String executionId, Integer currentPage) {
        Optional<SyncProgressEntity> execution = syncProgressRepository.findByFlowNameAndExecutionId(flowName, executionId);

        if (execution.isPresent()) {
            SyncProgressEntity progress = execution.get();
            progress.setCurrentPage(currentPage);
            progress.setLastUpdated(LocalDateTime.now());
            progress.setUpdatedAt(LocalDateTime.now());
            syncProgressRepository.save(progress);
            log.debug("Updated current page for flow {} execution {} to {}", flowName, executionId, currentPage);
        } else {
            log.warn("Execution {} not found for flow {}", executionId, flowName);
        }
    }

    /**
     * Update last processed proposition/tramitacao info to allow resuming from a specific proposition
     */
    @Transactional
    public void updateLastPropositionProgress(String flowName, String executionId, Long lastPropositionId, Long lastTramitacaoId, LocalDateTime updatedAt) {
        Optional<SyncProgressEntity> execution = syncProgressRepository.findByFlowNameAndExecutionId(flowName, executionId);

        if (execution.isPresent()) {
            SyncProgressEntity progress = execution.get();
            if (lastPropositionId != null) {
                progress.setLastPropositionId(lastPropositionId);
                progress.setLastCursor(String.valueOf(lastPropositionId));
            }
            if (lastTramitacaoId != null) {
                progress.setLastTramitacaoId(lastTramitacaoId);
            }
            progress.setLastPropositionUpdatedAt(updatedAt == null ? LocalDateTime.now() : updatedAt);
            progress.setLastUpdated(LocalDateTime.now());
            progress.setUpdatedAt(LocalDateTime.now());
            syncProgressRepository.save(progress);
            log.debug("Updated last proposition progress for flow {} execution {} to propositionId={} tramitacaoId={}", flowName, executionId, progress.getLastPropositionId(), progress.getLastTramitacaoId());
        } else {
            log.warn("Execution {} not found for flow {}", executionId, flowName);
        }
    }

    @Transactional
    public void clearLastTramitacao(SyncProgressEntity execution) {
        LocalDateTime now = LocalDateTime.now();
        execution.setLastTramitacaoId(null);
        execution.setLastUpdated(now);
        execution.setUpdatedAt(now);
        if (execution.getId() != null) {
            syncProgressRepository.clearLastTramitacaoId(execution.getId(), now);
        }
    }

    /**
     * Update total pages for a specific execution
     */
    @Transactional
    public void updateTotalPages(String flowName, String executionId, Integer totalPages) {
        Optional<SyncProgressEntity> execution = syncProgressRepository.findByFlowNameAndExecutionId(flowName, executionId);

        if (execution.isPresent()) {
            SyncProgressEntity progress = execution.get();
            progress.setTotalPages(totalPages);
            progress.setLastUpdated(LocalDateTime.now());
            progress.setUpdatedAt(LocalDateTime.now());
            syncProgressRepository.save(progress);
            log.info("Updated total pages for flow {} execution {} to {}", flowName, executionId, totalPages);
        } else {
            log.warn("Execution {} not found for flow {}", executionId, flowName);
        }
    }

    /**
     * Mark execution as completed
     */
    @Transactional
    public void markExecutionCompleted(String flowName, String executionId) {
        Optional<SyncProgressEntity> execution = syncProgressRepository.findByFlowNameAndExecutionId(flowName, executionId);

        if (execution.isPresent()) {
            SyncProgressEntity progress = execution.get();
            progress.setStatus("completed");
            progress.setEndTime(LocalDateTime.now());
            progress.setLastUpdated(LocalDateTime.now());
            progress.setUpdatedAt(LocalDateTime.now());
            syncProgressRepository.save(progress);
            log.info("Marked execution {} for flow {} as completed", executionId, flowName);
        } else {
            log.warn("Execution {} not found for flow {}", executionId, flowName);
        }
    }

    @Transactional
    public void markExecutionHalted(String flowName, String executionId, String status) {
        Optional<SyncProgressEntity> execution = syncProgressRepository.findByFlowNameAndExecutionId(flowName, executionId);
        if (execution.isEmpty()) {
            log.warn("Execution {} not found for flow {}", executionId, flowName);
            return;
        }
        SyncProgressEntity progress = execution.get();
        LocalDateTime now = LocalDateTime.now();
        progress.setStatus(status);
        progress.setEndTime("paused".equals(status) ? null : now);
        progress.setLastUpdated(now);
        progress.setUpdatedAt(now);
        syncProgressRepository.save(progress);
        log.info("Marked execution {} for flow {} as {}", executionId, flowName, status);
    }

    /**
     * Mark execution as failed
     */
    @Transactional
    public void markExecutionFailed(String flowName, String executionId) {
        Optional<SyncProgressEntity> execution = syncProgressRepository.findByFlowNameAndExecutionId(flowName, executionId);

        if (execution.isPresent()) {
            SyncProgressEntity progress = execution.get();
            progress.setStatus("failed");
            progress.setEndTime(LocalDateTime.now());
            progress.setLastUpdated(LocalDateTime.now());
            progress.setUpdatedAt(LocalDateTime.now());
            syncProgressRepository.save(progress);
            log.info("Marked execution {} for flow {} as failed", executionId, flowName);
        } else {
            log.warn("Execution {} not found for flow {}", executionId, flowName);
        }
    }

    /**
     * Get current page for a specific flow (from the latest running execution)
     */
    public Integer getCurrentPage(String flowName) {
        SyncProgressEntity currentExecution = getOrCreateCurrentExecution(flowName);
        return currentExecution.getCurrentPage();
    }

    /**
     * Get progress information for the current execution of a specific flow
     */
    public Optional<SyncProgressEntity> getCurrentProgress(String flowName) {
        return syncProgressRepository.findFirstByFlowNameOrderByStartTimeDesc(flowName);
    }
}

