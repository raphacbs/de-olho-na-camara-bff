package br.com.deolhonacamara.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Long countTotalPropositions() {
        String sql = "SELECT COUNT(*) FROM proposition";
        return jdbcTemplate.queryForObject(sql, new java.util.HashMap<>(), Long.class);
    }

    public Long countTotalPropositions(int year) {
        String sql = "SELECT COUNT(*) FROM proposition WHERE year = :year";
        var params = java.util.Map.of("year", year);
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Long countTotalExpenses() {
        String sql = "SELECT COUNT(*) FROM politician_expense";
        return jdbcTemplate.queryForObject(sql, new java.util.HashMap<>(), Long.class);
    }

    public Long countTotalExpenses(int year) {
        String sql = "SELECT COUNT(*) FROM expenses WHERE year = :year";
        var params = java.util.Map.of("year", year);
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Long countTotalVotes() {
        String sql = "SELECT COUNT(*) FROM politician_vote";
        return jdbcTemplate.queryForObject(sql, new java.util.HashMap<>(), Long.class);
    }

    public Long countTotalPoliticians() {
        String sql = "SELECT COUNT(*) FROM politicians";
        return jdbcTemplate.queryForObject(sql, new java.util.HashMap<>(), Long.class);
    }

    public Long countTotalFollowing(UUID userId) {
        String sql = """
            SELECT COUNT(*) FROM user_followed_politicians
            WHERE user_id = :userId
            """;
        var params = java.util.Map.of("userId", userId);
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public BigDecimal sumMonthlyExpenses(int year, int month) {
        String sql = "SELECT COALESCE(SUM(net_value), 0) FROM politician_expense WHERE year = :year AND month = :month";
        var params = Map.of("year", year, "month", month);
        return jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
    }
}
