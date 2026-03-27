package br.com.deolhonacamara.sdui.controller;

import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.sdui.service.HomeScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.SduiApi;
import net.coelho.deolhonacamara.api.model.HomeScreenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

@RestController
@RequiredArgsConstructor
@Log4j2
public class HomeScreenController implements SduiApi {

    private final HomeScreenService homeScreenService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<HomeScreenResponse> getHomeScreen(Integer ano) {
        log.info("Fetching SDUI home screen");
        var request = getRequest();
        var rawToken = request.map(r -> r.getHeader("Authorization")).orElse(null);
        var userId = jwtService.extractUserId(rawToken);
        var cleanToken = rawToken != null ? rawToken.replace("Bearer ", "") : "";
        var userEmail = jwtService.extractUsername(cleanToken);
        int year = (ano == null) ? Year.now().getValue() : ano;
        var screen = homeScreenService.buildHomeScreen(userId, userEmail, year);
        return ResponseEntity.ok(screen);
    }
}
