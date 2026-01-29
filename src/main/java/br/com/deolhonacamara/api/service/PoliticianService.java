package br.com.deolhonacamara.api.service;


import br.com.deolhonacamara.api.BusinessCode;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.repository.ExpenseRepository;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import br.com.deolhonacamara.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.PoliticianResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PoliticianService {

    private final PoliticianRepository repository;
    private final PropositionRepository propositionRepository;
    private final ExpenseRepository expenseRepository;
    private final Mapper mapper = Mapper.INSTANCE;

    public PoliticianResponseDTO getAll(int page, int size,  Map<String, Object> filters, UUID userId) {
        var pageable = PageRequest.of(page, size);

        // Verificar se há filtro de isFollowed
        PageResponse<PoliticianEntity> pageRes;
        if (filters.containsKey("isFollowed")) {
            pageRes = repository.findAllWithFollowedFilter(userId, pageable, filters);
        } else {
            pageRes = repository.findAll(pageable, filters);
        }

        Integer currentYear = filters.containsKey("year")
                ? ((Number) filters.get("year")).intValue()
                : LocalDate.now().getYear();

        log.debug("Fetching politicians for year: {}", currentYear);

        List<PoliticianDto> list = pageRes.getContent().stream().map(politician -> {
            Integer propositionsCount = propositionRepository.countByPoliticianIdAndYear(politician.getId(), currentYear);
            Integer expenseCount = expenseRepository.countByPoliticianIdAndYear(politician.getId(), currentYear);
            Boolean isFollowed = userId != null ? repository.isFollowedByUser(userId, politician.getId()) : false;

            log.debug("Politician {} (ID: {}): propositions={}, expenses={}, isFollowed={}",
                      politician.getName(), politician.getId(), propositionsCount, expenseCount, isFollowed);

            politician.setPropositionsTotal(propositionsCount);
            politician.setExpenseTotal(expenseCount);
            politician.setIsFollowed(isFollowed);
            return mapper.toDto(politician);
        }).collect(Collectors.toList());

        var portabilidadeResponseDto = new PoliticianResponseDTO();
        portabilidadeResponseDto.data(list);
        portabilidadeResponseDto.total((int) pageRes.getTotalElements());
        portabilidadeResponseDto.page(pageRes.getNumber());
        portabilidadeResponseDto.totalPages(pageRes.getTotalPages());
        portabilidadeResponseDto.sizePage(pageRes.getSize());

        return portabilidadeResponseDto;
    }

    public PoliticianDto getById(Integer id, UUID userId, Integer year) {
        Integer targetYear = year != null ? year : LocalDate.now().getYear();
        log.debug("Fetching politician by ID: {} for year: {}", id, targetYear);

        return repository.findById(id)
                .map(politician -> {
                    Integer propositionsCount = propositionRepository.countByPoliticianIdAndYear(politician.getId(), targetYear);
                    Integer expenseCount = expenseRepository.countByPoliticianIdAndYear(politician.getId(), targetYear);
                    Boolean isFollowed = userId != null ? repository.isFollowedByUser(userId, politician.getId()) : false;

                    log.debug("Politician {} (ID: {}): propositions={}, expenses={}, isFollowed={}",
                              politician.getName(), politician.getId(), propositionsCount, expenseCount, isFollowed);

                    politician.setPropositionsTotal(propositionsCount);
                    politician.setExpenseTotal(expenseCount);
                    politician.setIsFollowed(isFollowed);
                    return mapper.toDto(politician);
                })
                .orElseThrow(() -> new BusinessException(BusinessCode.POLITICIAN_NOT_FOUND, "Politician not found"));
    }

    public PoliticianResponseDTO getFollowedByUser(UUID userId, int page, int size, Map<String, Object> filters) {
        var pageable = PageRequest.of(page, size);
        PageResponse<PoliticianEntity> pageRes = repository.findFollowedByUser(userId, pageable, filters);
        Integer currentYear = LocalDate.now().getYear();

        log.debug("Fetching followed politicians for user {} for year: {}", userId, currentYear);

        List<PoliticianDto> list = pageRes.getContent().stream().map(politician -> {
            Integer propositionsCount = propositionRepository.countByPoliticianIdAndYear(politician.getId(), currentYear);
            Integer expenseCount = expenseRepository.countByPoliticianIdAndYear(politician.getId(), currentYear);
            Boolean isFollowed = repository.isFollowedByUser(userId, politician.getId());

            log.debug("Politician {} (ID: {}): propositions={}, expenses={}, isFollowed={}",
                      politician.getName(), politician.getId(), propositionsCount, expenseCount, isFollowed);

            politician.setPropositionsTotal(propositionsCount);
            politician.setExpenseTotal(expenseCount);
            politician.setIsFollowed(isFollowed);
            return mapper.toDto(politician);
        }).collect(Collectors.toList());

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
