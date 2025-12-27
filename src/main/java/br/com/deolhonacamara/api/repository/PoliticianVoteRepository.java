package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
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
public class PoliticianVoteRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageResponse<PoliticianVoteEntity> findByPoliticianId(Integer politicianId, Pageable pageable) {
        String sql = """
            SELECT pv.id, pv.vote_registration_date, pv.politician_id, pv.vote_type, pv.vote_id, pv.created_at, pv.updated_at,
                   v.id as vote_id_full, v.date as vote_date, v.description as vote_description,
                   v.approval as vote_approval, v.registration_date_time as vote_registration_date_time
            FROM politician_vote pv
            INNER JOIN voting v ON v.id = pv.vote_id
            WHERE pv.politician_id = :politicianId
            ORDER BY v.date DESC, pv.created_at DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM politician_vote WHERE politician_id = :politicianId";

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<PoliticianVoteEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public PageResponse<PoliticianVoteEntity> findByVoteId(String voteId, Pageable pageable) {
        String sql = """
            SELECT pv.id, pv.vote_registration_date, pv.politician_id, pv.vote_type, pv.vote_id, pv.created_at, pv.updated_at,
                   v.id as vote_id_full, v.date as vote_date, v.description as vote_description,
                   v.approval as vote_approval, v.registration_date_time as vote_registration_date_time
            FROM politician_vote pv
            INNER JOIN voting v ON v.id = pv.vote_id
            WHERE pv.vote_id = :voteId
            ORDER BY pv.created_at DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM politician_vote WHERE vote_id = :voteId";

        Map<String, Object> params = new HashMap<>();
        params.put("voteId", voteId);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<PoliticianVoteEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public void upsertPoliticianVote(PoliticianVoteEntity politicianVote) {
        String sql = """
            INSERT INTO politician_vote
                (vote_registration_date, politician_id, vote_type, vote_id, updated_at)
            VALUES
                (:voteRegistrationDate, :politicianId, :voteType, :voteId, CURRENT_TIMESTAMP)
            ON CONFLICT (politician_id, vote_id) DO UPDATE SET
                vote_registration_date = EXCLUDED.vote_registration_date,
                vote_type = EXCLUDED.vote_type,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("voteRegistrationDate", politicianVote.getVoteRegistrationDate());
        params.put("politicianId", politicianVote.getPoliticianId());
        params.put("voteType", politicianVote.getVoteType());
        params.put("voteId", politicianVote.getVoteId());

        jdbcTemplate.update(sql, params);
    }

    public void deleteByVoteId(String voteId) {
        String sql = "DELETE FROM politician_vote WHERE vote_id = :voteId";

        Map<String, Object> params = new HashMap<>();
        params.put("voteId", voteId);

        jdbcTemplate.update(sql, params);
    }

    private PoliticianVoteEntity mapRow(ResultSet rs) throws SQLException {
        return PoliticianVoteEntity.builder()
                .id(rs.getLong("id"))
                .voteRegistrationDate(rs.getTimestamp("vote_registration_date") != null ?
                    rs.getTimestamp("vote_registration_date").toLocalDateTime() : null)
                .politicianId(rs.getInt("politician_id"))
                .voteType(rs.getString("vote_type"))
                .voteId(rs.getString("vote_id"))
                .createdAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ?
                    rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }
}
