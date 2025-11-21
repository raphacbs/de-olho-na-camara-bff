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
public class VoteEntity {
    private String id;
    private LocalDate date;
    private String description;
    private String summary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

