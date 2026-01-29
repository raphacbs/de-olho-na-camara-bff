package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.PoliticianService;
import br.com.deolhonacamara.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.PoliticiansApi;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PoliticiansController implements PoliticiansApi {

    private final PoliticianService politicianService;
    private final UserService userService;

    @Override
    public ResponseEntity<PoliticianResponseDTO> listPoliticians(Integer page, Integer size, String name,
                                                                 List<String> party, List<String> state, Boolean isFollowed, Integer year) {

        Map<String, Object> filters = new HashMap<>();
        if (name != null) filters.put("name", name);
        if (party != null) filters.put("party", party);
        if (state != null) filters.put("state", state);
        if (isFollowed != null) filters.put("isFollowed", isFollowed);
        if (year != null) filters.put("year", year);

        UUID userId = extractUserIdFromAuth();
        return ResponseEntity.ok(politicianService.getAll(page, size, filters, userId));
    }

    @Override
    public ResponseEntity<PoliticianDto> politiciansIdGet(Integer id, Integer year) {
        UUID userId = extractUserIdFromAuth();
        return ResponseEntity.ok(politicianService.getById(id, userId, year));
    }

    private UUID extractUserIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            try {
                return userService.getUserIdByEmail(email);
            } catch (Exception e) {
                log.warn("Não foi possível obter userId do usuário autenticado: {}", email);
                return null;
            }
        }
        return null;
    }
}
