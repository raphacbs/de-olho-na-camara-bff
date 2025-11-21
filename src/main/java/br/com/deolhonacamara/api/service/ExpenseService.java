package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.ExpenseEntity;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.input.ExpenseInput;
import br.com.deolhonacamara.api.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.ExpenseDto;
import net.coelho.deolhonacamara.api.model.ExpenseResponseDTO;
import net.coelho.deolhonacamara.api.model.ExpenseResponseDTODetail;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public ExpenseResponseDTO getByPoliticianId(ExpenseInput input) {
        var pageable = PageRequest.of(input.getPage(), input.getSizePage());
        PageResponse<ExpenseEntity> pageRes = repository.findByPoliticianId(input, pageable);
        List<ExpenseDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        ExpenseResponseDTODetail detail = new ExpenseResponseDTODetail();
        detail.setMonth(input.getMonth());
        detail.setYear(input.getYear());

        double totalFromEntities = pageRes.getContent().stream()
                .map(e -> e.getDocumentValue() == null ? java.math.BigDecimal.ZERO : e.getDocumentValue())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                .doubleValue();

        detail.setTotalExpenses(NumberFormat.getCurrencyInstance(new java.util.Locale("pt", "BR"))
                .format(totalFromEntities));

        var responseDto = new ExpenseResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());
        responseDto.detail(detail);

        return responseDto;
    }
}

