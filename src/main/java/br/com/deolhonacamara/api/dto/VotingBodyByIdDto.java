package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VotingBodyByIdDto implements Serializable {
    @JsonProperty("aprovacao")
    private Integer approval;

    @JsonProperty("data")
    private String date;

    @JsonProperty("dataHoraRegistro")
    private String registrationDateTime;

    @JsonProperty("dataHoraUltimaAberturaVotacao")
    private String lastVotingOpenDateTime;

    @JsonProperty("descUltimaAberturaVotacao")
    private String lastVotingOpenDescription;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("efeitosRegistrados")
    private List<RegisteredEffect> registeredEffects;

    @JsonProperty("id")
    private String id;

    @JsonProperty("idEvento")
    private Integer eventId;

    @JsonProperty("idOrgao")
    private Integer organId;

    @JsonProperty("objetosPossiveis")
    private List<PossibleObject> possibleObjects;

    @JsonProperty("proposicoesAfetadas")
    private List<AffectedProposition> affectedPropositions;

    @JsonProperty("siglaOrgao")
    private String organAcronym;

    @JsonProperty("ultimaApresentacaoProposicao")
    private LastPropositionPresentation lastPropositionPresentation;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("uriEvento")
    private String eventUri;

    @JsonProperty("uriOrgao")
    private String organUri;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RegisteredEffect implements Serializable {
        @JsonProperty("dataHoraResultado")
        private String resultDateTime;

        @JsonProperty("dataHoraUltimaAberturaVotacao")
        private String lastVotingOpenDateTime;

        @JsonProperty("dataHoraUltimaApresentacaoProposicao")
        private String lastPropositionPresentationDateTime;

        @JsonProperty("descResultado")
        private String resultDescription;

        @JsonProperty("descUltimaAberturaVotacao")
        private String lastVotingOpenDescription;

        @JsonProperty("descUltimaApresentacaoProposicao")
        private String lastPropositionPresentationDescription;

        @JsonProperty("tituloProposicao")
        private String propositionTitle;

        @JsonProperty("tituloProposicaoCitada")
        private String citedPropositionTitle;

        @JsonProperty("uriProposicao")
        private String propositionUri;

        @JsonProperty("uriProposicaoCitada")
        private String citedPropositionUri;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PossibleObject implements Serializable {
        @JsonProperty("ano")
        private Integer year;

        @JsonProperty("codTipo")
        private Integer typeCode;

        @JsonProperty("dataApresentacao")
        private String presentationDate;

        @JsonProperty("ementa")
        private String summary;

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("numero")
        private Integer number;

        @JsonProperty("siglaTipo")
        private String typeAcronym;

        @JsonProperty("uri")
        private String uri;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AffectedProposition implements Serializable {
        @JsonProperty("ano")
        private Integer year;

        @JsonProperty("codTipo")
        private Integer typeCode;

        @JsonProperty("dataApresentacao")
        private String presentationDate;

        @JsonProperty("ementa")
        private String summary;

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("numero")
        private Integer number;

        @JsonProperty("siglaTipo")
        private String typeAcronym;

        @JsonProperty("uri")
        private String uri;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LastPropositionPresentation implements Serializable {
        @JsonProperty("dataHoraRegistro")
        private String registrationDateTime;

        @JsonProperty("descricao")
        private String description;

        @JsonProperty("uriProposicaoCitada")
        private String citedPropositionUri;
    }
}
