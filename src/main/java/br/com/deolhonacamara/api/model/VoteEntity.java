package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEntity {
    private Long id;
    private LocalDateTime voteRegistrationDate;
    private Integer politicianId;
    private String voteType;
    private String voteId;
    private String votingId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
