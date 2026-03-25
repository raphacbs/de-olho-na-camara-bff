package br.com.deolhonacamara.api.service;


import br.com.deolhonacamara.api.BusinessCode;
import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.model.UserEntity;
import br.com.deolhonacamara.api.repository.UserRepository;
import br.com.deolhonacamara.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.model.AuthResponseDTO;
import net.coelho.deolhonacamara.api.model.RequestLogin;
import net.coelho.deolhonacamara.api.model.RequestNewUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static br.com.deolhonacamara.api.BusinessCode.INACTIVE_USER;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private static final Duration MIN_TOKEN_EXPIRATION = Duration.ofSeconds(1);
    private static final Duration MAX_TOKEN_EXPIRATION = Duration.ofDays(365);
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtUtil;
    private final UserRepository userRepository;
    private final PropertiesConfig propertiesConfig;

    public AuthResponseDTO doLogin(RequestLogin login) {

        var user = this.userRepository.findByEmail(login.getEmail()).orElseThrow(
                () -> new BusinessException(BusinessCode.INVALID_EMAIL, "Email inválido")
        );

        validateUserCredentials(user, login.getPassword());
        validateUserActive(user);

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        log.info("✅ Tokens gerado com sucesso para o usuário: {} - ID: {}", user.getEmail(), user.getId());

        Expiration expiration = resolveExpiration();

        return new AuthResponseDTO()
                .accessToken(accessToken)
                .expireIn(expiration.expireInSecondsAsInt())
                .expireAt(expiration.expireAt())
                .tokenType("JWT")
                .refreshToken(refreshToken);

    }

    @Transactional
    public AuthResponseDTO createUser(RequestNewUser requestNewUser){
        this.userRepository.findByEmail(requestNewUser.getEmail())
                .ifPresent(user -> {
                    throw new BusinessException(BusinessCode.USER_ALREADY_EXISTS, "Email já cadastrado");
                });
        UserEntity newUser = new UserEntity();
        newUser.setId(UUID.randomUUID());
        newUser.setEmail(requestNewUser.getEmail());
        newUser.setFullName(requestNewUser.getFullName());
        newUser.setPassword(passwordEncoder.encode(requestNewUser.getPassword()));
        newUser.setActive(true);
        newUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(newUser);

        log.info("✅ Novo usuário criado com sucesso: {} - ID: {}", newUser.getEmail(), newUser.getId());

        String accessToken = jwtUtil.generateToken(newUser);
        String refreshToken = jwtUtil.generateRefreshToken(newUser);

        log.info("✅ Tokens gerado com sucesso para o usuário: {} - ID: {}", newUser.getEmail(), newUser.getId());

        Expiration expiration = resolveExpiration();

        return new AuthResponseDTO()
                .accessToken(accessToken)
                .expireIn(expiration.expireInSecondsAsInt())
                .expireAt(expiration.expireAt())
                .tokenType("JWT")
                .refreshToken(refreshToken);

    }

    private void validateUserCredentials(UserEntity user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(INACTIVE_USER, "Senha inválida");
        }
    }

    private void validateUserActive(UserEntity user) {
        if (!user.isActive()) {
            throw new BusinessException("Usuário inativo", "Usuário não está ativo");
        }
    }

    public UUID getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserEntity::getId)
                .orElse(null);
    }

    private Expiration resolveExpiration() {
        long configuredMs = propertiesConfig.getJwtExpirationMs();
        Duration expirationDuration = Duration.ofMillis(configuredMs);
        if (expirationDuration.compareTo(MIN_TOKEN_EXPIRATION) < 0 || expirationDuration.compareTo(MAX_TOKEN_EXPIRATION) > 0) {
            throw new IllegalArgumentException("Configured JWT expiration (" + configuredMs + " ms) must be between 1 second and 365 days.");
        }
        long expireInSeconds = expirationDuration.getSeconds();
        OffsetDateTime expireAt = OffsetDateTime.now(ZoneOffset.UTC).plus(expirationDuration);
        return new Expiration(expireAt, expireInSeconds);
    }

    private record Expiration(OffsetDateTime expireAt, long expireInSeconds) {
        int expireInSecondsAsInt() {
            // Validation in resolveExpiration caps this at 31,536,000 seconds (365 days), which fits in int.
            return (int) expireInSeconds;
        }
    }

}
