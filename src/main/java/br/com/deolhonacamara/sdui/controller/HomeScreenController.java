package br.com.deolhonacamara.sdui.controller;

import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.sdui.model.HomeScreenResponse;
import br.com.deolhonacamara.sdui.service.HomeScreenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

@RestController
@RequestMapping("/api/v1/sdui")
@RequiredArgsConstructor
@Log4j2
public class HomeScreenController {

    private final HomeScreenService homeScreenService;
    private final JwtService jwtService;

    @GetMapping("/home")
    public ResponseEntity<HomeScreenResponse> getHomeScreen(
            HttpServletRequest request,
            @RequestParam(value = "ano", required = false) Integer ano) {
        log.info("Fetching SDUI home screen");
        String rawToken = request.getHeader("Authorization");
        var userId = jwtService.extractUserId(rawToken);
        String cleanToken = rawToken != null ? rawToken.replace("Bearer ", "") : "";
        var userEmail = jwtService.extractUsername(cleanToken);
        int year = (ano == null) ? Year.now().getValue() : ano;
        var screen = homeScreenService.buildHomeScreen(userId, userEmail, year);
        return ResponseEntity.ok(screen);
    }
}
