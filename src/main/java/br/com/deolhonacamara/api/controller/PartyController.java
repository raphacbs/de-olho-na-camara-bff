package br.com.deolhonacamara.api.controller;


import br.com.deolhonacamara.api.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import net.coelho.deolhonacamara.api.PartiesApi;
import net.coelho.deolhonacamara.api.model.PartyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PartyController implements PartiesApi {

    private final PartyService partyService;

    @Override
    public ResponseEntity<List<PartyDto>> listParties() {
        return ResponseEntity.ok(partyService.getAll());
    }
}
