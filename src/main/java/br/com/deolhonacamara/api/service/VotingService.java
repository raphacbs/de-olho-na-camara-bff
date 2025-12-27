package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
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

    private final VotingRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PoliticianVoteResponseDTO getByPoliticianId(Integer politicianId, int page, int size) {
       return null;
    }
}

