package br.com.deolhonacamara.api.service.sdui.components;

import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.SDUIComponent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory para criação de componentes AdvancedFilter.
 * Cria filtros avançados com seções configuráveis para busca e seleção múltipla.
 */
@Component
@RequiredArgsConstructor
public class AdvancedFilterFactory {

    private final SDUIComponentFactory componentFactory;

    /**
     * Configuração de uma seção de filtro.
     */
    public static class FilterSection {
        private final String id;
        private final String title;
        private final String type; // "single" ou "multi"
        private final List<FilterOption> options;

        public FilterSection(String id, String title, String type, List<FilterOption> options) {
            this.id = id;
            this.title = title;
            this.type = type;
            this.options = options != null ? options : new ArrayList<>();
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getType() { return type; }
        public List<FilterOption> getOptions() { return options; }
    }

    /**
     * Configuração de uma opção de filtro.
     */
    public static class FilterOption {
        private final String id;
        private final String label;
        private final boolean selected;

        public FilterOption(String id, String label, boolean selected) {
            this.id = id;
            this.label = label;
            this.selected = selected;
        }

        public String getId() { return id; }
        public String getLabel() { return label; }
        public boolean isSelected() { return selected; }
    }

    /**
     * Cria um componente AdvancedFilter.
     *
     * @param id ID único do componente
     * @param triggerLabel Rótulo do botão que abre o filtro
     * @param title Título do modal de filtro
     * @param primaryColor Cor primária (ex: "#EA1D2C")
     * @param searchPlaceholder Placeholder para o campo de busca
     * @param applyActionId ID da ação para aplicar os filtros
     * @param sections Lista de seções de filtro
     * @return Componente AdvancedFilter configurado
     */
    public SDUIComponent createAdvancedFilter(String id, String triggerLabel, String title,
                                             String primaryColor, String searchPlaceholder,
                                             String applyActionId, List<FilterSection> sections) {
        SDUIComponent advancedFilter = componentFactory.createComponent(SDUIComponent.TypeEnum.ADVANCED_FILTER);
        advancedFilter.setId(id);

        // Configura as propriedades no nível raiz do componente
        advancedFilter.setTitle(title != null ? title : "Opções de Filtro");

        // Configura as propriedades específicas do AdvancedFilter
        Map<String, Object> props = new HashMap<>();
        props.put("triggerLabel", triggerLabel != null ? triggerLabel : "Filtrar");
        props.put("primaryColor", primaryColor != null ? primaryColor : "#EA1D2C");
        props.put("searchPlaceholder", searchPlaceholder != null ? searchPlaceholder : "Busque por item...");
        props.put("applyActionId", applyActionId != null ? applyActionId : "apply_filters");

        // Converte as seções para o formato esperado
        List<Map<String, Object>> sectionsData = new ArrayList<>();
        if (sections != null) {
            for (FilterSection section : sections) {
                Map<String, Object> sectionData = new HashMap<>();
                sectionData.put("id", section.getId());
                sectionData.put("title", section.getTitle());
                sectionData.put("type", section.getType());

                // Converte as opções
                List<Map<String, Object>> optionsData = new ArrayList<>();
                for (FilterOption option : section.getOptions()) {
                    Map<String, Object> optionData = new HashMap<>();
                    optionData.put("id", option.getId());
                    optionData.put("label", option.getLabel());
                    optionData.put("selected", option.isSelected());
                    optionsData.add(optionData);
                }
                sectionData.put("options", optionsData);
                sectionsData.add(sectionData);
            }
        }
        props.put("sections", sectionsData);

        advancedFilter.setProps(props);
        return advancedFilter;
    }

    /**
     * Cria um AdvancedFilter baseado no exemplo fornecido pelo usuário.
     *
     * @param id ID único do componente
     * @return Componente AdvancedFilter com configuração de exemplo
     */
    public SDUIComponent createExampleAdvancedFilter(String id) {
        // Cria as opções para cada seção
        List<FilterOption> categoryOptions = List.of(
            new FilterOption("promo", "Promoções", false),
            new FilterOption("lunch", "Almoço", true)
        );

        List<FilterOption> priceOptions = List.of(
            new FilterOption("low", "Até R$ 20", false),
            new FilterOption("mid", "R$ 20 - R$ 50", false)
        );

        // Cria as seções
        List<FilterSection> sections = List.of(
            new FilterSection("category", "Categorias", "multi", categoryOptions),
            new FilterSection("price", "Faixa de Preço", "single", priceOptions)
        );

        return createAdvancedFilter(id, "Filtrar Cardápio", "Opções de Filtro",
                                   "#EA1D2C", "Busque por item...", "apply_filters", sections);
    }
}