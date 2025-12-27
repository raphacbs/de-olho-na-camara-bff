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

        // If no execution exists or the last one is completed, create a new one
        if (lastExecution.isEmpty() || lastExecution.get().isCompleted()) {
            String executionId = UUID.randomUUID().toString();
            SyncProgressEntity newExecution = SyncProgressEntity.builder()
                    .flowName(flowName)
                    .executionId(executionId)
                    .status("running")
                    .currentPage(1)
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

