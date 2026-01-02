package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.SduiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.SduiApi;
import net.coelho.deolhonacamara.api.model.SDUIResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SduiController implements SduiApi {

    private final SduiService sduiService;

    @Override
    public ResponseEntity<SDUIResponse> getHomeScreen(String authorization) {
        // TODO: Extrair userId do token JWT para personalizar conteúdo
        SDUIResponse response = sduiService.getHomeScreen();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SDUIResponse> getPoliticiansScreen(String authorization, String search, String uf, Integer page, Integer size) {
        SDUIResponse response = sduiService.getPoliticiansScreen(search, uf, page, size);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SDUIResponse> getPropositionsScreen(String authorization, String tipo, String status, String politico, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim, Integer page, Integer size) {
        // Converter parâmetros únicos para listas (suporte a múltiplos valores)
        List<String> tipos = tipo != null && !tipo.trim().isEmpty() ?
            Arrays.asList(tipo.split(",")) : Collections.emptyList();

        List<String> statuses = status != null && !status.trim().isEmpty() ?
            Arrays.asList(status.split(",")) : Collections.emptyList();

        String dataInicioStr = dataInicio != null ? dataInicio.toString() : null;
        String dataFimStr = dataFim != null ? dataFim.toString() : null;
        SDUIResponse response = sduiService.getPropositionsScreen(tipos, statuses, politico, dataInicioStr, dataFimStr, page, size);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SDUIResponse> getVotingsScreen(String authorization, String periodo, Integer page, Integer size) {
        SDUIResponse response = sduiService.getVotingsScreen(periodo, page, size);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SDUIResponse> getSettingsScreen(String authorization) {
        SDUIResponse response = sduiService.getSettingsScreen();
        return ResponseEntity.ok(response);
    }
}
