package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.PoliticianService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.PoliticiansApi;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PoliticiansController implements PoliticiansApi {

    private final PoliticianService politicianService;

    @Override
    public ResponseEntity<PoliticianResponseDTO> listPoliticians(Integer page, Integer size, String name,
                                                                 List<String> party, List<String> state) {

        Map<String, Object> filters = new HashMap<>();
        if (name != null) filters.put("name", name);
        if (party != null) filters.put("party", party);
        if (state != null) filters.put("state", state);

        return ResponseEntity.ok(politicianService.getAll(page, size, filters ));
    }

    @Override
    public ResponseEntity<PoliticianDto> politiciansIdGet(Integer id) {
        return ResponseEntity.ok(politicianService.getById(id));
    }
}
