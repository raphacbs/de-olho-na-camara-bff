package br.com.deolhonacamara.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Throwable cause = authException.getCause();
        if (cause != null && cause.getMessage().contains("Token expirado")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        } else if (cause != null && cause.getMessage().contains("Token não encontrado")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não encontrado");
        } else if (cause != null && cause.getMessage().contains("Token inválido")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Não autorizado");
        }
    }
}
