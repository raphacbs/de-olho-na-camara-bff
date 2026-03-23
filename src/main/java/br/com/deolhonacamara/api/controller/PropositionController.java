package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.model.input.InputBuilder;
import br.com.deolhonacamara.api.model.input.PropositionInput;
import br.com.deolhonacamara.api.service.PropositionService;
import br.com.deolhonacamara.api.service.PropositionTramitationService;
import br.com.deolhonacamara.api.dto.PropositionTramitationDto;
import br.com.deolhonacamara.api.dto.TramitationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.PropositionsApi;
import net.coelho.deolhonacamara.api.model.PropositionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PropositionController implements PropositionsApi {

    private final PropositionService propositionService;
    private final PropositionTramitationService propositionTramitationService;

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
    public ResponseEntity<java.util.List<net.coelho.deolhonacamara.api.model.PropositionTramitationDto>> getLastTramitation(Integer id) {
        var list = propositionTramitationService.getLatestTramitation(id, 5);
        List<net.coelho.deolhonacamara.api.model.PropositionTramitationDto> gen = list.stream().map(d -> {
            net.coelho.deolhonacamara.api.model.PropositionTramitationDto g = new net.coelho.deolhonacamara.api.model.PropositionTramitationDto();
            g.setDateTime(java.time.OffsetDateTime.of(d.getDateTime(), java.time.ZoneOffset.UTC));
            g.setSequence(d.getSequence());
            g.setOrgAcronym(d.getOrgAcronym());
            g.setOrgUri(d.getOrgUri());
            g.setLastReporterUri(d.getLastReporterUri());
            g.setRegime(d.getRegime());
            g.setTramitationDescription(d.getTramitationDescription());
            g.setTramitationTypeCode(d.getTramitationTypeCode());
            g.setSituationDescription(d.getSituationDescription());
            g.setSituationCode(d.getSituationCode());
            g.setDispatch(d.getDispatch());
            g.setUrl(d.getUrl());
            g.setScope(d.getScope());
            g.setAppreciation(d.getAppreciation());
            return g;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(gen);
    }

    @Override
    public ResponseEntity<net.coelho.deolhonacamara.api.model.TramitationResponseDTO> listTramitation(Integer id, Integer page, Integer size, LocalDate startDate, LocalDate endDate) {
        int p = page != null ? page : 0;
        int s = size != null && size > 0 ? size : 20;
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atStartOfDay() : null;

        var list = propositionTramitationService.getTramitation(id, start, end, p, s);
        Integer total = propositionTramitationService.countTramitation(id, start, end);

        net.coelho.deolhonacamara.api.model.TramitationResponseDTO resp = new net.coelho.deolhonacamara.api.model.TramitationResponseDTO();
        var genList = list.stream().map(d -> {
            net.coelho.deolhonacamara.api.model.PropositionTramitationDto g = new net.coelho.deolhonacamara.api.model.PropositionTramitationDto();
            g.setDateTime(java.time.OffsetDateTime.of(d.getDateTime(), java.time.ZoneOffset.UTC));
            g.setSequence(d.getSequence());
            g.setOrgAcronym(d.getOrgAcronym());
            g.setOrgUri(d.getOrgUri());
            g.setLastReporterUri(d.getLastReporterUri());
            g.setRegime(d.getRegime());
            g.setTramitationDescription(d.getTramitationDescription());
            g.setTramitationTypeCode(d.getTramitationTypeCode());
            g.setSituationDescription(d.getSituationDescription());
            g.setSituationCode(d.getSituationCode());
            g.setDispatch(d.getDispatch());
            g.setUrl(d.getUrl());
            g.setScope(d.getScope());
            g.setAppreciation(d.getAppreciation());
            return g;
        }).collect(Collectors.toList());

        resp.setData(genList);
        resp.setTotal(total);
        resp.setPage(p);
        resp.setSizePage(s);
        resp.setTotalPages(s > 0 ? (int) Math.ceil((double) total / s) : 0);
        return ResponseEntity.ok(resp);
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
