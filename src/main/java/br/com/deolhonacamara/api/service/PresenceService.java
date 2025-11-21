package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PresenceEntity;
import br.com.deolhonacamara.api.repository.PresenceRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.PresenceDto;
import net.coelho.deolhonacamara.api.model.PresenceResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresenceService {

    private final PresenceRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PresenceResponseDTO getByPoliticianId(Integer politicianId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        PageResponse<PresenceEntity> pageRes = repository.findByPoliticianId(politicianId, pageable);
        List<PresenceDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var responseDto = new PresenceResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }
}

