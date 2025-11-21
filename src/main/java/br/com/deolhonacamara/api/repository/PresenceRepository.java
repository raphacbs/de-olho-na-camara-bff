package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PresenceEntity;
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
public class PresenceRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageResponse<PresenceEntity> findByPoliticianId(Integer politicianId, Pageable pageable) {
        String sql = """
            SELECT id, politician_id, date, description, status, created_at, updated_at
            FROM camara_deputados.presence
            WHERE politician_id = :politicianId
            ORDER BY date DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM camara_deputados.presence WHERE politician_id = :politicianId";

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<PresenceEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public void upsertPresence(PresenceEntity presence) {
        String sql = """
            INSERT INTO camara_deputados.presence
                (politician_id, date, description, status, updated_at)
            VALUES
                (:politicianId, :date, :description, :status, CURRENT_TIMESTAMP)
            ON CONFLICT DO NOTHING;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", presence.getPoliticianId());
        params.put("date", presence.getDate());
        params.put("description", presence.getDescription() != null ? presence.getDescription() : "");
        params.put("status", presence.getStatus() != null ? presence.getStatus() : "");

        jdbcTemplate.update(sql, params);
    }

    private PresenceEntity mapRow(ResultSet rs) throws SQLException {
        return PresenceEntity.builder()
                .id(rs.getInt("id"))
                .politicianId(rs.getInt("politician_id"))
                .date(rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null)
                .description(rs.getString("description"))
                .status(rs.getString("status"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }
}

