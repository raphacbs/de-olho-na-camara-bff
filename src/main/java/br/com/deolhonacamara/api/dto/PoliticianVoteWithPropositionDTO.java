package br.com.deolhonacamara.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoliticianVoteWithPropositionDTO {
    private Long id;
    private Integer politicianId;
    private String vote;
    private String voteId;
    private LocalDate voteDate;
    private String votingDescription;
    private String propositionSummary;
    private Integer propositionYear;
    private String propositionDetailedSummary;
}
