package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PoliticianRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void upsertPolitician(PoliticianEntity politician) {
        String sql = """
            INSERT INTO politicians
                (id, name, party, party_uri, state, legislature_id, email, uri, photo_url, updated_at)
            VALUES
                (:id, :name, :party, :partyUri, :state, :legislatureId, :email, :uri, :photoUrl, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO UPDATE SET
                name = EXCLUDED.name,
                party = EXCLUDED.party,
                party_uri = EXCLUDED.party_uri,
                state = EXCLUDED.state,
                legislature_id = EXCLUDED.legislature_id,
                email = EXCLUDED.email,
                uri = EXCLUDED.uri,
                photo_url = EXCLUDED.photo_url,
                updated_at = CURRENT_TIMESTAMP;
        """;

        var params = Map.of(
                "id", politician.getId(),
                "name", politician.getName(),
                "party", politician.getParty(),
                "partyUri", politician.getPartyUri(),
                "state", politician.getState(),
                "legislatureId", politician.getLegislatureId(),
                "email", politician.getEmail(),
                "uri", politician.getUri(),
                "photoUrl", politician.getPhotoUrl()
        );

        jdbcTemplate.update(sql, params);
    }

    public PageResponse<PoliticianEntity> findAll(Pageable pageable, Map<String, Object> filters) {

        StringBuilder where = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        // ---- Filtros dinâmicos ----
        if (filters.containsKey("name")) {
            where.append(where.isEmpty() ? " WHERE " : " AND ");
            where.append(" name ILIKE :name ");
            params.put("name", "%" + filters.get("name") + "%");
        }

        if (filters.containsKey("party")) {
            where.append(where.isEmpty() ? " WHERE " : " AND ");
            where.append(" party = :party ");
            params.put("party", filters.get("party"));
        }

        if (filters.containsKey("state")) {
            where.append(where.isEmpty() ? " WHERE " : " AND ");
            where.append(" state = :state ");
            params.put("state", filters.get("state"));
        }

        // ---- SQL de conteúdo ----
        String sql = """
        SELECT id, name, party, party_uri, state, legislature_id, email, uri, photo_url, created_at, updated_at
        FROM politicians
    """ + where +
                """
                    ORDER BY name ASC
                    LIMIT :limit OFFSET :offset
                """;

        // ---- SQL para contagem ----
        String countSql = "SELECT COUNT(*) FROM politicians " + where;

        // ---- Paginação ----
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        // ---- Execução ----
        List<PoliticianEntity> content =
                jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));

        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }


    public PageResponse<PoliticianEntity> findFollowedByUser(UUID userId, Pageable pageable) {
        String sql = """
            SELECT p.id, p.name, p.party, p.party_uri, p.state, p.legislature_id, p.email, p.uri, p.photo_url,
             p.updated_at, p.created_at
            FROM politicians p
            INNER JOIN user_followed_politicians ufp ON ufp.politician_id = p.id
            WHERE ufp.user_id = :userId
            ORDER BY p.name ASC
            LIMIT :limit OFFSET :offset
        """;
        String countSql = "SELECT COUNT(*) FROM user_followed_politicians WHERE user_id = :userId";

        var params = Map.of("userId", userId, "limit", pageable.getPageSize(), "offset", pageable.getOffset());

        List<PoliticianEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = Objects.requireNonNull(jdbcTemplate.queryForObject(countSql, params, Integer.class));

        return new PageResponse<>(content, total, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    }

    public void followPolitician(UUID userId, int politicianId) {
        String sql = """
            INSERT INTO user_followed_politicians (user_id, politician_id)
            VALUES (:userId, :politicianId)
            ON CONFLICT DO NOTHING;
        """;
        jdbcTemplate.update(sql, Map.of("userId", userId, "politicianId", politicianId));
    }

    public void unfollowPolitician(UUID userId, int politicianId) {
        String sql = """
            DELETE FROM user_followed_politicians
            WHERE user_id = :userId AND politician_id = :politicianId;
        """;
        jdbcTemplate.update(sql, Map.of("userId", userId, "politicianId", politicianId));
    }

    private PoliticianEntity mapRow(ResultSet rs) throws SQLException {
        return PoliticianEntity.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .party(rs.getString("party"))
                .partyUri(rs.getString("party_uri"))
                .state(rs.getString("state"))
                .legislatureId(rs.getInt("legislature_id"))
                .email(rs.getString("email"))
                .uri(rs.getString("uri"))
                .photoUrl(rs.getString("photo_url"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }
}
