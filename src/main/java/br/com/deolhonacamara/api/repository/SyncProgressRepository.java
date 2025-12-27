package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.SyncProgressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SyncProgressRepository extends CrudRepository<SyncProgressEntity, Long> {

    Optional<SyncProgressEntity> findByFlowNameAndExecutionId(String flowName, String executionId);

    List<SyncProgressEntity> findByFlowNameOrderByStartTimeDesc(String flowName);

    Optional<SyncProgressEntity> findFirstByFlowNameOrderByStartTimeDesc(String flowName);

    List<SyncProgressEntity> findByStatus(String status);

    boolean existsByFlowNameAndExecutionId(String flowName, String executionId);
}
