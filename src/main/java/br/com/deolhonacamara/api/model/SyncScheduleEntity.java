package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sync_schedule")
public class SyncScheduleEntity {

    @Id
    @Column("flow_name")
    private String flowName;

    @Column("cron_expression")
    private String cronExpression;

    private boolean enabled;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
