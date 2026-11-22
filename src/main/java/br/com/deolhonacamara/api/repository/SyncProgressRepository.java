package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.SyncProgressEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SyncProgressRepository extends CrudRepository<SyncProgressEntity, Long> {

    Optional<SyncProgressEntity> findByFlowNameAndExecutionId(String flowName, String executionId);

    List<SyncProgressEntity> findByFlowNameOrderByStartTimeDesc(String flowName);

    Optional<SyncProgressEntity> findFirstByFlowNameOrderByStartTimeDesc(String flowName);

    List<SyncProgressEntity> findByStatus(String status);

    boolean existsByFlowNameAndExecutionId(String flowName, String executionId);

    @Modifying
    @Query("UPDATE sync_progress SET last_tramitacao_id = NULL, last_updated = :now, updated_at = :now WHERE id = :id")
    void clearLastTramitacaoId(@Param("id") Long id, @Param("now") LocalDateTime now);
}
