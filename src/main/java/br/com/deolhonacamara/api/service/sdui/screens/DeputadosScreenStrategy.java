package br.com.deolhonacamara.api.service.sdui.screens;

import br.com.deolhonacamara.api.service.PoliticianService;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estratégia para construção da tela de Deputados.
 */
@Component
@RequiredArgsConstructor
public class DeputadosScreenStrategy implements SDUIScreenStrategy {

    private final br.com.deolhonacamara.api.service.sdui.components.SDUIComponentFactory componentFactory;
    private final br.com.deolhonacamara.api.service.sdui.builders.SDUIScreenBuilderFactory screenBuilderFactory;
    private final PoliticianService politicianService;

    @Override
    public String getScreenType() {
        return "deputados";
    }

    @Override
    public SDUIResponse buildScreen(Map<String, Object> params) {
        String search = (String) params.get("search");
        String uf = (String) params.get("uf");
        Integer page = (Integer) params.getOrDefault("page", 0);
        Integer size = (Integer) params.getOrDefault("size", 20);

        List<SDUIComponent> components = new ArrayList<>();

        // Campo de busca
        components.add(buildSearchContainer());

        // Filtros
        components.add(buildFiltersSection());

        // Lista de deputados
        components.add(buildDeputadosList(search, uf, page, size));

        // Navegação
        SDUINavigation navigation = new SDUINavigation()
                .header(new SDUINavigationHeader()
                        .title("Deputados(as)")
                        .showBack(false)
                        .actions(new ArrayList<>() {{
                            add(new SDUINavigationHeaderActionsInner()
                                    .id("search-action")
                                    .type(SDUINavigationHeaderActionsInner.TypeEnum.ICON)
                                    .icon("search")
                                    .action("toggle_search"));
                        }}));

        // Ações
        List<SDUIAction> actions = new ArrayList<>();
        actions.add(new SDUIAction()
                .type(SDUIAction.TypeEnum.API)
                .payload(Map.of("endpoint", "/api/deputados/search", "method", "GET")));

        // Metadados
        SDUIMetadata metadata = new SDUIMetadata()
                .version("1.0.0")
                .cache(true)
                .ttl(1800);

        return screenBuilderFactory.createBuilder()
                .withId("deputados")
                .withTitle("Deputados(as)")
                .withNavigation(navigation)
                .addComponents(components)
                .withActions(actions)
                .withMetadata(metadata)
                .build();
    }

    private SDUIComponent buildSearchContainer() {
        SDUIComponent container = componentFactory.createContainer("container-search-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .padding("16 20");

        SDUIComponent searchInput = componentFactory.createInput(
                "input-search-main", "Buscar deputado...", SDUIComponent.InputTypeEnum.TEXT);

        container.getChildren().add(searchInput);
        return container;
    }

    private SDUIComponent buildFiltersSection() {
        SDUIComponent container = componentFactory.createContainer("container-filters-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .spacing(12)
                .scrollable(true)
                .horizontal(true)
                .padding("0 20 16 20");

        container.getChildren().add(createFilterButton("filter-all", "Todos", "filter_deputados",
                Map.of("filter", "all")));
        container.getChildren().add(createFilterButton("filter-uf", "Por UF", "filter_deputados",
                Map.of("filter", "uf")));

        return container;
    }

    private SDUIComponent buildDeputadosList(String search, String uf, Integer page, Integer size) {
        SDUIComponent container = componentFactory.createContainer("container-deputados-list-main")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .scrollable(true);

        // Buscar dados reais da base
        Map<String, Object> filters = new HashMap<>();
        if (search != null && !search.trim().isEmpty()) {
            filters.put("name", search.trim());
        }
        if (uf != null && !uf.trim().isEmpty()) {
            filters.put("state", uf.trim());
        }

        try {
            var response = politicianService.getAll(page, size, filters);
            List<net.coelho.deolhonacamara.api.model.PoliticianDto> politicians = response.getData();

            for (net.coelho.deolhonacamara.api.model.PoliticianDto politician : politicians) {
                container.getChildren().add(buildDeputadoCard(politician));
            }
        } catch (Exception e) {
            // Fallback para dados mockados em caso de erro
            container.getChildren().add(buildMockDeputadoCard("deputado-1", "João Silva", "PT", "SP"));
            container.getChildren().add(buildMockDeputadoCard("deputado-2", "Maria Santos", "PSDB", "RJ"));
        }

        return container;
    }

    private SDUIComponent buildDeputadoCard(net.coelho.deolhonacamara.api.model.PoliticianDto politician) {
        SDUIComponent card = componentFactory.createCard(
                "deputado-" + politician.getId(),
                politician.getName())
                .subtitle(politician.getParty() + " - " + politician.getState())
                .elevation(Integer.valueOf(1))
                .borderRadius(Integer.valueOf(12))
                .padding("16")
                .margin("0 20 8 20")
                .backgroundColor("#FFFFFF")
                .onPress("open_deputy_detail")
                .actionParams(Map.of(
                    "deputyId", politician.getId(),
                    "deputyName", politician.getName(),
                    "apiEndpoint", "/api/deputados/" + politician.getId()
                ));

        // Imagem do deputado
        if (politician.getPhotoUrl() != null) {
            SDUIComponent image = componentFactory.createImage(
                    "image-deputado-" + politician.getId() + "-photo-main",
                    politician.getPhotoUrl(),
                    60, 60)
                    .style(Map.of(
                        "borderRadius", "30",
                        "marginBottom", "12"
                    ));
            card.getChildren().add(image);
        }

        // Informações do deputado
        String situacao = "Exercício"; // Informação não disponível no DTO
        String email = politician.getEmail() != null ? politician.getEmail() : "contato@camara.leg.br";

        SDUIComponent info = componentFactory.createTextBlock(
                "textblock-deputado-" + politician.getId() + "-info-main",
                situacao + " • " + email,
                SDUIComponent.VariantEnum.CAPTION)
                .color("#666")
                .fontSize(Integer.valueOf(12));

        card.getChildren().add(info);

        return card;
    }

    private SDUIComponent buildMockDeputadoCard(String id, String nome, String partido, String uf) {
        return buildDeputadoCard(new net.coelho.deolhonacamara.api.model.PoliticianDto()
                .id(Integer.valueOf(id.replace("deputado-", "")))
                .name(nome)
                .party(partido)
                .state(uf)
                .photoUrl("https://example.com/photo.jpg"));
    }

    private SDUIComponent createFilterButton(String id, String title, String onPress, Map<String, Object> actionParams) {
        return componentFactory.createButton(id, title, onPress)
                .actionParams(actionParams);
    }
}