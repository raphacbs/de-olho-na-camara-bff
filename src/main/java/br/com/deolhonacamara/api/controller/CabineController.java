package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.CabineService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CabineController {

    private final CabineService cabineService;

    @GetMapping(value = "/cabine", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> page() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(new ClassPathResource("cabine/index.html"));
    }

    @GetMapping("/cabine/api/board")
    @ResponseBody
    public CabineService.Board board() {
        requireOperator();
        return cabineService.board();
    }

    @PostMapping("/cabine/api/flows/{code}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> start(@PathVariable String code) {
        requireOperator();
        cabineService.start(code);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("message", "Disparo aceito"));
    }

    private void requireOperator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !CabineService.OPERATOR_EMAIL.equalsIgnoreCase(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A Cabine aceita só o usuário de operação");
        }
    }
}
