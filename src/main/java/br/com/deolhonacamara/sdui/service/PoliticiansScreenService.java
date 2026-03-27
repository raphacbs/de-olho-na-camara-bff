package br.com.deolhonacamara.sdui.service;

import br.com.deolhonacamara.api.service.PartyService;
import br.com.deolhonacamara.api.service.PoliticianService;
import br.com.deolhonacamara.sdui.model.ComponentAction;
import br.com.deolhonacamara.sdui.model.properties.FilterChip;
import br.com.deolhonacamara.sdui.model.properties.FilterGroup;
import br.com.deolhonacamara.sdui.model.properties.FilterSectionProperties;
import br.com.deolhonacamara.sdui.model.properties.PoliticianCardListProperties;
import br.com.deolhonacamara.sdui.model.properties.PoliticianCardProperties;
import br.com.deolhonacamara.sdui.model.properties.SearchBarProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.model.HomeScreenResponse;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.ScreenComponent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PoliticiansScreenService {

    private static final String ACTION_NAVIGATE = "NAVIGATE";
    private static final String SCREEN_ID = "politicians";
    private static final String SCREEN_VERSION = "1.0";

    private static final List<String> BRAZILIAN_STATES = List.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
            "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
            "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    private final PoliticianService politicianService;
    private final PartyService partyService;

    public HomeScreenResponse buildPoliticiansScreen(UUID userId, Map<String, Object> filters,
                                                     int page, int size) {
        log.info("Building SDUI politicians screen for user {} with filters {}", userId, filters);

        var politiciansPage = politicianService.getAll(page, size, filters, userId);
        var politicians = politiciansPage.getData();

        List<PoliticianCardProperties> cards = politicians == null ? List.of() :
                politicians.stream()
                        .map(this::toPoliticianCard)
                        .collect(Collectors.toList());

        var cardListProps = PoliticianCardListProperties.builder()
                .items(cards)
                .total(politiciansPage.getTotal())
                .currentPage(politiciansPage.getPage())
                .totalPages(politiciansPage.getTotalPages())
                .build();

        var response = new HomeScreenResponse();
        response.setScreenId(SCREEN_ID);
        response.setVersion(SCREEN_VERSION);
        response.setComponents(List.of(
                buildSearchBar(),
                buildFilterSection(),
                component("politicians-list", "POLITICIAN_CARD_LIST", cardListProps)
        ));
        return response;
    }

    private ScreenComponent buildSearchBar() {
        return component("politicians-search", "SEARCH_BAR",
                SearchBarProperties.builder()
                        .placeholder("Pesquisar por nome...")
                        .build());
    }

    private ScreenComponent buildFilterSection() {
        List<FilterChip> stateChips = BRAZILIAN_STATES.stream()
                .map(s -> FilterChip.builder().id(s).label(s).build())
                .collect(Collectors.toList());

        List<FilterChip> partyChips = new ArrayList<>();
        try {
            partyChips = partyService.getAll().stream()
                    .filter(p -> p.getAcronym() != null && !p.getAcronym().isBlank())
                    .map(p -> FilterChip.builder().id(p.getAcronym()).label(p.getAcronym()).build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Could not load parties for filter: {}", e.getMessage());
        }

        FilterGroup stateGroup = FilterGroup.builder()
                .label("Estado")
                .chips(stateChips)
                .build();

        FilterGroup partyGroup = FilterGroup.builder()
                .label("Partido")
                .chips(partyChips)
                .build();

        return component("politicians-filters", "FILTER_SECTION",
                FilterSectionProperties.builder()
                        .groups(List.of(stateGroup, partyGroup))
                        .applyButtonLabel("APLICAR FILTROS")
                        .clearButtonLabel("Limpar")
                        .build());
    }

    private PoliticianCardProperties toPoliticianCard(PoliticianDto p) {
        return PoliticianCardProperties.builder()
                .id(p.getId())
                .photoUrl(p.getPhotoUrl())
                .name(p.getName())
                .party(p.getParty())
                .state(p.getState())
                .propositionsTotal(p.getPropositionsTotal())
                .expenseTotal(p.getExpenseTotal())
                .isFollowed(Boolean.TRUE.equals(p.getIsFollowed()))
                .action(ComponentAction.builder()
                        .type(ACTION_NAVIGATE)
                        .route("/politicians/" + p.getId())
                        .build())
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
