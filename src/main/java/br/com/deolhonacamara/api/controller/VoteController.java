package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.VotingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.VotesApi;
import net.coelho.deolhonacamara.api.model.PoliticianVoteResponseDTO;
import net.coelho.deolhonacamara.api.model.PoliticianVoteWithPropositionResponseDTO;
import net.coelho.deolhonacamara.api.model.VotingWithVotesResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class VoteController implements VotesApi {

    private final VotingService voteService;

    @Override
    public ResponseEntity<PoliticianVoteResponseDTO> getPoliticianVotes(Integer id, Integer page, Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        return ResponseEntity.ok(voteService.getByPoliticianId(id, pageNum, pageSize));
    }

    @Override
    public ResponseEntity<PoliticianVoteWithPropositionResponseDTO> getPoliticianVotesWithProposition(
            Integer id, Integer page, Integer size) {
        return ResponseEntity.ok(voteService.getPoliticianVotesWithProposition(id, page, size));
    }

    @Override
    public ResponseEntity<VotingWithVotesResponseDTO> getVotingsWithVotes(
            Integer page, Integer size, String votingId, Integer politicianId, Boolean onlyVotes) {
        return ResponseEntity.ok(voteService.getVotingsWithVotes(
                page, size, votingId, politicianId, Boolean.TRUE.equals(onlyVotes)));
    }
}
