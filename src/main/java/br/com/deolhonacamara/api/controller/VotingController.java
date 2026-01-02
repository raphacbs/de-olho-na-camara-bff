package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.dto.PoliticianVoteWithPropositionResponseDTO;
import br.com.deolhonacamara.api.dto.VotingWithVotesResponseDTO;
import br.com.deolhonacamara.api.service.VotingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1")
public class VotingController {

    private final VotingService votingService;

    @GetMapping("/politicians/{id}/votes-with-proposition")
    public ResponseEntity<PoliticianVoteWithPropositionResponseDTO> getPoliticianVotesWithProposition(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        PoliticianVoteWithPropositionResponseDTO response = votingService.getPoliticianVotesWithProposition(id, pageNum, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/votings-with-votes")
    public ResponseEntity<VotingWithVotesResponseDTO> getVotingsWithVotes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        VotingWithVotesResponseDTO response = votingService.getVotingsWithVotes(pageNum, pageSize);
        return ResponseEntity.ok(response);
    }
}
