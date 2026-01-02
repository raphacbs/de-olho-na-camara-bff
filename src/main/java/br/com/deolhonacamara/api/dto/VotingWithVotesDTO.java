package br.com.deolhonacamara.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingWithVotesDTO {
    private String id;
    private LocalDate date;
    private String description;
    private String organAcronym;
    private List<PoliticianVoteSummaryDTO> votes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PoliticianVoteSummaryDTO {
        private Integer politicianId;
        private String politicianName;
        private String voteType;
    }
}
