package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeputadosBodyDto implements Serializable {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("uri")
    private String uri;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("siglaPartido")
    private String siglaPartido;
    
    @JsonProperty("uriPartido")
    private String uriPartido;
    
    @JsonProperty("siglaUf")
    private String siglaUf;
    
    @JsonProperty("idLegislatura")
    private Integer idLegislatura;
    
    @JsonProperty("urlFoto")
    private String urlFoto;
    
    @JsonProperty("email")
    private String email;
}
