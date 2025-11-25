package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PropositionEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PropositionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PageResponse<PropositionEntity> findByPoliticianId(Integer politicianId, Pageable pageable) {
        String sql = """
            SELECT p.id, p.type, p.number, p.year, p.summary, p.presentation_date, p.status, 
                   p.created_at, p.updated_at
            FROM camara_deputados.proposition p
            INNER JOIN camara_deputados.politician_proposition pp ON pp.proposition_id = p.id
            WHERE pp.politician_id = :politicianId
            ORDER BY p.presentation_date DESC, p.year DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = """
            SELECT COUNT(*) FROM camara_deputados.politician_proposition 
            WHERE politician_id = :politicianId
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<PropositionEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

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
            INSERT INTO camara_deputados.proposition
                (id, type, number, year, summary, presentation_date, status, updated_at)
            VALUES
                (:id, :type, :number, :year, :summary, :presentationDate, :status::jsonb, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO UPDATE SET
                type = EXCLUDED.type,
                number = EXCLUDED.number,
                year = EXCLUDED.year,
                summary = EXCLUDED.summary,
                presentation_date = EXCLUDED.presentation_date,
                status = EXCLUDED.status,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("id", proposition.getId());
        params.put("type", proposition.getType() != null ? proposition.getType() : "");
        params.put("number", proposition.getNumber());
        params.put("year", proposition.getYear());
        params.put("summary", proposition.getSummary() != null ? proposition.getSummary() : "");
        params.put("presentationDate", proposition.getPresentationDate());

        // build status JSON from proposition fields (entity no longer has a 'status' property)
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
            INSERT INTO camara_deputados.politician_proposition (politician_id, proposition_id)
            VALUES (:politicianId, :propositionId)
            ON CONFLICT DO NOTHING;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("propositionId", propositionId);

        jdbcTemplate.update(sql, params);
    }

    private PropositionEntity mapRow(ResultSet rs) throws SQLException {
        PropositionEntity.PropositionEntityBuilder builder = PropositionEntity.builder()
                .id(rs.getInt("id"))
                .type(rs.getString("type"))
                .number(rs.getInt("number"))
                .year(rs.getInt("year"))
                .summary(rs.getString("summary"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);

        // presentation_date can be DATE or TIMESTAMP; prefer timestamp
        try {
            builder.presentationDate(rs.getTimestamp("presentation_date") != null ? rs.getTimestamp("presentation_date").toLocalDateTime() : null);
        } catch (Exception ex) {
            // fallback to date
            builder.presentationDate(rs.getDate("presentation_date") != null ? rs.getDate("presentation_date").toLocalDate().atStartOfDay() : null);
        }

        // parse status JSON into fields
        String statusJson = rs.getString("status");
        if (statusJson != null) {
            try {
                Map<String, Object> m = objectMapper.readValue(statusJson, new TypeReference<Map<String, Object>>(){});
                Object v = m.get("dataHora");
                if (v != null) {
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
                builder.statusLastReporterUri(m.get("uriUltimoRelator") != null ? m.get("uriUltimoRelator").toString() : null);
                builder.statusTramitationDescription(m.get("descricaoTramitacao") != null ? m.get("descricaoTramitacao").toString() : null);
                builder.statusTramitationTypeCode(m.get("codTipoTramitacao") != null ? m.get("codTipoTramitacao").toString() : null);
                builder.statusSituationDescription(m.get("descricaoSituacao") != null ? m.get("descricaoSituacao").toString() : null);
                builder.statusSituationCode(m.get("codSituacao") != null ? m.get("codSituacao").toString() : null);
                builder.statusDispatch(m.get("despacho") != null ? m.get("despacho").toString() : null);
                builder.statusUrl(m.get("url") != null ? m.get("url").toString() : null);
                builder.statusScope(m.get("ambito") != null ? m.get("ambito").toString() : null);
                builder.statusAppreciation(m.get("apreciacao") != null ? m.get("apreciacao").toString() : null);
            } catch (Exception ex) {
                // ignore parsing errors
            }
        }

        return builder.build();
    }
}
