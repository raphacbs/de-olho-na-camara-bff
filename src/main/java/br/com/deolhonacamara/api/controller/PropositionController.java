package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.model.input.InputBuilder;
import br.com.deolhonacamara.api.model.input.PropositionInput;
import br.com.deolhonacamara.api.service.PropositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.PropositionsApi;
import net.coelho.deolhonacamara.api.model.PropositionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PropositionController implements PropositionsApi {

    private final PropositionService propositionService;

    @Override
    public ResponseEntity<PropositionResponseDTO> getPoliticianPropositions(Integer id,
                                                                            Integer page,
                                                                            Integer size,
                                                                            Integer year) {


        //TODO Ajustar forma de criar o input, pois da forma atual fica muito verboso
        PropositionInput input = InputBuilder
                .builder(PropositionInput.class)
                .page(page != null ? page : 0)
                .sizePage(size != null ? size : 20)
                .politicianId(id)
                .filter("year", year)
                .build();

        return ResponseEntity.ok(propositionService.getByPoliticianId(input));
    }

    @Override
    public ResponseEntity<PropositionResponseDTO> listPropositions(Integer page,
                                                                   Integer size,
                                                                   String politicianId,
                                                                   List<String> types,
                                                                   List<String> statuses,
                                                                   LocalDate startDate,
                                                                   LocalDate endDate) {

        Map<String, Object> filters = new HashMap<>();
        filters.put("page", page != null ? page : 0);
        filters.put("size", size != null ? size : 20);
        filters.put("politicianId", politicianId);
        filters.put("types", types);
        filters.put("statuses", statuses);
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);



        return ResponseEntity.ok(propositionService.getFilteredPropositions(filters));
    }
}
