package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.JwtService;
import br.com.deolhonacamara.api.service.PoliticianService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.FollowedApi;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PoliticianResponseDTO> listFollowed(String token, Integer page, Integer size) {
        var userId = jwtService.extractUserId(token);
        var response = politicianService.getFollowedByUser(userId, page, size);
        return ResponseEntity.ok(response);
    }
}
