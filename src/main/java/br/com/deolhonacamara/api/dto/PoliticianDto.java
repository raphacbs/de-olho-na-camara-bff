package br.com.deolhonacamara.api.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoliticianDto {
    private Integer id;
    private String name;
    private String party;
    private String partyUri;
    private String siglaUf;
    private String photoUrl;
    private String email;
    private Integer idLegislatura;
    private String uri;
}