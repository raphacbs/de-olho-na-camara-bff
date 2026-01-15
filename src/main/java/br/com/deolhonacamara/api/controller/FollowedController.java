package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.api.service.PoliticianService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.FollowedApi;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class FollowedController implements FollowedApi {

    private final JwtService jwtService;
    private final PoliticianService politicianService;

    @Override
    public ResponseEntity<Void> apiV1FollowedPoliticianIdDelete(String token, Integer politicianId) {
        var userId = jwtService.extractUserId(token);
        politicianService.unfollow(userId, politicianId);
        return  ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> apiV1FollowedPoliticianIdPost(String token, Integer politicianId) {
        var userId = jwtService.extractUserId(token);
        politicianService.follow(userId, politicianId);
        return  ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PoliticianResponseDTO> listFollowed(String token, Integer page, Integer size, String name, List<String> party, List<String> state) {
        var userId = jwtService.extractUserId(token);

        Map<String, Object> filters = new HashMap<>();
        if (name != null) {
            filters.put("name", name);
        }
        if (party != null && !party.isEmpty()) {
            filters.put("party", party);
        }
        if (state != null && !state.isEmpty()) {
            filters.put("state", state);
        }

        var response = politicianService.getFollowedByUser(userId, page, size, filters);
        return ResponseEntity.ok(response);
    }
}
