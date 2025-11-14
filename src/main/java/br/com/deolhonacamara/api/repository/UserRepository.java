package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.UserEntity;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    @Named("namedParameterJdbcTemplate")
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<UserEntity> findByEmail(String email) {
        String sql = "SELECT id, full_name, email, active, created_at, password_hash FROM authentication.users " +
                "WHERE email = :email";
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql,
                    java.util.Collections.singletonMap("email", email),
                    (rs, rowNum) -> {
                        UserEntity user = new UserEntity();
                        user.setId(UUID.fromString(rs.getString("id")));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setActive(rs.getBoolean("active"));
                        user.setPassword(rs.getString("password_hash"));
                        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        return user;
                    }));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public void save(UserEntity user) {
        final String INSERT_USER_SQL = "INSERT INTO authentication.users (id, full_name, email, active, " +
                "created_at, password_hash) VALUES (:id, :fullName, :email, :active, :createdAt, :passwordHash)";

        namedParameterJdbcTemplate.update(INSERT_USER_SQL, Map.of(
                "id", user.getId(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "active", user.isActive(),
                "passwordHash", user.getPassword(),
                "createdAt", user.getCreatedAt()
        ));
    }
}
