package br.com.deolhonacamara.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TramitationResponseDTO {
    private List<PropositionTramitationDto> data;
    private Integer total;
    private Integer page;
    private Integer totalPages;
    private Integer sizePage;
}
