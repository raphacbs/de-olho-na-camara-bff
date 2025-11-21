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
public class PresenceBodyDto implements Serializable {
    @JsonProperty("data")
    private LocalDate date;
    
    @JsonProperty("descricao")
    private String description;
    
    @JsonProperty("situacao")
    private String status;
}

