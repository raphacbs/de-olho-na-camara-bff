package br.com.deolhonacamara.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropositionBodyDto implements Serializable {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("siglaTipo")
    private String type;
    
    @JsonProperty("numero")
    private Integer number;
    
    @JsonProperty("ano")
    private Integer year;
    
    @JsonProperty("ementa")
    private String summary;
    
    @JsonProperty("dataApresentacao")
    private LocalDate presentationDate;
    
    @JsonProperty("statusProposicao")
    private Object status;
}

