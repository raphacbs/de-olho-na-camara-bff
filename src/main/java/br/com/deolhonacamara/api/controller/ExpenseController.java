package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.model.input.ExpenseInput;
import br.com.deolhonacamara.api.model.input.InputBuilder;
import br.com.deolhonacamara.api.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.ExpensesApi;
import net.coelho.deolhonacamara.api.model.ExpenseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ExpenseController implements ExpensesApi {

    private final ExpenseService expenseService;

    @Override
    public ResponseEntity<ExpenseResponseDTO> getPoliticianExpenses(Integer id, Integer page, Integer size, Integer year, Integer month) {

        var expenseInput = InputBuilder
                .builder(ExpenseInput.class)
                .page(page != null ? page : 0)
                .sizePage(size != null ? size : 20)
                .build();

        expenseInput.setPoliticianId(id);
        expenseInput.setYear(year);
        expenseInput.setMonth(month);

        return ResponseEntity.ok(expenseService.getByPoliticianId(expenseInput));
    }
}
