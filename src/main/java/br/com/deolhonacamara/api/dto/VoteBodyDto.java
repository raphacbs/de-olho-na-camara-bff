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
public class VoteBodyDto implements Serializable {
    @JsonProperty("dataRegistroVoto")
    private LocalDateTime dataRegistroVoto;

    @JsonProperty("deputado_")
    private Deputado deputado;

    @JsonProperty("tipoVoto")
    private String tipoVoto;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Deputado implements Serializable {
        @JsonProperty("email")
        private String email;

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("idLegislatura")
        private Integer idLegislatura;

        @JsonProperty("nome")
        private String nome;

        @JsonProperty("siglaPartido")
        private String siglaPartido;

        @JsonProperty("siglaUf")
        private String siglaUf;

        @JsonProperty("uri")
        private String uri;

        @JsonProperty("uriPartido")
        private String uriPartido;

        @JsonProperty("urlFoto")
        private String urlFoto;
    }
}
