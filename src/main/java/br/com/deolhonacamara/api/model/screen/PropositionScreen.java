package br.com.deolhonacamara.api.model.screen;

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
public class PropositionScreen {
    private Integer id;
    private String type;
    private String codeType;
    private Integer number;
    private Integer year;
    private String summary;
    private String detailedSummary;
    private LocalDate presentationDate;
    private String statusTramitationDescription;
    private List<PoliticianScreen> authors;
}
