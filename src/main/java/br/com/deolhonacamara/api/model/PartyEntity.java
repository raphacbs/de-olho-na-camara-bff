package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyEntity {
    private Integer id;
    private String acronym;
    private String name;
    private Integer electoralNumber;
    private LocalDateTime createdAt;
}
