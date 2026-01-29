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
public class PoliticianEntity {
    private Integer id;
    private String name;
    private String party;
    private String partyUri;
    private String state;
    private Integer legislatureId;
    private String email;
    private String uri;
    private String photoUrl;
    private Integer propositionsTotal;
    private Integer expenseTotal;
    private Boolean isFollowed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}