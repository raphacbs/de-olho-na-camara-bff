package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.input.PropositionInput;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.PropositionDto;
import net.coelho.deolhonacamara.api.model.PropositionResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropositionService {

    private final PropositionRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PropositionResponseDTO getByPoliticianId(PropositionInput input) {
        var pageable = PageRequest.of(input.getPage(), input.getSizePage());
        PageResponse<PropositionEntity> pageRes = repository.findByPoliticianId(input.getPropositionId(), pageable);
        List<PropositionDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var responseDto = new PropositionResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }
}

