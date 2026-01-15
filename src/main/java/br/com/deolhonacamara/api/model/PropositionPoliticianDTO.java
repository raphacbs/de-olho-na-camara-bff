package br.com.deolhonacamara.api.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropositionPoliticianDTO {

    private Long id;
    private String uri;
    private String type;
    private String codeType;
    private Integer number;
    private Integer year;
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
    @JsonRawValue
    private String politician;
}
