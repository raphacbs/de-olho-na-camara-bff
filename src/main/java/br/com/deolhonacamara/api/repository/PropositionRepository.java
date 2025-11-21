package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PropositionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PropositionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

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
        params.put("status", proposition.getStatus());

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
        return PropositionEntity.builder()
                .id(rs.getInt("id"))
                .type(rs.getString("type"))
                .number(rs.getInt("number"))
                .year(rs.getInt("year"))
                .summary(rs.getString("summary"))
                .presentationDate(rs.getDate("presentation_date") != null ? rs.getDate("presentation_date").toLocalDate() : null)
                .status(rs.getObject("status"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }
}

