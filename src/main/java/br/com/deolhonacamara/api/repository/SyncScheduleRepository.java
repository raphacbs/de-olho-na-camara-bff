package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.SyncScheduleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncScheduleRepository extends CrudRepository<SyncScheduleEntity, String> {
}
