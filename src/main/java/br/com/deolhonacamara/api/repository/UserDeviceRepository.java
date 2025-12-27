package br.com.deolhonacamara.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserDeviceRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void upsertDevice(UUID userId, String fcmToken) {
        String sql = """
            INSERT INTO user_device (user_id, fcm_token, updated_at)
            VALUES (:userId, :fcmToken, CURRENT_TIMESTAMP)
            ON CONFLICT (user_id, fcm_token) DO UPDATE SET
                updated_at = CURRENT_TIMESTAMP;
        """;

        jdbcTemplate.update(sql, Map.of("userId", userId, "fcmToken", fcmToken));
    }

    public void deleteDevice(UUID userId, String fcmToken) {
        String sql = """
            DELETE FROM user_device
            WHERE user_id = :userId AND fcm_token = :fcmToken;
        """;

        jdbcTemplate.update(sql, Map.of("userId", userId, "fcmToken", fcmToken));
    }
}

