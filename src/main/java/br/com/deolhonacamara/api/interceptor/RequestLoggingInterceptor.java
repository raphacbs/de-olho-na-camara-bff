package br.com.deolhonacamara.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Interceptor para logging de todas as requisições HTTP.
 * Registra o início e fim de cada requisição com caracteres especiais para
 * destacar.
 */
@Component
@Log4j2
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime startTime = LocalDateTime.now();

        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        String remoteAddr = getClientIpAddress(request);

        log.info("################################################################################");
        log.info("🚀 INÍCIO DA REQUISIÇÃO - ID: {} - {} {}", requestId, method, uri);
        if (queryString != null) {
            log.info("📝 Query String: {}", queryString);
        }
        log.info("🌐 IP Cliente: {}", remoteAddr);
        log.info("📱 User-Agent: {}", userAgent);
        log.info("⏰ Timestamp: {}", startTime);
        log.info("################################################################################");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Log executado após o handler, mas antes da view ser renderizada
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
        int status = response.getStatus();

        log.info("📊 REQUISIÇÃO PROCESSADA - ID: {} - Status: {}", requestId, status);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
        LocalDateTime startTime = (LocalDateTime) request.getAttribute(START_TIME_ATTRIBUTE);
        LocalDateTime endTime = LocalDateTime.now();

        long durationMs = ChronoUnit.MILLIS.between(startTime, endTime);
        int status = response.getStatus();

        if (ex != null) {
            log.error("❌ FIM DA REQUISIÇÃO COM ERRO - ID: {} - Status: {} - Duração: {}ms - Erro: {}",
                    requestId, status, durationMs, ex.getMessage());
        } else {
            log.info("✅ FIM DA REQUISIÇÃO - ID: {} - Status: {} - Duração: {}ms",
                    requestId, status, durationMs);
        }

        log.info("################################################################################");

        // Log de performance para requisições lentas
        if (durationMs > 1000) {
            log.warn("⚠️ REQUISIÇÃO LENTA DETECTADA - ID: {} - Duração: {}ms - {} {}",
                    requestId, durationMs, request.getMethod(), request.getRequestURI());
        }
    }

    /**
     * Obtém o endereço IP real do cliente, considerando proxies
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}

