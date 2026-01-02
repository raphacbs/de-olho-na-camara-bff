package br.com.deolhonacamara.api.service.sdui.screens;

import br.com.deolhonacamara.api.model.PropositionType;
import br.com.deolhonacamara.api.model.screen.PropositionScreen;
import br.com.deolhonacamara.api.service.PropositionService;
import br.com.deolhonacamara.api.service.sdui.components.AdvancedFilterFactory;
import br.com.deolhonacamara.api.service.sdui.components.FilterModalFactory;
import br.com.deolhonacamara.api.service.sdui.components.PropositionCardFactory;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Estratégia para construção da tela de Proposições.
 */
@Component
@RequiredArgsConstructor
public class ProposicoesScreenStrategy implements SDUIScreenStrategy {

    private final br.com.deolhonacamara.api.service.sdui.components.SDUIComponentFactory componentFactory;
    private final br.com.deolhonacamara.api.service.sdui.builders.SDUIScreenBuilderFactory screenBuilderFactory;
    private final PropositionService propositionService;
    private final PropositionCardFactory propositionCardFactory;
    private final FilterModalFactory filterModalFactory;
    private final AdvancedFilterFactory advancedFilterFactory;

    @Override
    public String getScreenType() {
        return "proposicoes";
    }

    @Override
    public SDUIResponse buildScreen(Map<String, Object> params) {
        String tipo = (String) params.get("tipo");
        String status = (String) params.get("status");
        String periodo = (String) params.get("periodo");
        String politico = (String) params.get("politico");
        String dataInicio = (String) params.get("dataInicio");
        String dataFim = (String) params.get("dataFim");
        Integer page = (Integer) params.getOrDefault("page", 0);
        Integer size = (Integer) params.getOrDefault("size", 20);

        List<SDUIComponent> components = new ArrayList<>();

        // Filtro avançado
        components.add(buildFilterComponent());

        // Lista de proposições
        components.add(buildPropositionsList(tipo, status, periodo, politico, dataInicio, dataFim, page, size));

        // Navegação
        SDUINavigation navigation = new SDUINavigation()
                .header(new SDUINavigationHeader()
                        .title("Proposições")
                        .showBack(false));

        // Metadados
        SDUIMetadata metadata = new SDUIMetadata()
                .version("1.0.0")
                .cache(true)
                .ttl(1800);

        return screenBuilderFactory.createBuilder()
                .withId("proposals")
                .withTitle("Propostas")
                .withNavigation(navigation)
                .addComponents(components)
                .withMetadata(metadata)
                .build();
    }

    private SDUIComponent buildFilterComponent() {
        // Cria as opções para o filtro de tipo usando todos os tipos do enum PropositionType
        List<AdvancedFilterFactory.FilterOption> tipoOptions = createPropositionTypeFilterOptions();

        // Cria as opções para o filtro de status
        List<AdvancedFilterFactory.FilterOption> statusOptions = List.of(
            new AdvancedFilterFactory.FilterOption("tramitando", "Em Tramitação", true),
            new AdvancedFilterFactory.FilterOption("arquivado", "Arquivado", false),
            new AdvancedFilterFactory.FilterOption("aprovado", "Aprovado", false),
            new AdvancedFilterFactory.FilterOption("rejeitado", "Rejeitado", false),
            new AdvancedFilterFactory.FilterOption("vetado", "Vetado", false)
        );

        // Cria as opções para período
        List<AdvancedFilterFactory.FilterOption> periodoOptions = List.of(
            new AdvancedFilterFactory.FilterOption("ultima_semana", "Última Semana", false),
            new AdvancedFilterFactory.FilterOption("ultimo_mes", "Último Mês", false),
            new AdvancedFilterFactory.FilterOption("ultimos_3_meses", "Últimos 3 Meses", true),
            new AdvancedFilterFactory.FilterOption("ultimo_ano", "Último Ano", false),
            new AdvancedFilterFactory.FilterOption("personalizado", "Período Personalizado", false)
        );

        // Cria as seções do filtro
        List<AdvancedFilterFactory.FilterSection> sections = List.of(
            new AdvancedFilterFactory.FilterSection("tipo", "Tipo de Proposição", "single", tipoOptions),
            new AdvancedFilterFactory.FilterSection("status", "Status da Tramitação", "single", statusOptions),
            new AdvancedFilterFactory.FilterSection("periodo", "Período", "single", periodoOptions)
        );

        // Cria o componente AdvancedFilter
        SDUIComponent advancedFilter = advancedFilterFactory.createAdvancedFilter(
            "filter-propositions",
            "Filtrar Propostas",
            "Filtros de Propostas",
            "#009C3B",
            "Buscar por Propostas...",
            "apply_proposition_filters",
            sections
        );

        // Configura actionParams para direcionar à tela de proposições
        Map<String, Object> actionParams = Map.of(
            "targetScreen", "proposicoes",
            "filterType", "advanced"
        );
        advancedFilter.setActionParams(actionParams);

        return advancedFilter;
    }

    private SDUIComponent buildPropositionsList(String tipo, String status, String periodo, String politico,
            String dataInicio, String dataFim, Integer page, Integer size) {
        SDUIComponent container = componentFactory.createContainer("container-proposals-list-main")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .scrollable(true);

        // Converter período para datas se fornecido
        LocalDate periodoInicioParsed = null;
        LocalDate periodoFimParsed = null;
        if (periodo != null && !periodo.trim().isEmpty()) {
            LocalDate now = LocalDate.now();
            switch (periodo.trim()) {
                case "ultima_semana":
                    periodoInicioParsed = now.minusWeeks(1);
                    periodoFimParsed = now;
                    break;
                case "ultimo_mes":
                    periodoInicioParsed = now.minusMonths(1);
                    periodoFimParsed = now;
                    break;
                case "ultimos_3_meses":
                    periodoInicioParsed = now.minusMonths(3);
                    periodoFimParsed = now;
                    break;
                case "ultimo_ano":
                    periodoInicioParsed = now.minusYears(1);
                    periodoFimParsed = now;
                    break;
                // Para "personalizado", as datas serão fornecidas separadamente
            }
        }

        // Converter datas de string para LocalDate se fornecidas (sobrescreve período se personalizado)
        LocalDate dataInicioParsed = null;
        LocalDate dataFimParsed = null;
        try {
            if (dataInicio != null && !dataInicio.trim().isEmpty()) {
                dataInicioParsed = LocalDate.parse(dataInicio);
            }
            if (dataFim != null && !dataFim.trim().isEmpty()) {
                dataFimParsed = LocalDate.parse(dataFim);
            }
        } catch (Exception e) {
            // Ignorar erros de parsing e usar null
        }

        // Usar datas do período se não houver datas personalizadas
        if (dataInicioParsed == null && periodoInicioParsed != null) {
            dataInicioParsed = periodoInicioParsed;
        }
        if (dataFimParsed == null && periodoFimParsed != null) {
            dataFimParsed = periodoFimParsed;
        }

        // Buscar proposições com filtros aplicados
        List<PropositionScreen> propositions;
        if ((politico == null || politico.trim().isEmpty()) &&
                (tipo == null || tipo.trim().isEmpty()) &&
                dataInicioParsed == null && dataFimParsed == null) {
            // Se não há filtros aplicados, usar o mesmo método que o HomeScreenStrategy
            propositions = propositionService.getLatestPropositionsScreen(size);
        } else {
            // Se há filtros, usar o método filtrado
            propositions = propositionService.getFilteredPropositionsScreen(
                    politico, tipo, status, dataInicioParsed, dataFimParsed, size);
        }

        // Agrupar proposições por mês
        Map<String, List<PropositionScreen>> propositionsByMonth = propositions.stream()
                .collect(Collectors.groupingBy(
                        prop -> {
                            LocalDate date = prop.getPresentationDate();
                            if (date != null) {
                                return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                            }
                            return "sem-data";
                        },
                        LinkedHashMap::new,
                        Collectors.toList()));

        // Adicionar seções por mês
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        for (Map.Entry<String, List<PropositionScreen>> entry : propositionsByMonth.entrySet()) {
            String monthKey = entry.getKey();
            List<PropositionScreen> monthPropositions = entry.getValue();

            // Criar cabeçalho do mês
            String monthTitle;
            if ("sem-data".equals(monthKey)) {
                monthTitle = "Sem Data";
            } else {
                LocalDate monthDate = LocalDate.parse(monthKey + "-01");
                monthTitle = capitalizeFirstLetter(monthDate.format(monthFormatter));
            }

            SDUIComponent monthHeader = componentFactory.createTextBlock(
                    "month-header-" + monthKey,
                    monthTitle,
                    SDUIComponent.VariantEnum.HEADLINE)
                    .margin("16")
                    .textAlign(SDUIComponent.TextAlignEnum.LEFT);

            container.getChildren().add(monthHeader);

            // Adicionar proposições do mês
            for (PropositionScreen proposition : monthPropositions) {
                container.getChildren().add(buildPropositionCard(proposition));
            }
        }

        // Se não houver proposições, mostrar mensagem
        if (propositions.isEmpty()) {
            SDUIComponent emptyMessage = componentFactory.createTextBlock(
                    "textblock-no-propositions-main", "Nenhuma proposição encontrada", SDUIComponent.VariantEnum.BODY)
                    .textAlign(SDUIComponent.TextAlignEnum.CENTER);
            container.getChildren().add(emptyMessage);
        }

        return container;
    }

    private SDUIComponent buildPropositionCard(PropositionScreen proposition) {
        return propositionCardFactory.createPropositionCard(proposition);
    }

    /**
     * Cria dinamicamente as opções de filtro para tipos de proposição usando o enum PropositionType.
     * Isso garante que todas as opções estejam sempre sincronizadas com o enum e
     * qualquer novo tipo adicionado ao enum será automaticamente incluído no filtro.
     */
    private List<AdvancedFilterFactory.FilterOption> createPropositionTypeFilterOptions() {
        return Arrays.stream(PropositionType.values())
                .map(type -> new AdvancedFilterFactory.FilterOption(
                    type.getCode(),
                    type.getDescription(),
                    false // Nenhum tipo selecionado por padrão
                ))
                .collect(Collectors.toList());
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}