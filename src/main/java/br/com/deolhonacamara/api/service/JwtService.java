package br.com.deolhonacamara.api.service;


import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import br.com.deolhonacamara.exception.BusinessException;
import br.com.deolhonacamara.api.BusinessCode;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Utilitário para gerenciamento de tokens JWT (JSON Web Tokens).
 * Fornece funcionalidades para geração, validação e extração de informações
 * de tokens JWT usados na autenticação da aplicação.
 *
 * @author Personal Finance Team
 * @version 1.0
 * @since 1.0
 */
@Component
@AllArgsConstructor
@Log4j2
public class JwtService {
    private final PropertiesConfig propertiesConfig;

    /**
     * Gera um token JWT para um usuário específico.
     * O token inclui o email do usuário, ID do usuário, data de emissão
     * e data de expiração configurada.
     *
     * @param user entidade do usuário para geração do token
     * @return token JWT gerado
     * @throws Exception se houver erro na geração do token
     */
    public String generateToken(UserEntity user) {
        log.debug("🔑 Gerando token JWT para usuário: {}", user.getEmail());
        try {
            long expirationMs = propertiesConfig.getJwtExpirationMs();
            Instant expirationInstant = Instant.now().plusMillis(expirationMs);
            Date expirationDate = Date.from(expirationInstant);
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("userId", user.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS512, propertiesConfig.getJwtSecret())
                    .compact();
            log.debug("✅ Token JWT gerado com sucesso para usuário: {}", user.getEmail());
            return token;
        } catch (Exception e) {
            log.error("❌ Erro ao gerar token JWT para usuário: {} - {}", user.getEmail(), e.getMessage());
            throw e;
        }
    }


    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 dias
                .signWith(SignatureAlgorithm.HS512, propertiesConfig.getJwtSecret())
                .compact();
    }

    /**
     * Extrai o username (email) de um token JWT.
     *
     * @param token token JWT para extração do username
     * @return username extraído do token
     * @throws Exception se houver erro na extração do username
     */
    public String extractUsername(String token) {
        log.debug("🔍 Extraindo username do token JWT");
        try {
            String username = Jwts.parser().setSigningKey(propertiesConfig.getJwtSecret()).parseClaimsJws(token)
                    .getBody().getSubject();
            log.debug("✅ Username extraído com sucesso: {}", username);
            return username;
        } catch (Exception e) {
            log.error("❌ Erro ao extrair username do token JWT: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Valida se um token JWT é válido para um usuário específico.
     * Verifica se o username do token corresponde ao email fornecido
     * e se o token não está expirado.
     *
     * @param token token JWT a ser validado
     * @param email email do usuário para validação
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateTokenExpired(String token, String email) {
        log.debug("🔍 Validando token JWT para usuário: {}", email);
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(email) && !isTokenExpired(token));
            log.debug("✅ Validação do token JWT concluída - Usuário: {} - Válido: {}", email, isValid);
            return isValid;
        } catch (Exception e) {
            log.error("❌ Erro ao validar token JWT para usuário: {} - {}", email, e.getMessage());
            return false;
        }
    }

    /**
     * Valida se um token JWT é válido (assinatura correta e não expirado).
     *
     * @param token token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateTokenExpired(String token) {
        log.debug("🔍 Validando formato e assinatura do token JWT");
        try {
            Jwts.parser().setSigningKey(propertiesConfig.getJwtSecret()).parseClaimsJws(token);
            log.debug("✅ Token JWT possui formato e assinatura válidos");
            return true;
        } catch (Exception e) {
            log.error("❌ Token JWT inválido - {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida se um token JWT é válido (assinatura correta, sem validar expiração).
     *
     * @param token token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateTokenWithoutExpiration(String token) {
        log.debug("🔍 Validando formato e assinatura do token JWT (sem verificar expiração)");
        try {
            Jwts.parser().setSigningKey(propertiesConfig.getJwtSecret()).parseClaimsJws(token);
            log.debug("✅ Token JWT possui formato e assinatura válidos");
            return true;
        } catch (Exception e) {
            log.error("❌ Token JWT inválido - {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida se um token JWT é válido para um usuário específico (assinatura correta, sem validar expiração).
     *
     * @param token token JWT a ser validado
     * @param email email do usuário para validação
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateTokenWithoutExpiration(String token, String email) {
        log.debug("🔍 Validando token JWT para usuário (sem verificar expiração): {}", email);
        try {
            final String username = extractUsername(token);
            boolean isValid = username.equals(email);
            log.debug("✅ Validação do token JWT concluída - Usuário: {} - Válido: {}", email, isValid);
            return isValid;
        } catch (Exception e) {
            log.error("❌ Erro ao validar token JWT para usuário: {} - {}", email, e.getMessage());
            return false;
        }
    }



    /**
     * Verifica se um token JWT está expirado.
     *
     * @param token token JWT para verificação de expiração
     * @return true se o token estiver expirado, false caso contrário
     */
    public boolean isTokenExpired(String token) {
        log.debug("⏰ Verificando expiração do token JWT");
        try {
            final Date expiration = Jwts.parser().setSigningKey(propertiesConfig.getJwtSecret()).parseClaimsJws(token)
                    .getBody().getExpiration();
            boolean isExpired = expiration.before(new Date());
            log.debug("✅ Verificação de expiração concluída - Expirado: {}", isExpired);
            return isExpired;
        } catch (Exception e) {
            log.error("❌ Erro ao verificar expiração do token JWT: {}", e.getMessage());
            return true; // Considera como expirado em caso de erro
        }
    }

    /**
     * Extrai o ID do usuário de um token JWT.
     * Remove automaticamente o prefixo "Bearer " se presente.
     *
     * @param token token JWT para extração do ID do usuário
     * @return UUID do usuário extraído do token
     * @throws Exception se houver erro na extração do ID do usuário
     */
    public UUID extractUserId(String token) {
        log.debug("🔍 Extraindo userId do token JWT");
        try {
            if (token == null || token.isBlank()) {
                log.error("❌ Token JWT vazio ou ausente ao extrair userId");
                throw new BusinessException(BusinessCode.TOKEN_NOT_FOUND_OR_EXPIRED, "Token não encontrado");
            }

            String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            Claims claims = Jwts.parser()
                    .setSigningKey(propertiesConfig.getJwtSecret())
                    .parseClaimsJws(cleanToken)
                    .getBody();

            Object userIdObj = claims.get("userId");
            if (userIdObj == null) {
                log.error("❌ userId não encontrado nas claims do token");
                throw new BusinessException(BusinessCode.INVALID_USER, "userId ausente no token JWT");
            }

            String userId = userIdObj.toString();
            UUID uuid = UUID.fromString(userId);
            log.debug("✅ UserId extraído com sucesso: {}", uuid);
            return uuid;
        } catch (Exception e) {
            log.error("❌ Erro ao extrair userId do token JWT: {}", e.getMessage());
            if (e instanceof BusinessException) throw (BusinessException) e;
            throw new BusinessException(BusinessCode.TOKEN_INVALID, e.getMessage());
        }
    }
}
