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
public class PoliticianVoteBodyDto implements Serializable {
    @JsonProperty("idVotacao")
    private String voteId;
    
    @JsonProperty("voto")
    private String voteOption;
    
    @JsonProperty("deputado")
    private DeputadoInfoDto deputy;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeputadoInfoDto implements Serializable {
        @JsonProperty("id")
        private Integer id;
    }
}

