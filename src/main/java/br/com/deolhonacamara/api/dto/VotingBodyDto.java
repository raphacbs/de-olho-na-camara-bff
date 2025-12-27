package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VotingBodyDto implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("data")
    private LocalDate date;

    @JsonProperty("dataHoraRegistro")
    private LocalDateTime dataHoraRegistro;

    @JsonProperty("siglaOrgao")
    private String siglaOrgao;

    @JsonProperty("uriOrgao")
    private String uriOrgao;

    @JsonProperty("uriEvento")
    private String uriEvento;

    @JsonProperty("proposicaoObjeto")
    private String proposicaoObjeto;

    @JsonProperty("uriProposicaoObjeto")
    private String uriProposicaoObjeto;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("aprovacao")
    private Integer aprovacao;
}

