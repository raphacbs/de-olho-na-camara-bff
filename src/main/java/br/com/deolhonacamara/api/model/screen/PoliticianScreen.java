package br.com.deolhonacamara.api.model.screen;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder   
public class PoliticianScreen {
    private Integer id;
    private String name;
    private String party;
    private String partyUri;
    private String state;
    private Integer legislatureId;
    private String email;
    private String uri;
    private String photoUrl;
}
