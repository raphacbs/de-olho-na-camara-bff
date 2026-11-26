package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.BusinessCode;
import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.model.UserEntity;
import br.com.deolhonacamara.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private PropertiesConfig propertiesConfig;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(propertiesConfig);
    }

    @Test
    void shouldGenerateAndValidateTokenForUser() {
        when(propertiesConfig.getJwtSecret()).thenReturn("test-secret-key-for-jwt");
        when(propertiesConfig.getJwtExpirationMs()).thenReturn(60_000L);

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals(user.getEmail(), jwtService.extractUsername(token));
        assertTrue(jwtService.validateTokenExpired(token, user.getEmail()));
        assertFalse(jwtService.validateTokenExpired(token, "other@example.com"));
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void shouldExtractUserIdFromBearerToken() {
        when(propertiesConfig.getJwtSecret()).thenReturn("test-secret-key-for-jwt");
        when(propertiesConfig.getJwtExpirationMs()).thenReturn(60_000L);

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");

        String token = jwtService.generateToken(user);

        assertEquals(user.getId(), jwtService.extractUserId("Bearer " + token));
    }

    @Test
    void shouldThrowBusinessExceptionWhenTokenIsMissing() {
        BusinessException exception = assertThrows(BusinessException.class, () -> jwtService.extractUserId(" "));

        assertEquals(BusinessCode.TOKEN_NOT_FOUND_OR_EXPIRED, exception.getCode());
    }

    @Test
    void shouldWrapInvalidTokenAsBusinessException() {
        when(propertiesConfig.getJwtSecret()).thenReturn("test-secret-key-for-jwt");

        BusinessException exception = assertThrows(BusinessException.class, () -> jwtService.extractUserId("invalid-token"));

        assertEquals(BusinessCode.TOKEN_INVALID, exception.getCode());
    }
}
