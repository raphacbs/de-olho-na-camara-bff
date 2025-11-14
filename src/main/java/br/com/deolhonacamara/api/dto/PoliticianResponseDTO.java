package br.com.deolhonacamara.api.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoliticianResponseDTO {
    private List<PoliticianDto> data;
    private Integer total;
    private Integer page;
    private Integer totalPages;
    private Integer sizePage;
}
