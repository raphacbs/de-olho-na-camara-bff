package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropositionEntity {
    private Integer id;
    private String type;
    private Integer number;
    private Integer year;
    private String summary;
    private LocalDate presentationDate;
    private Object status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

