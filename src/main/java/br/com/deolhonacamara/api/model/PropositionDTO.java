package br.com.deolhonacamara.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropositionDTO {
    private Long id;
    private String uri;
    private String type;
    private String codeType;
    private int number;
    private int year;
    private String summary;
    private String detailedSummary;
    private LocalDate presentationDate;
    private LocalDateTime statusDateTime;
    private String statusLastReporterUri;
    private String statusTramitationDescription;
    private String statusTramitationTypeCode;
    private String statusSituationDescription;
    private String statusSituationCode;
    private String statusDispatch;
    private String statusUrl;
    private String statusScope;
    private String statusAppreciation;
    private String uriOrgaoNumerador;
    private String uriAutores;
    private String typeDescription;
    private String keywords;
    private String uriPropPrincipal;
    private String uriPropAnterior;
    private String uriPropPosterior;
    private String urlInteiroTeor;
    private String urnFinal;
    private String text;
    private String justification;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonRawValue
    private String status;
    private List<PoliticianDTO> politicians;
}
