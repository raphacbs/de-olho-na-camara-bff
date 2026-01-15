package br.com.deolhonacamara.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliticianDTO {
    private Long id;
    private String name;
    private String party;
    @JsonProperty("photoUrl")
    private String photoUrl;
    private String state;
}
