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
public class PoliticianVoteEntity {
    private Integer id;
    private String voteId;
    private Integer politicianId;
    private String voteOption;
    private LocalDateTime createdAt;
    private VoteEntity vote;
}

