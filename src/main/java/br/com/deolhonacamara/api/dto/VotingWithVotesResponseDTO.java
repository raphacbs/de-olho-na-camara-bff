package br.com.deolhonacamara.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingWithVotesResponseDTO {
    private List<VotingWithVotesDTO> data;
    private Integer total;
    private Integer page;
    private Integer totalPages;
    private Integer sizePage;
}
