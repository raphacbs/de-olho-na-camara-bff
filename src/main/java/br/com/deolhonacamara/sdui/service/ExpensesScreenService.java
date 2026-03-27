package br.com.deolhonacamara.sdui.service;

import br.com.deolhonacamara.api.model.input.ExpenseInput;
import br.com.deolhonacamara.api.model.input.InputBuilder;
import br.com.deolhonacamara.api.service.ExpenseService;
import br.com.deolhonacamara.sdui.model.properties.ExpenseCardListProperties;
import br.com.deolhonacamara.sdui.model.properties.ExpenseCardProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.model.ExpenseDto;
import net.coelho.deolhonacamara.api.model.HomeScreenResponse;
import net.coelho.deolhonacamara.api.model.ScreenComponent;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExpensesScreenService {

    private static final String SCREEN_VERSION = "1.0";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String LABEL_SUPPLIER = "Fornecedor: ";
    private static final String LABEL_AMOUNT = "Valor: ";
    private static final String LABEL_DATE = "Data: ";

    private final ExpenseService expenseService;

    public HomeScreenResponse buildExpensesScreen(Integer politicianId, Integer year,
                                                  Integer month, int page, int size) {
        log.info("Building SDUI expenses screen for politician {}", politicianId);

        ExpenseInput input = InputBuilder
                .builder(ExpenseInput.class)
                .page(page)
                .sizePage(size)
                .build();

        input.setPoliticianId(politicianId);
        input.setYear(year);
        input.setMonth(month);

        var expensesPage = expenseService.getByPoliticianId(input);
        var expenses = expensesPage.getData();

        List<ExpenseCardProperties> cards = expenses == null ? List.of() :
                expenses.stream()
                        .map(this::toExpenseCard)
                        .collect(Collectors.toList());

        var cardListProps = ExpenseCardListProperties.builder()
                .items(cards)
                .total(expensesPage.getTotal())
                .currentPage(expensesPage.getPage())
                .totalPages(expensesPage.getTotalPages())
                .build();

        var response = new HomeScreenResponse();
        response.setScreenId("politician-expenses-" + politicianId);
        response.setVersion(SCREEN_VERSION);
        response.setComponents(List.of(
                component("expenses-list", "EXPENSE_CARD_LIST", cardListProps)
        ));
        return response;
    }

    private ExpenseCardProperties toExpenseCard(ExpenseDto e) {
        String date = e.getDocumentDate() != null
                ? e.getDocumentDate().format(DATE_FORMATTER) : "";
        String amount = e.getDocumentValue() != null ? e.getDocumentValue() : "R$ 0,00";

        return ExpenseCardProperties.builder()
                .id(e.getId())
                .expenseType(e.getExpenseType())
                .supplier(LABEL_SUPPLIER + (e.getSupplier() != null ? e.getSupplier() : ""))
                .amount(LABEL_AMOUNT + amount)
                .date(LABEL_DATE + date)
                .documentUrl(e.getDocumentUrl())
                .build();
    }

    private ScreenComponent component(String id, String type, Object properties) {
        var comp = new ScreenComponent();
        comp.setId(id);
        comp.setType(type);
        comp.setProperties(properties);
        return comp;
    }
}
