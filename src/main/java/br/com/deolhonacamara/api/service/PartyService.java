package br.com.deolhonacamara.api.service;


import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.repository.PartyRepository;
import lombok.RequiredArgsConstructor;

import net.coelho.deolhonacamara.api.model.PartyDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public List<PartyDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
