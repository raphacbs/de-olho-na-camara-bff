package br.com.deolhonacamara.api.service;


import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoliticianService {

    private final PoliticianRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PoliticianResponseDTO getAll(int page, int size,  Map<String, Object> filters) {
        var pageable = PageRequest.of(page, size);
        PageResponse<PoliticianEntity> pageRes = repository.findAll(pageable, filters);
        List<PoliticianDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var portabilidadeResponseDto = new PoliticianResponseDTO();
        portabilidadeResponseDto.data(list);
        portabilidadeResponseDto.total((int) pageRes.getTotalElements());
        portabilidadeResponseDto.page(pageRes.getNumber());
        portabilidadeResponseDto.totalPages(pageRes.getTotalPages());
        portabilidadeResponseDto.sizePage(pageRes.getSize());

        return portabilidadeResponseDto;
    }

    public PoliticianResponseDTO getFollowedByUser(UUID userId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        PageResponse<PoliticianEntity> pageRes = repository.findFollowedByUser(userId, pageable);
        List<PoliticianDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var portabilidadeResponseDto = new PoliticianResponseDTO();
        portabilidadeResponseDto.data(list);
        portabilidadeResponseDto.total((int) pageRes.getTotalElements());
        portabilidadeResponseDto.page(pageRes.getNumber());
        portabilidadeResponseDto.totalPages(pageRes.getTotalPages());
        portabilidadeResponseDto.sizePage(pageRes.getSize());
        return portabilidadeResponseDto;
    }

    public void follow(UUID userId, Integer politicianId) {
        repository.followPolitician(userId, politicianId);
    }

    public void unfollow(UUID userId, Integer politicianId) {
        repository.unfollowPolitician(userId, politicianId);
    }
}
