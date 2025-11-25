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
public class PropositionListResponseBodyDto implements Serializable {
    @JsonProperty("dados")
    private List<PropositionBodyDto> data;
    @JsonProperty("links")
    private List<LinkDto> links;
}

