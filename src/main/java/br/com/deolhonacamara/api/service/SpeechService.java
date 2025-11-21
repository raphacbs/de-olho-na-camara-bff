package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.SpeechEntity;
import br.com.deolhonacamara.api.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.SpeechDto;
import net.coelho.deolhonacamara.api.model.SpeechResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeechService {

    private final SpeechRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public SpeechResponseDTO getByPoliticianId(Integer politicianId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        PageResponse<SpeechEntity> pageRes = repository.findByPoliticianId(politicianId, pageable);
        List<SpeechDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var responseDto = new SpeechResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }
}

