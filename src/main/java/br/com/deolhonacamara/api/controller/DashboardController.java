package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.DashboardService;
import br.com.deolhonacamara.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.DashboardApi;
import net.coelho.deolhonacamara.api.model.DashboardStatsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Year;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequiredArgsConstructor
@Log4j2
public class DashboardController implements DashboardApi {

    private final DashboardService dashboardService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<DashboardStatsDto> getDashboardStats(@RequestParam(value = "ano", required = false) Integer ano) {
        log.info("Fetching dashboard statistics");
        var token = getAuthorizationHeader();
        var userId = jwtService.extractUserId(token);
        int year = (ano == null) ? Year.now().getValue() : ano;
        var stats = dashboardService.getStats(userId, year);
        return ResponseEntity.ok(stats);
    }

    private String getAuthorizationHeader() {
        var opt = getRequest().map(r -> r.getHeader("Authorization"));
        if (opt.isPresent()) return opt.get();

        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest req = attrs.getRequest();
        return req != null ? req.getHeader("Authorization") : null;
    }
}
