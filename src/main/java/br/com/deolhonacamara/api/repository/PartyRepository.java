package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PartyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PartyRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<PartyEntity> findAll() {
        String sql = """
            SELECT id, acronym, name, electoral_number, created_at
            FROM party
            ORDER BY name ASC
        """;

        return jdbcTemplate.query(sql, (rs, i) -> mapRow(rs));
    }

    private PartyEntity mapRow(ResultSet rs) throws SQLException {
        return PartyEntity.builder()
                .id(rs.getInt("id"))
                .acronym(rs.getString("acronym"))
                .name(rs.getString("name"))
                .electoralNumber(rs.getInt("electoral_number"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .build();
    }
}
