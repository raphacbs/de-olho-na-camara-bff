package br.com.deolhonacamara.sdui.controller;

import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.sdui.service.ExpensesScreenService;
import br.com.deolhonacamara.sdui.service.HomeScreenService;
import br.com.deolhonacamara.sdui.service.PoliticiansScreenService;
import br.com.deolhonacamara.sdui.service.PropositionsScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.SduiApi;
import net.coelho.deolhonacamara.api.model.HomeScreenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j2
public class HomeScreenController implements SduiApi {

    private final HomeScreenService homeScreenService;
    private final JwtService jwtService;
    private final PoliticiansScreenService politiciansScreenService;
    private final PropositionsScreenService propositionsScreenService;
    private final ExpensesScreenService expensesScreenService;

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

    @Override
    public ResponseEntity<HomeScreenResponse> getSduiPoliticiansScreen(
            String name, String party, String state, Boolean isFollowed,
            Integer year, Integer page, Integer size) {
        log.info("Fetching SDUI politicians screen");
        var request = getRequest();
        var rawToken = request.map(r -> r.getHeader("Authorization")).orElse(null);
        UUID userId = jwtService.extractUserId(rawToken);

        Map<String, Object> filters = new HashMap<>();
        if (name != null && !name.isBlank()) filters.put("name", name);
        if (party != null && !party.isBlank()) filters.put("party", party);
        if (state != null && !state.isBlank()) filters.put("state", state);
        if (isFollowed != null) filters.put("isFollowed", isFollowed);
        if (year != null) filters.put("year", year);

        int p = page != null ? page : 0;
        int s = size != null ? size : 20;

        var screen = politiciansScreenService.buildPoliticiansScreen(userId, filters, p, s);
        return ResponseEntity.ok(screen);
    }

    @Override
    public ResponseEntity<HomeScreenResponse> getSduiPropositionsScreen(
            Integer politicianId, List<String> types, List<String> statuses,
            LocalDate startDate, LocalDate endDate, Integer page, Integer size) {
        log.info("Fetching SDUI propositions screen");
        int p = page != null ? page : 0;
        int s = size != null ? size : 20;
        var screen = propositionsScreenService.buildPropositionsScreen(
                politicianId, types, statuses, startDate, endDate, p, s);
        return ResponseEntity.ok(screen);
    }

    @Override
    public ResponseEntity<HomeScreenResponse> getSduiPropositionDetailScreen(Integer id) {
        log.info("Fetching SDUI proposition detail screen for id {}", id);
        var screen = propositionsScreenService.buildPropositionDetailScreen(id);
        return ResponseEntity.ok(screen);
    }

    @Override
    public ResponseEntity<HomeScreenResponse> getSduiPoliticianExpensesScreen(
            Integer id, Integer year, Integer month, Integer page, Integer size) {
        log.info("Fetching SDUI expenses screen for politician {}", id);
        int p = page != null ? page : 0;
        int s = size != null ? size : 20;
        var screen = expensesScreenService.buildExpensesScreen(id, year, month, p, s);
        return ResponseEntity.ok(screen);
    }
}
