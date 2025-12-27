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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("sync_progress")
public class SyncProgressEntity {

    @Id
    private Long id;

    @Column("flow_name")
    private String flowName;

    @Column("execution_id")
    private String executionId;

    @Column("status")
    private String status; // running, completed, failed

    @Column("total_pages")
    private Integer totalPages;

    @Column("current_page")
    private Integer currentPage;

    @Column("start_time")
    private LocalDateTime startTime;

    @Column("end_time")
    private LocalDateTime endTime;

    @Column("last_updated")
    private LocalDateTime lastUpdated;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isCompleted() {
        return "completed".equals(status);
    }

    public boolean isRunning() {
        return "running".equals(status);
    }

    public boolean isFailed() {
        return "failed".equals(status);
    }
}
