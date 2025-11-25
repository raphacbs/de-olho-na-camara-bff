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
public class PropositionEntity {
    private Integer id;
    private String uri;
    private String type;
    private String codeType;
    private Integer number;
    private Integer year;
    private String summary;
    private LocalDateTime presentationDate;
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
    private String detailedSummary;
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
}
