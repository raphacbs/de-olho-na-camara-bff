package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
import br.com.deolhonacamara.api.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.PoliticianVoteDto;
import net.coelho.deolhonacamara.api.model.PoliticianVoteResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PoliticianVoteResponseDTO getByPoliticianId(Integer politicianId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        PageResponse<PoliticianVoteEntity> pageRes = repository.findByPoliticianId(politicianId, pageable);
        List<PoliticianVoteDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var responseDto = new PoliticianVoteResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }
}

