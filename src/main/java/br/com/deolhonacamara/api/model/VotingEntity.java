package br.com.deolhonacamara.api.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingEntity {
    private String id;
    private Integer approval;
    private LocalDate date;
    private LocalDateTime registrationDateTime;
    private LocalDateTime lastVotingOpenDateTime;
    private String lastVotingOpenDescription;
    private String description;
    private Integer eventId;
    private Integer organId;
    private String organAcronym;
    private String uri;
    private String eventUri;
    private String organUri;
    private String registeredEffects; // JSONB
    private String possibleObjects; // JSONB
    private String affectedPropositions; // JSONB
    private String lastPropositionPresentation; // JSONB
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Métodos para converter JSON para objetos
    public List<RegisteredEffect> getRegisteredEffectsAsObjects() {
        try {
            return objectMapper.readValue(registeredEffects, new TypeReference<List<RegisteredEffect>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public List<PossibleObject> getPossibleObjectsAsObjects() {
        try {
            return objectMapper.readValue(possibleObjects, new TypeReference<List<PossibleObject>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public List<AffectedProposition> getAffectedPropositionsAsObjects() {
        try {
            return objectMapper.readValue(affectedPropositions, new TypeReference<List<AffectedProposition>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    public LastPropositionPresentation getLastPropositionPresentationAsObject() {
        try {
            return objectMapper.readValue(lastPropositionPresentation, LastPropositionPresentation.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Classes internas para representar os objetos JSON
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisteredEffect {
        private String dataHoraResultado;
        private String dataHoraUltimaAberturaVotacao;
        private String dataHoraUltimaApresentacaoProposicao;
        private String descResultado;
        private String descUltimaAberturaVotacao;
        private String descUltimaApresentacaoProposicao;
        private String tituloProposicao;
        private String tituloProposicaoCitada;
        private String uriProposicao;
        private String uriProposicaoCitada;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PossibleObject {
        private Integer ano;
        private Integer codTipo;
        private String dataApresentacao;
        private String ementa;
        private Integer id;
        private Integer numero;
        private String siglaTipo;
        private String uri;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AffectedProposition {
        private Integer ano;
        private Integer codTipo;
        private String dataApresentacao;
        private String ementa;
        private Integer id;
        private Integer numero;
        private String siglaTipo;
        private String uri;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LastPropositionPresentation {
        private String dataHoraRegistro;
        private String descricao;
        private String uriProposicaoCitada;
    }
}

