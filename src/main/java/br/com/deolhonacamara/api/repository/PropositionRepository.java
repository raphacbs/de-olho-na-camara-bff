package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PropositionType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.input.Input;
import br.com.deolhonacamara.api.model.input.PropositionInput;
import br.com.deolhonacamara.api.model.screen.PoliticianScreen;
import br.com.deolhonacamara.api.model.screen.PropositionScreen;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
@RequiredArgsConstructor
public class PropositionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PageResponse<PropositionEntity> findByPoliticianId(Input<PropositionInput> input, Pageable pageable) {
        String sql = """
            SELECT p.id, p.uri, p.type, p.code_type, p.number, p.year, p.summary, p.detailed_summary,
                   p.presentation_date, p.status_date_time, p.status_last_reporter_uri, p.status_tramitation_description,
                   p.status_tramitation_type_code, p.status_situation_description, p.status_situation_code,
                   p.status_dispatch, p.status_url, p.status_scope, p.status_appreciation,
                   p.uri_orgao_numerador, p.uri_autores, p.type_description, p.keywords,
                   p.uri_prop_principal, p.uri_prop_anterior, p.uri_prop_posterior,
                   p.url_inteiro_teor, p.urn_final, p.text, p.justification,
                   p.status, p.created_at, p.updated_at, pp.politician_id as politician_id
            FROM proposition p
            INNER JOIN politician_proposition pp ON pp.proposition_id = p.id
            WHERE pp.politician_id = :politicianId
            AND p.year = COALESCE(:year, p.year)
            ORDER BY p.presentation_date DESC, p.year DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = """
            SELECT COUNT(*) FROM politician_proposition
            WHERE politician_id = :politicianId
            AND EXISTS (
                SELECT 1 FROM proposition p
                WHERE p.id = proposition_id
                AND p.year = COALESCE(:year, p.year)
            )
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", input.getPropositionId());
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());
        if (input.hasFilter("year")) {
            params.put("year", input.getFilter("year"));
        } else {
            params.put("year", LocalDate.now().getYear());
        }

        List<PropositionEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        Integer totalObj = jdbcTemplate.queryForObject(countSql, params, Integer.class);
        int total = totalObj == null ? 0 : totalObj;

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public void upsertProposition(PropositionEntity proposition) {
        String sql = """
            INSERT INTO proposition
                (id, uri, type, code_type, number, year, summary, detailed_summary,
                 presentation_date, status_date_time, status_last_reporter_uri, status_tramitation_description,
                 status_tramitation_type_code, status_situation_description, status_situation_code,
                 status_dispatch, status_url, status_scope, status_appreciation,
                 uri_orgao_numerador, uri_autores, type_description, keywords,
                 uri_prop_principal, uri_prop_anterior, uri_prop_posterior,
                 url_inteiro_teor, urn_final, text, justification,
                 status, created_at, updated_at)
            VALUES
                (:id, :uri, :type, :codeType, :number, :year, :summary, :detailedSummary,
                 :presentationDate, :statusDateTime, :statusLastReporterUri, :statusTramitationDescription,
                 :statusTramitationTypeCode, :statusSituationDescription, :statusSituationCode,
                 :statusDispatch, :statusUrl, :statusScope, :statusAppreciation,
                 :uriOrgaoNumerador, :uriAutores, :typeDescription, :keywords,
                 :uriPropPrincipal, :uriPropAnterior, :uriPropPosterior,
                 :urlInteiroTeor, :urnFinal, :text, :justification,
                 :status::jsonb, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO UPDATE SET
                uri = EXCLUDED.uri,
                type = EXCLUDED.type,
                code_type = EXCLUDED.code_type,
                number = EXCLUDED.number,
                year = EXCLUDED.year,
                summary = EXCLUDED.summary,
                detailed_summary = EXCLUDED.detailed_summary,
                presentation_date = EXCLUDED.presentation_date,
                status_date_time = EXCLUDED.status_date_time,
                status_last_reporter_uri = EXCLUDED.status_last_reporter_uri,
                status_tramitation_description = EXCLUDED.status_tramitation_description,
                status_tramitation_type_code = EXCLUDED.status_tramitation_type_code,
                status_situation_description = EXCLUDED.status_situation_description,
                status_situation_code = EXCLUDED.status_situation_code,
                status_dispatch = EXCLUDED.status_dispatch,
                status_url = EXCLUDED.status_url,
                status_scope = EXCLUDED.status_scope,
                status_appreciation = EXCLUDED.status_appreciation,
                uri_orgao_numerador = EXCLUDED.uri_orgao_numerador,
                uri_autores = EXCLUDED.uri_autores,
                type_description = EXCLUDED.type_description,
                keywords = EXCLUDED.keywords,
                uri_prop_principal = EXCLUDED.uri_prop_principal,
                uri_prop_anterior = EXCLUDED.uri_prop_anterior,
                uri_prop_posterior = EXCLUDED.uri_prop_posterior,
                url_inteiro_teor = EXCLUDED.url_inteiro_teor,
                urn_final = EXCLUDED.urn_final,
                text = EXCLUDED.text,
                justification = EXCLUDED.justification,
                status = EXCLUDED.status,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("id", proposition.getId());
        params.put("uri", proposition.getUri());
        params.put("type", proposition.getType() != null ? proposition.getType() : "");
        params.put("codeType", proposition.getCodeType());
        params.put("number", proposition.getNumber());
        params.put("year", proposition.getYear());
        params.put("summary", proposition.getSummary() != null ? proposition.getSummary() : "");
        params.put("detailedSummary", proposition.getDetailedSummary());
        params.put("presentationDate", proposition.getPresentationDate() == null ? null : Timestamp.valueOf(proposition.getPresentationDate()));
        params.put("statusDateTime", proposition.getStatusDateTime() == null ? null : Timestamp.valueOf(proposition.getStatusDateTime()));
        params.put("statusLastReporterUri", proposition.getStatusLastReporterUri());
        params.put("statusTramitationDescription", proposition.getStatusTramitationDescription());
        params.put("statusTramitationTypeCode", proposition.getStatusTramitationTypeCode());
        params.put("statusSituationDescription", proposition.getStatusSituationDescription());
        params.put("statusSituationCode", proposition.getStatusSituationCode());
        params.put("statusDispatch", proposition.getStatusDispatch());
        params.put("statusUrl", proposition.getStatusUrl());
        params.put("statusScope", proposition.getStatusScope());
        params.put("statusAppreciation", proposition.getStatusAppreciation());
        params.put("uriOrgaoNumerador", proposition.getUriOrgaoNumerador());
        params.put("uriAutores", proposition.getUriAutores());
        params.put("typeDescription", proposition.getTypeDescription());
        params.put("keywords", proposition.getKeywords());
        params.put("uriPropPrincipal", proposition.getUriPropPrincipal());
        params.put("uriPropAnterior", proposition.getUriPropAnterior());
        params.put("uriPropPosterior", proposition.getUriPropPosterior());
        params.put("urlInteiroTeor", proposition.getUrlInteiroTeor());
        params.put("urnFinal", proposition.getUrnFinal());
        params.put("text", proposition.getText());
        params.put("justification", proposition.getJustification());

        // build status JSON from proposition fields
        Map<String, Object> statusMap = new HashMap<>();
        if (proposition.getStatusDateTime() != null) statusMap.put("dataHora", proposition.getStatusDateTime().toString());
        if (proposition.getStatusLastReporterUri() != null) statusMap.put("uriUltimoRelator", proposition.getStatusLastReporterUri());
        if (proposition.getStatusTramitationDescription() != null) statusMap.put("descricaoTramitacao", proposition.getStatusTramitationDescription());
        if (proposition.getStatusTramitationTypeCode() != null) statusMap.put("codTipoTramitacao", proposition.getStatusTramitationTypeCode());
        if (proposition.getStatusSituationDescription() != null) statusMap.put("descricaoSituacao", proposition.getStatusSituationDescription());
        if (proposition.getStatusSituationCode() != null) statusMap.put("codSituacao", proposition.getStatusSituationCode());
        if (proposition.getStatusDispatch() != null) statusMap.put("despacho", proposition.getStatusDispatch());
        if (proposition.getStatusUrl() != null) statusMap.put("url", proposition.getStatusUrl());
        if (proposition.getStatusScope() != null) statusMap.put("ambito", proposition.getStatusScope());
        if (proposition.getStatusAppreciation() != null) statusMap.put("apreciacao", proposition.getStatusAppreciation());

        try {
            if (statusMap.isEmpty()) {
                params.put("status", null);
            } else {
                params.put("status", objectMapper.writeValueAsString(statusMap));
            }
        } catch (Exception ex) {
            params.put("status", null);
        }

        jdbcTemplate.update(sql, params);
    }

    public void linkPoliticianToProposition(Integer politicianId, Integer propositionId) {
        String sql = """
            INSERT INTO politician_proposition (politician_id, proposition_id)
            VALUES (:politicianId, :propositionId)
            ON CONFLICT DO NOTHING;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("propositionId", propositionId);

        jdbcTemplate.update(sql, params);
    }

    public List<PropositionEntity> findLatestPropositions(int limit) {
        String sql = """
            SELECT p.id, p.uri, p.type, p.code_type, p.number, p.year, p.summary, p.detailed_summary,
                   p.presentation_date, p.status_date_time, p.status_last_reporter_uri, p.status_tramitation_description,
                   p.status_tramitation_type_code, p.status_situation_description, p.status_situation_code,
                   p.status_dispatch, p.status_url, p.status_scope, p.status_appreciation,
                   p.uri_orgao_numerador, p.uri_autores, p.type_description, p.keywords,
                   p.uri_prop_principal, p.uri_prop_anterior, p.uri_prop_posterior,
                   p.url_inteiro_teor, p.urn_final, p.text, p.justification,
                   p.status, p.created_at, p.updated_at
            FROM proposition p
            ORDER BY p.presentation_date DESC, p.year DESC
            LIMIT :limit
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);

        return jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
    }

    public List<PropositionScreen> findLatestPropositionsScreen(int limit) {
        // Fazer a query para pegar as proposições mais recentes agrupando todos os autores de cada proposição
        // Usando CTEs para garantir ordenação consistente e agregação correta dos autores
        String sql = """
            WITH ordered_propositions AS (
                SELECT DISTINCT p.id, p.presentation_date, p.year
                FROM proposition p
                ORDER BY p.presentation_date DESC NULLS LAST, p.year DESC, p.id DESC
                LIMIT :limit
            ),
            proposition_authors AS (
                SELECT
                    pp.proposition_id,
                    json_agg(json_build_object(
                        'id', pol.id,
                        'name', pol.name,
                        'party', pol.party,
                        'photo_url', pol.photo_url,
                        'state', pol.state
                    ) ORDER BY pol.name) as authors
                FROM politician_proposition pp
                INNER JOIN politicians pol ON pol.id = pp.politician_id
                GROUP BY pp.proposition_id
            )
            SELECT p.id, p.uri, p.type, p.code_type, p.number, p.year, p.summary, p.detailed_summary,
                   p.presentation_date, p.status_date_time, p.status_last_reporter_uri, p.status_tramitation_description,
                   p.status_tramitation_type_code, p.status_situation_description, p.status_situation_code,
                   p.status_dispatch, p.status_url, p.status_scope, p.status_appreciation,
                   p.uri_orgao_numerador, p.uri_autores, p.type_description, p.keywords,
                   p.uri_prop_principal, p.uri_prop_anterior, p.uri_prop_posterior,
                   p.url_inteiro_teor, p.urn_final, p.text, p.justification,
                   p.status, p.created_at, p.updated_at,
                   COALESCE(pa.authors, '[]'::json) as authors
            FROM ordered_propositions op
            INNER JOIN proposition p ON p.id = op.id
            LEFT JOIN proposition_authors pa ON pa.proposition_id = p.id
            ORDER BY p.presentation_date DESC NULLS LAST, p.year DESC, p.id DESC
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);

        return jdbcTemplate.query(sql, params, (rs, i) -> {
            try {
                return mapRowScreen(rs);
            } catch (Exception e) {
                log.error("Error mapping row to PropositionScreen", e);
                return null;
            }
        });
    }

    public List<PropositionScreen> findFilteredPropositionsScreen(String politico, String tipo, LocalDate dataInicio, LocalDate dataFim, int limit) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.uri, p.type, p.code_type, p.number, p.year, p.summary, p.detailed_summary,
                   p.presentation_date, p.status_date_time, p.status_last_reporter_uri, p.status_tramitation_description,
                   p.status_tramitation_type_code, p.status_situation_description, p.status_situation_code,
                   p.status_dispatch, p.status_url, p.status_scope, p.status_appreciation,
                   p.uri_orgao_numerador, p.uri_autores, p.type_description, p.keywords,
                   p.uri_prop_principal, p.uri_prop_anterior, p.uri_prop_posterior,
                   p.url_inteiro_teor, p.urn_final, p.text, p.justification,
                   p.status, p.created_at, p.updated_at,
                   json_agg(json_build_object(
                       'id', pol.id,
                       'name', pol.name,
                       'party', pol.party,
                       'photo_url', pol.photo_url,
                       'state', pol.state
                   )) as authors
            FROM proposition p
            INNER JOIN politician_proposition pp ON pp.proposition_id = p.id
            INNER JOIN politicians pol ON pol.id = pp.politician_id
            WHERE 1=1
        """);

        Map<String, Object> params = new HashMap<>();

        // Filtro por político (nome)
        if (politico != null && !politico.trim().isEmpty()) {
            sql.append(" AND LOWER(pol.name) LIKE LOWER(:politico)");
            params.put("politico", "%" + politico.trim() + "%");
        }

        // Filtro por tipo (validado com enum)
        if (tipo != null && !tipo.trim().isEmpty()) {
            String tipoTrimmed = tipo.trim().toUpperCase();
            // Valida se o tipo é válido usando o enum PropositionType
            if (PropositionType.isValidCode(tipoTrimmed)) {
                sql.append(" AND UPPER(p.type) = :tipo");
                params.put("tipo", tipoTrimmed);
            } else {
                // Se o tipo não for válido, não aplica filtro (evita queries vazias)
                log.warn("Tipo de proposição inválido fornecido: {}", tipoTrimmed);
            }
        }

        // Filtro por data inicial
        if (dataInicio != null) {
            sql.append(" AND p.presentation_date >= :dataInicio");
            params.put("dataInicio", dataInicio);
        }

        // Filtro por data final
        if (dataFim != null) {
            sql.append(" AND p.presentation_date <= :dataFim");
            params.put("dataFim", dataFim);
        }

        sql.append("""
            GROUP BY p.id, p.uri, p.type, p.code_type, p.number, p.year, p.summary, p.detailed_summary,
                     p.presentation_date, p.status_date_time, p.status_last_reporter_uri, p.status_tramitation_description,
                     p.status_tramitation_type_code, p.status_situation_description, p.status_situation_code,
                     p.status_dispatch, p.status_url, p.status_scope, p.status_appreciation,
                     p.uri_orgao_numerador, p.uri_autores, p.type_description, p.keywords,
                     p.uri_prop_principal, p.uri_prop_anterior, p.uri_prop_posterior,
                     p.url_inteiro_teor, p.urn_final, p.text, p.justification,
                     p.status, p.created_at, p.updated_at
            ORDER BY p.presentation_date DESC NULLS LAST, p.year DESC, p.id DESC
            LIMIT :limit
        """);

        params.put("limit", limit);

        String finalSql = sql.toString();
        @SuppressWarnings("null")
        List<PropositionScreen> result = jdbcTemplate.query(finalSql, params, (rs, i) -> {
            try {
                return mapRowScreen(rs);
            } catch (Exception e) {
                log.error("Error mapping row to PropositionScreen", e);
                return null;
            }
        });
        return result;
    }


    private PropositionScreen mapRowScreen(ResultSet rs) throws SQLException, JsonMappingException, JsonProcessingException {
        // Processar o array JSON de autores
        String authorsJson = rs.getString("authors");
        List<PoliticianScreen> authors = new ArrayList<>();

        if (authorsJson != null && !authorsJson.equals("null")) {
            try {
                // Deserializar o array JSON de autores
                List<Map<String, Object>> authorsData = objectMapper.readValue(authorsJson,
                    new TypeReference<List<Map<String, Object>>>(){});

                for (Map<String, Object> authorData : authorsData) {
                    PoliticianScreen politicianScreen = PoliticianScreen.builder()
                            .id(((Number) authorData.get("id")).intValue())
                            .name((String) authorData.get("name"))
                            .party((String) authorData.get("party"))
                            .photoUrl((String) authorData.get("photo_url"))
                            .state((String) authorData.get("state"))
                            .build();
                    authors.add(politicianScreen);
                }
            } catch (Exception e) {
                log.warn("Erro ao processar autores da proposição {}: {}", rs.getInt("id"), e.getMessage());
                // Fallback: criar lista vazia
                authors = new ArrayList<>();
            }
        }

        PropositionScreen.PropositionScreenBuilder builder = PropositionScreen.builder()
                .id(rs.getInt("id"))
                .presentationDate(rs.getTimestamp("presentation_date") != null ? rs.getTimestamp("presentation_date").toLocalDateTime().toLocalDate() : null)
                .authors(authors)
                .type(rs.getString("type"))
                .codeType(rs.getString("code_type"))
                .number(rs.getInt("number"))
                .year(rs.getInt("year"))
                .summary(rs.getString("summary"))
                .detailedSummary(rs.getString("detailed_summary"))
                .statusTramitationDescription(rs.getString("status_tramitation_description"));

        // Parse status JSON into fields if explicit columns were null
        String statusJson = rs.getString("status");
        if (statusJson != null && builder.build().getStatusTramitationDescription() == null) {
            try {
                Map<String, Object> m = objectMapper.readValue(statusJson, new TypeReference<>(){});
                Object descricaoTramitacao = m.get("descricaoTramitacao");
                if (descricaoTramitacao != null) {
                    builder.statusTramitationDescription(descricaoTramitacao.toString());
                }
            } catch (Exception ex) {
                // ignore parsing errors
            }
        }

        return builder.build();
    }
    

    private PropositionEntity mapRow(ResultSet rs) throws SQLException {
        PropositionEntity.PropositionEntityBuilder builder = PropositionEntity.builder()
                .id(rs.getInt("id"))
                .uri(rs.getString("uri"))
                .type(rs.getString("type"))
                .codeType(rs.getString("code_type"))
                .number(rs.getInt("number"))
                .year(rs.getInt("year"))
                .summary(rs.getString("summary"))
                .detailedSummary(rs.getString("detailed_summary"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // presentation_date can be DATE or TIMESTAMP; prefer timestamp
        try {
            builder.presentationDate(rs.getTimestamp("presentation_date") != null ? rs.getTimestamp("presentation_date").toLocalDateTime() : null);
        } catch (Exception ex) {
            // fallback to date
            builder.presentationDate(rs.getDate("presentation_date") != null ? rs.getDate("presentation_date").toLocalDate().atStartOfDay() : null);
        }

        // try reading status_date_time and other status columns directly (prefer explicit columns)
        try {
            builder.statusDateTime(rs.getTimestamp("status_date_time") != null ? rs.getTimestamp("status_date_time").toLocalDateTime() : null);
        } catch (Exception ex) {
            // ignore
        }
        builder.statusLastReporterUri(rs.getString("status_last_reporter_uri"));
        builder.statusTramitationDescription(rs.getString("status_tramitation_description"));
        builder.statusTramitationTypeCode(rs.getString("status_tramitation_type_code"));
        builder.statusSituationDescription(rs.getString("status_situation_description"));
        builder.statusSituationCode(rs.getString("status_situation_code"));
        builder.statusDispatch(rs.getString("status_dispatch"));
        builder.statusUrl(rs.getString("status_url"));
        builder.statusScope(rs.getString("status_scope"));
        builder.statusAppreciation(rs.getString("status_appreciation"));

        builder.uriOrgaoNumerador(rs.getString("uri_orgao_numerador"));
        builder.uriAutores(rs.getString("uri_autores"));
        builder.typeDescription(rs.getString("type_description"));
        builder.keywords(rs.getString("keywords"));
        builder.uriPropPrincipal(rs.getString("uri_prop_principal"));
        builder.uriPropAnterior(rs.getString("uri_prop_anterior"));
        builder.uriPropPosterior(rs.getString("uri_prop_posterior"));
        builder.urlInteiroTeor(rs.getString("url_inteiro_teor"));
        builder.urnFinal(rs.getString("urn_final"));
        builder.text(rs.getString("text"));
        builder.justification(rs.getString("justification"));

        // parse status JSON into fields if explicit columns were null
        String statusJson = rs.getString("status");
        if (statusJson != null) {
            try {
                Map<String, Object> m = objectMapper.readValue(statusJson, new TypeReference<>(){});
                Object v = m.get("dataHora");
                if (v != null && builder.build().getStatusDateTime() == null) {
                    if (v instanceof String) {
                        try {
                            builder.statusDateTime(LocalDateTime.parse((String) v));
                        } catch (DateTimeParseException ex) {
                            // ignore
                        }
                    } else if (v instanceof LocalDateTime) {
                        builder.statusDateTime((LocalDateTime) v);
                    }
                }
                if (builder.build().getStatusLastReporterUri() == null) builder.statusLastReporterUri(m.get("uriUltimoRelator") != null ? m.get("uriUltimoRelator").toString() : null);
                if (builder.build().getStatusTramitationDescription() == null) builder.statusTramitationDescription(m.get("descricaoTramitacao") != null ? m.get("descricaoTramitacao").toString() : null);
                if (builder.build().getStatusTramitationTypeCode() == null) builder.statusTramitationTypeCode(m.get("codTipoTramitacao") != null ? m.get("codTipoTramitacao").toString() : null);
                if (builder.build().getStatusSituationDescription() == null) builder.statusSituationDescription(m.get("descricaoSituacao") != null ? m.get("descricaoSituacao").toString() : null);
                if (builder.build().getStatusSituationCode() == null) builder.statusSituationCode(m.get("codSituacao") != null ? m.get("codSituacao").toString() : null);
                if (builder.build().getStatusDispatch() == null) builder.statusDispatch(m.get("despacho") != null ? m.get("despacho").toString() : null);
                if (builder.build().getStatusUrl() == null) builder.statusUrl(m.get("url") != null ? m.get("url").toString() : null);
                if (builder.build().getStatusScope() == null) builder.statusScope(m.get("ambito") != null ? m.get("ambito").toString() : null);
                if (builder.build().getStatusAppreciation() == null) builder.statusAppreciation(m.get("apreciacao") != null ? m.get("apreciacao").toString() : null);
            } catch (Exception ex) {
                // ignore parsing errors
            }
        }

        return builder.build();
    }
}
