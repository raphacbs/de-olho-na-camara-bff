package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.dto.PoliticianVoteWithPropositionResponseDTO;
import br.com.deolhonacamara.api.dto.VotingWithVotesResponseDTO;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
import br.com.deolhonacamara.api.repository.PoliticianVoteRepository;
import br.com.deolhonacamara.api.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.PoliticianVoteDto;
import net.coelho.deolhonacamara.api.model.PoliticianVoteResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final VotingRepository votingRepository;
    private final PoliticianVoteRepository politicianVoteRepository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PoliticianVoteResponseDTO getByPoliticianId(Integer politicianId, int page, int size) {
        PageResponse<PoliticianVoteEntity> pageResponse = politicianVoteRepository.findByPoliticianId(politicianId, PageRequest.of(page, size));

        List<PoliticianVoteDto> dtos = pageResponse.getContent().stream()
                .map(entity -> mapper.toDto(entity))
                .collect(Collectors.toList());

        PoliticianVoteResponseDTO response = new PoliticianVoteResponseDTO();
        response.setData(dtos);
        response.setTotal((int) pageResponse.getTotalElements());
        response.setPage(pageResponse.getNumber());
        response.setTotalPages(pageResponse.getTotalPages());
        response.setSizePage(pageResponse.getSize());

        return response;
    }

    public PoliticianVoteWithPropositionResponseDTO getPoliticianVotesWithProposition(Integer politicianId, int page, int size) {
        return politicianVoteRepository.findVotesWithPropositionByPoliticianId(politicianId, PageRequest.of(page, size));
    }

    public VotingWithVotesResponseDTO getVotingsWithVotes(int page, int size) {
        return votingRepository.findVotingsWithVotes(PageRequest.of(page, size));
    }
}

