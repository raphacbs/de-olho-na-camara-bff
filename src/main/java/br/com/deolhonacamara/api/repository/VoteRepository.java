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
public class VoteRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageResponse<PoliticianVoteEntity> findByPoliticianId(Integer politicianId, Pageable pageable) {
        String sql = """
            SELECT pv.id, pv.vote_id, pv.politician_id, pv.vote_option, pv.created_at,
                   v.id as vote_id_full, v.date as vote_date, v.description as vote_description, 
                   v.summary as vote_summary
            FROM camara_deputados.politician_vote pv
            INNER JOIN camara_deputados.vote v ON v.id = pv.vote_id
            WHERE pv.politician_id = :politicianId
            ORDER BY v.date DESC, pv.created_at DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM camara_deputados.politician_vote WHERE politician_id = :politicianId";

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

    public void upsertVote(br.com.deolhonacamara.api.model.VoteEntity vote) {
        String sql = """
            INSERT INTO camara_deputados.vote
                (id, date, description, summary, updated_at)
            VALUES
                (:id, :date, :description, :summary, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO UPDATE SET
                date = EXCLUDED.date,
                description = EXCLUDED.description,
                summary = EXCLUDED.summary,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("id", vote.getId());
        params.put("date", vote.getDate());
        params.put("description", vote.getDescription() != null ? vote.getDescription() : "");
        params.put("summary", vote.getSummary() != null ? vote.getSummary() : "");

        jdbcTemplate.update(sql, params);
    }

    public void upsertPoliticianVote(PoliticianVoteEntity politicianVote) {
        String sql = """
            INSERT INTO camara_deputados.politician_vote
                (vote_id, politician_id, vote_option)
            VALUES
                (:voteId, :politicianId, :voteOption)
            ON CONFLICT DO NOTHING;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("voteId", politicianVote.getVoteId());
        params.put("politicianId", politicianVote.getPoliticianId());
        params.put("voteOption", politicianVote.getVoteOption() != null ? politicianVote.getVoteOption() : "");

        jdbcTemplate.update(sql, params);
    }

    private PoliticianVoteEntity mapRow(ResultSet rs) throws SQLException {
        var vote = br.com.deolhonacamara.api.model.VoteEntity.builder()
                .id(rs.getString("vote_id_full"))
                .date(rs.getDate("vote_date") != null ? rs.getDate("vote_date").toLocalDate() : null)
                .description(rs.getString("vote_description"))
                .summary(rs.getString("vote_summary"))
                .build();

        return PoliticianVoteEntity.builder()
                .id(rs.getInt("id"))
                .voteId(rs.getString("vote_id"))
                .politicianId(rs.getInt("politician_id"))
                .voteOption(rs.getString("vote_option"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .vote(vote)
                .build();
    }
}

