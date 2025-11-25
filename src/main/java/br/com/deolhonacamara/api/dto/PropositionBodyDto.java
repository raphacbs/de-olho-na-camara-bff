package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropositionBodyDto implements Serializable {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("uri")
    private String uri;
    
    @JsonProperty("siglaTipo")
    private String type;

    @JsonProperty("codTipo")
    private String codeType;

    @JsonProperty("numero")
    private Integer number;
    
    @JsonProperty("ano")
    private Integer year;
    
    @JsonProperty("ementa")
    private String summary;
    
    @JsonProperty("dataApresentacao")
    private LocalDateTime presentationDate;

    @JsonProperty("uriOrgaoNumerador")
    private String uriOrgaoNumerador;

    @JsonProperty("statusProposicao")
    private PropositionStatusDto statusProposition;

    @JsonProperty("uriAutores")
    private String uriAutores;

    @JsonProperty("descricaoTipo")
    private String typeDescription;

    @JsonProperty("ementaDetalhada")
    private String detailedSummary;

    @JsonProperty("keywords")
    private String keywords;

    @JsonProperty("uriPropPrincipal")
    private String uriPropPrincipal;

    @JsonProperty("uriPropAnterior")
    private String uriPropAnterior;

    @JsonProperty("uriPropPosterior")
    private String uriPropPosterior;


    @JsonProperty("urlInteiroTeor")
    private String urlInteiroTeor;

    @JsonProperty("urnFinal")
    private String urnFinal;

    @JsonProperty("texto")
    private String text;

    @JsonProperty("justificativa")
    private String justification;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PropositionStatusDto {
        @JsonProperty("dataHora")
        private LocalDateTime dateTime;
        @JsonProperty("sequencia")
        private Integer sequence;
        @JsonProperty("siglaOrgao")
        private String organAcronym;
        @JsonProperty("uriOrgao")
        private String organUri;
        @JsonProperty("uriUltimoRelator")
        private String lastReporterUri;
        @JsonProperty("regime")
        private String regime;
        @JsonProperty("descricaoTramitacao")
        private String tramitationDescription;
        @JsonProperty("codTipoTramitacao")
        private String tramitationTypeCode;
        @JsonProperty("descricaoSituacao")
        private String situationDescription;
        @JsonProperty("codSituacao")
        private String situationCode;
        @JsonProperty("despacho")
        private String dispatch;
        @JsonProperty("url")
        private String url;
        @JsonProperty("ambito")
        private String scope;
        @JsonProperty("apreciacao")
        private String appreciation;
    }


}
