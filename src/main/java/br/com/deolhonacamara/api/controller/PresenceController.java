package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.PresenceApi;
import net.coelho.deolhonacamara.api.model.PresenceResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PresenceController implements PresenceApi {

    private final PresenceService presenceService;

    @Override
    public ResponseEntity<PresenceResponseDTO> getPoliticianPresence(Integer id, Integer page, Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        return ResponseEntity.ok(presenceService.getByPoliticianId(id, pageNum, pageSize));
    }
}

