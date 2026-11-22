package br.com.deolhonacamara.api.config;

import br.com.deolhonacamara.api.BusinessCode;
import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        log.debug("🔐 Processando autenticação JWT para URI: {}", request.getRequestURI());

        String path = request.getRequestURI();

        // Ignorar endpoints públicos
        if (path.equals("/api/v1/auth/login") || path.equals("/api/auth/refresh")
                || path.equals("/api/v1/auth/register") || path.equals("/api/users/activate")
                || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator/health")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("⚠️ Header Authorization não encontrado ou inválido");
                throw new AuthenticationException("Token não encontrado") {};
            }

            token = authHeader.substring(7);
            email = jwtService.extractUsername(token);
            log.debug("👤 Usuário extraído do token: {}", email);

        } catch (BusinessException e) {
            // já é BusinessException com código específico, só relança
            throw e;
        } catch (Exception e) {
            log.error("❌ Erro ao processar token JWT: {}", e.getMessage());
            throw new AuthenticationException("Token inválido") {};
        }


        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.validateTokenWithoutExpiration(token, email)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
                        null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("✅ Autenticação JWT bem-sucedida para usuário: {}", email);
            } else {
                log.error("❌ Token JWT inválido para usuário: {}", email);
                throw new BusinessException(BusinessCode.TOKEN_NOT_FOUND_OR_EXPIRED, "Token inválido.");
            }
        } else if (email != null) {
            log.debug("ℹ️ Usuário já autenticado: {}", email);
        }

        filterChain.doFilter(request, response);
    }
}
