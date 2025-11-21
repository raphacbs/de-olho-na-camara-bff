package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.SpeechService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.SpeechesApi;
import net.coelho.deolhonacamara.api.model.SpeechResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SpeechController implements SpeechesApi {

    private final SpeechService speechService;

    @Override
    public ResponseEntity<SpeechResponseDTO> getPoliticianSpeeches(Integer id, Integer page, Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        return ResponseEntity.ok(speechService.getByPoliticianId(id, pageNum, pageSize));
    }
}

