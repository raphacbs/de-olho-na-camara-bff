package br.com.deolhonacamara.api.mapper;


import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.model.*;
import net.coelho.deolhonacamara.api.model.*;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    PoliticianDto toDto(PoliticianEntity e);


    @Mappings({
            @Mapping(source = "documentDate", target = "documentDate", dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "documentValue",
                    expression = "java(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"pt\",\"BR\")).format(e.getDocumentValue()))"),
            @Mapping(target = "netValue",expression = "java(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"pt\",\"BR\")).format(e.getNetValue()))"),
            @Mapping(target = "glosaValue", expression = "java(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"pt\",\"BR\")).format(e.getGlosaValue()))")
    })
    ExpenseDto toDto(ExpenseEntity e);

    VoteDto toDto(br.com.deolhonacamara.api.model.VoteEntity e);

    PoliticianVoteDto toDto(PoliticianVoteEntity e);

    @Mappings({
            @Mapping(source = "startDateTime", target = "startDateTime", dateFormat = "dd/MM/yyyy HH:mm"),
            @Mapping(source = "endDateTime", target = "endDateTime", dateFormat = "dd/MM/yyyy HH:mm"),
            @Mapping(source = "startDateTimeEvent", target = "startDateTimeEvent", dateFormat = "dd/MM/yyyy HH:mm"),
            @Mapping(source = "endDateTimeEvent", target = "endDateTimeEvent", dateFormat = "dd/MM/yyyy HH:mm")
    })
    SpeechDto toDto(SpeechEntity e);

    SpeechEntity toDto(SpeechDto o);

    SpeechEntity toDto(Integer politicianId, SpeechBodyDto o);


    @Mappings({
            // map all fields from entity to generated DTO
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "uri", target = "uri"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "codeType", target = "codeType"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "summary", target = "summary"),
            @Mapping(source = "detailedSummary", target = "detailedSummary"),
            @Mapping(source = "presentationDate", target = "presentationDate", qualifiedByName = "localDateTimeToLocalDate"),
            @Mapping(source = "statusDateTime", target = "statusDateTime", qualifiedByName = "localDateTimeToString"),
            @Mapping(source = "statusLastReporterUri", target = "statusLastReporterUri"),
            @Mapping(source = "statusTramitationDescription", target = "statusTramitationDescription"),
            @Mapping(source = "statusTramitationTypeCode", target = "statusTramitationTypeCode"),
            @Mapping(source = "statusSituationDescription", target = "statusSituationDescription"),
            @Mapping(source = "statusSituationCode", target = "statusSituationCode"),
            @Mapping(source = "statusDispatch", target = "statusDispatch"),
            @Mapping(source = "statusUrl", target = "statusUrl"),
            @Mapping(source = "statusScope", target = "statusScope"),
            @Mapping(source = "statusAppreciation", target = "statusAppreciation"),
            @Mapping(source = "uriOrgaoNumerador", target = "uriOrgaoNumerador"),
            @Mapping(source = "uriAutores", target = "uriAutores"),
            @Mapping(source = "typeDescription", target = "typeDescription"),
            @Mapping(source = "keywords", target = "keywords"),
            @Mapping(source = "uriPropPrincipal", target = "uriPropPrincipal"),
            @Mapping(source = "uriPropAnterior", target = "uriPropAnterior"),
            @Mapping(source = "uriPropPosterior", target = "uriPropPosterior"),
            @Mapping(source = "urlInteiroTeor", target = "urlInteiroTeor"),
            @Mapping(source = "urnFinal", target = "urnFinal"),
            @Mapping(source = "text", target = "text"),
            @Mapping(source = "justification", target = "justification"),
            @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToString"),
            @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeToString"),
            // keep the status object as map as well
            @Mapping(target = "status", expression = "java(statusFromEntity(e))")
    })
    PropositionDto toDto(PropositionEntity e);

    @Mappings({
            // map from generated DTO back to entity
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "uri", target = "uri"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "codeType", target = "codeType"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "summary", target = "summary"),
            @Mapping(source = "detailedSummary", target = "detailedSummary"),
            @Mapping(source = "presentationDate", target = "presentationDate", qualifiedByName = "localDateToLocalDateTime"),
            @Mapping(source = "statusDateTime", target = "statusDateTime", qualifiedByName = "stringToLocalDateTime"),
            @Mapping(source = "statusLastReporterUri", target = "statusLastReporterUri"),
            @Mapping(source = "statusTramitationDescription", target = "statusTramitationDescription"),
            @Mapping(source = "statusTramitationTypeCode", target = "statusTramitationTypeCode"),
            @Mapping(source = "statusSituationDescription", target = "statusSituationDescription"),
            @Mapping(source = "statusSituationCode", target = "statusSituationCode"),
            @Mapping(source = "statusDispatch", target = "statusDispatch"),
            @Mapping(source = "statusUrl", target = "statusUrl"),
            @Mapping(source = "statusScope", target = "statusScope"),
            @Mapping(source = "statusAppreciation", target = "statusAppreciation"),
            @Mapping(source = "uriOrgaoNumerador", target = "uriOrgaoNumerador"),
            @Mapping(source = "uriAutores", target = "uriAutores"),
            @Mapping(source = "typeDescription", target = "typeDescription"),
            @Mapping(source = "keywords", target = "keywords"),
            @Mapping(source = "uriPropPrincipal", target = "uriPropPrincipal"),
            @Mapping(source = "uriPropAnterior", target = "uriPropAnterior"),
            @Mapping(source = "uriPropPosterior", target = "uriPropPosterior"),
            @Mapping(source = "urlInteiroTeor", target = "urlInteiroTeor"),
            @Mapping(source = "urnFinal", target = "urnFinal"),
            @Mapping(source = "text", target = "text"),
            @Mapping(source = "justification", target = "justification"),
            @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "stringToLocalDateTime"),
            @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "stringToLocalDateTime")
    })
    PropositionEntity toEntity(PropositionDto d);


    @Mappings({
            // map from generated DTO back to entity
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "uri", target = "uri"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "codeType", target = "codeType"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "summary", target = "summary"),
            @Mapping(source = "detailedSummary", target = "detailedSummary"),
            @Mapping(source = "presentationDate", target = "presentationDate", qualifiedByName = "localDateToLocalDateTime"),
            @Mapping(source = "statusProposition.dateTime", target = "statusDateTime", qualifiedByName = "stringToLocalDateTime"),
            @Mapping(source = "statusProposition.lastReporterUri", target = "statusLastReporterUri"),
            @Mapping(source = "statusProposition.tramitationDescription", target = "statusTramitationDescription"),
            @Mapping(source = "statusProposition.tramitationTypeCode", target = "statusTramitationTypeCode"),
            @Mapping(source = "statusProposition.situationDescription", target = "statusSituationDescription"),
            @Mapping(source = "statusProposition.situationCode", target = "statusSituationCode"),
            @Mapping(source = "statusProposition.dispatch", target = "statusDispatch"),
            @Mapping(source = "statusProposition.url", target = "statusUrl"),
            @Mapping(source = "statusProposition.scope", target = "statusScope"),
            @Mapping(source = "statusProposition.appreciation", target = "statusAppreciation"),
            @Mapping(source = "uriOrgaoNumerador", target = "uriOrgaoNumerador"),
            @Mapping(source = "uriAutores", target = "uriAutores"),
            @Mapping(source = "typeDescription", target = "typeDescription"),
            @Mapping(source = "keywords", target = "keywords"),
            @Mapping(source = "uriPropPrincipal", target = "uriPropPrincipal"),
            @Mapping(source = "uriPropAnterior", target = "uriPropAnterior"),
            @Mapping(source = "uriPropPosterior", target = "uriPropPosterior"),
            @Mapping(source = "urlInteiroTeor", target = "urlInteiroTeor"),
            @Mapping(source = "urnFinal", target = "urnFinal"),
            @Mapping(source = "text", target = "text"),
            @Mapping(source = "justification", target = "justification"),
            @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "updatedAt",  expression = "java(java.time.LocalDateTime.now())")
    })
    PropositionEntity toEntity(PropositionBodyDto d);

    PresenceDto toDto(PresenceEntity e);

    // Map from API body DTO to entity (complete mapping)
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "uri", target = "uri"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "codeType", target = "codeType"),
            @Mapping(source = "number", target = "number"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "summary", target = "summary"),
            @Mapping(source = "presentationDate", target = "presentationDate"),
            @Mapping(source = "uriOrgaoNumerador", target = "uriOrgaoNumerador"),
            @Mapping(source = "uriAutores", target = "uriAutores"),
            @Mapping(source = "typeDescription", target = "typeDescription"),
            @Mapping(source = "detailedSummary", target = "detailedSummary"),
            @Mapping(source = "keywords", target = "keywords"),
            @Mapping(source = "uriPropPrincipal", target = "uriPropPrincipal"),
            @Mapping(source = "uriPropAnterior", target = "uriPropAnterior"),
            @Mapping(source = "uriPropPosterior", target = "uriPropPosterior"),
            @Mapping(source = "urlInteiroTeor", target = "urlInteiroTeor"),
            @Mapping(source = "urnFinal", target = "urnFinal"),
            @Mapping(source = "text", target = "text"),
            @Mapping(source = "justification", target = "justification"),
            // statusProposicao nested mapping
            @Mapping(source = "statusProposicao.dateTime", target = "statusDateTime"),
            @Mapping(source = "statusProposicao.lastReporterUri", target = "statusLastReporterUri"),
            @Mapping(source = "statusProposicao.tramitationDescription", target = "statusTramitationDescription"),
            @Mapping(source = "statusProposicao.tramitationTypeCode", target = "statusTramitationTypeCode"),
            @Mapping(source = "statusProposicao.situationDescription", target = "statusSituationDescription"),
            @Mapping(source = "statusProposicao.situationCode", target = "statusSituationCode"),
            @Mapping(source = "statusProposicao.dispatch", target = "statusDispatch"),
            @Mapping(source = "statusProposicao.url", target = "statusUrl"),
            @Mapping(source = "statusProposicao.scope", target = "statusScope"),
            @Mapping(source = "statusProposicao.appreciation", target = "statusAppreciation")
    })
    PropositionEntity fromBody(PropositionBodyDto b);


    // helper methods to convert status Object <-> Map
    @Named("statusFromEntity")
    default Object statusFromEntity(PropositionEntity e) {
        if (e == null) return null;
        Map<String, Object> m = new HashMap<>();
        if (e.getStatusDateTime() != null) m.put("dataHora", e.getStatusDateTime().toString());
        if (e.getStatusLastReporterUri() != null) m.put("uriUltimoRelator", e.getStatusLastReporterUri());
        if (e.getStatusTramitationDescription() != null) m.put("descricaoTramitacao", e.getStatusTramitationDescription());
        if (e.getStatusTramitationTypeCode() != null) m.put("codTipoTramitacao", e.getStatusTramitationTypeCode());
        if (e.getStatusSituationDescription() != null) m.put("descricaoSituacao", e.getStatusSituationDescription());
        if (e.getStatusSituationCode() != null) m.put("codSituacao", e.getStatusSituationCode());
        if (e.getStatusDispatch() != null) m.put("despacho", e.getStatusDispatch());
        if (e.getStatusUrl() != null) m.put("url", e.getStatusUrl());
        if (e.getStatusScope() != null) m.put("ambito", e.getStatusScope());
        if (e.getStatusAppreciation() != null) m.put("apreciacao", e.getStatusAppreciation());
        if (m.isEmpty()) return null;
        return m;
    }

    @Named("extractStatusDateTime")
    default LocalDateTime extractStatusDateTime(Object status) {
        if (status == null) return null;
        if (status instanceof Map) {
            Object v = ((Map<?, ?>) status).get("dataHora");
            if (v == null) return null;
            if (v instanceof LocalDateTime) return (LocalDateTime) v;
            if (v instanceof String) {
                try {
                    return LocalDateTime.parse((String) v);
                } catch (DateTimeParseException ex) {
                    return null;
                }
            }
        }
        return null;
    }

    @Named("extractStatusStringUriUltimoRelator")
    default String extractStatusStringUriUltimoRelator(Object status) {
        return extractStatusString(status, "uriUltimoRelator");
    }

    @Named("extractStatusStringDescricaoTramitacao")
    default String extractStatusStringDescricaoTramitacao(Object status) {
        return extractStatusString(status, "descricaoTramitacao");
    }

    @Named("extractStatusStringCodTipoTramitacao")
    default String extractStatusStringCodTipoTramitacao(Object status) {
        return extractStatusString(status, "codTipoTramitacao");
    }

    @Named("extractStatusStringDescricaoSituacao")
    default String extractStatusStringDescricaoSituacao(Object status) {
        return extractStatusString(status, "descricaoSituacao");
    }

    @Named("extractStatusStringCodSituacao")
    default String extractStatusStringCodSituacao(Object status) {
        return extractStatusString(status, "codSituacao");
    }

    @Named("extractStatusStringDespacho")
    default String extractStatusStringDespacho(Object status) {
        return extractStatusString(status, "despacho");
    }

    @Named("extractStatusStringUrl")
    default String extractStatusStringUrl(Object status) {
        return extractStatusString(status, "url");
    }

    @Named("extractStatusStringAmbito")
    default String extractStatusStringAmbito(Object status) {
        return extractStatusString(status, "ambito");
    }

    @Named("extractStatusStringApreciacao")
    default String extractStatusStringApreciacao(Object status) {
        return extractStatusString(status, "apreciacao");
    }

    default String extractStatusString(Object status, String key) {
        if (status == null) return null;
        if (status instanceof Map) {
            Object v = ((Map<?, ?>) status).get(key);
            return v == null ? null : v.toString();
        }
        return null;
    }

    @Named("localDateTimeToLocalDate")
    default java.time.LocalDate localDateTimeToLocalDate(LocalDateTime dt) {
        return dt == null ? null : dt.toLocalDate();
    }

    @Named("localDateToLocalDateTime")
    default LocalDateTime localDateToLocalDateTime(java.time.LocalDate d) {
        return d == null ? null : d.atStartOfDay();
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dt) {
        return dt == null ? null : dt.toString();
    }

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String s) {
        if (s == null) return null;
        try {
            return LocalDateTime.parse(s);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

}
