package br.com.deolhonacamara.api.service.sdui.components;

import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.SDUIComponent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory para criação de modals de filtros reutilizáveis.
 * Cria bottom sheets modernos estilo iFood para filtros dinâmicos.
 */
@Component
@RequiredArgsConstructor
public class FilterModalFactory {

    private final SDUIComponentFactory componentFactory;

    /**
     * Configuração de um filtro de texto (campo de busca).
     */
    public static class TextFilterConfig {
        private final String id;
        private final String label;
        private final String placeholder;
        private String currentValue;

        public TextFilterConfig(String id, String label, String placeholder) {
            this.id = id;
            this.label = label;
            this.placeholder = placeholder;
        }

        public TextFilterConfig withValue(String value) {
            this.currentValue = value;
            return this;
        }

        public String getId() { return id; }
        public String getLabel() { return label; }
        public String getPlaceholder() { return placeholder; }
        public String getCurrentValue() { return currentValue; }
    }

    /**
     * Configuração de um filtro de data (intervalo).
     */
    public static class DateRangeFilterConfig {
        private final String id;
        private final String label;
        private String fromPlaceholder;
        private String toPlaceholder;
        private String fromValue;
        private String toValue;

        public DateRangeFilterConfig(String id, String label) {
            this.id = id;
            this.label = label;
            this.fromPlaceholder = "De";
            this.toPlaceholder = "Até";
        }

        public DateRangeFilterConfig withPlaceholders(String from, String to) {
            this.fromPlaceholder = from;
            this.toPlaceholder = to;
            return this;
        }

        public DateRangeFilterConfig withValues(String from, String to) {
            this.fromValue = from;
            this.toValue = to;
            return this;
        }

        public String getId() { return id; }
        public String getLabel() { return label; }
        public String getFromPlaceholder() { return fromPlaceholder; }
        public String getToPlaceholder() { return toPlaceholder; }
        public String getFromValue() { return fromValue; }
        public String getToValue() { return toValue; }
    }

    /**
     * Configuração de um filtro de seleção múltipla (botões).
     */
    public static class SelectionFilterConfig {
        private final String id;
        private final String label;
        private final List<SelectionOption> options;
        private List<String> selectedValues;

        public SelectionFilterConfig(String id, String label) {
            this.id = id;
            this.label = label;
            this.options = new ArrayList<>();
            this.selectedValues = new ArrayList<>();
        }

        public SelectionFilterConfig addOption(String value, String label, String icon) {
            this.options.add(new SelectionOption(value, label, icon));
            return this;
        }

        public SelectionFilterConfig withSelected(List<String> selected) {
            this.selectedValues = selected != null ? selected : new ArrayList<>();
            return this;
        }

        public String getId() { return id; }
        public String getLabel() { return label; }
        public List<SelectionOption> getOptions() { return options; }
        public List<String> getSelectedValues() { return selectedValues; }

        public static class SelectionOption {
            private final String value;
            private final String label;
            private final String icon;

            public SelectionOption(String value, String label, String icon) {
                this.value = value;
                this.label = label;
                this.icon = icon;
            }

            public String getValue() { return value; }
            public String getLabel() { return label; }
            public String getIcon() { return icon; }
        }
    }

    /**
     * Configuração completa do modal de filtros.
     */
    public static class FilterModalConfig {
        private final String id;
        private final String title;
        private String clearAction = "clear_filters";
        private String applyAction = "apply_filters";
        private final List<TextFilterConfig> textFilters = new ArrayList<>();
        private final List<DateRangeFilterConfig> dateRangeFilters = new ArrayList<>();
        private final List<SelectionFilterConfig> selectionFilters = new ArrayList<>();
        private int activeFiltersCount = 0;

        public FilterModalConfig(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public FilterModalConfig withActions(String clearAction, String applyAction) {
            this.clearAction = clearAction;
            this.applyAction = applyAction;
            return this;
        }

        public FilterModalConfig addTextFilter(TextFilterConfig filter) {
            this.textFilters.add(filter);
            if (filter.getCurrentValue() != null && !filter.getCurrentValue().trim().isEmpty()) {
                activeFiltersCount++;
            }
            return this;
        }

        public FilterModalConfig addDateRangeFilter(DateRangeFilterConfig filter) {
            this.dateRangeFilters.add(filter);
            if ((filter.getFromValue() != null && !filter.getFromValue().trim().isEmpty()) ||
                (filter.getToValue() != null && !filter.getToValue().trim().isEmpty())) {
                activeFiltersCount++;
            }
            return this;
        }

        public FilterModalConfig addSelectionFilter(SelectionFilterConfig filter) {
            this.selectionFilters.add(filter);
            if (filter.getSelectedValues() != null && !filter.getSelectedValues().isEmpty()) {
                activeFiltersCount++;
            }
            return this;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getClearAction() { return clearAction; }
        public String getApplyAction() { return applyAction; }
        public List<TextFilterConfig> getTextFilters() { return textFilters; }
        public List<DateRangeFilterConfig> getDateRangeFilters() { return dateRangeFilters; }
        public List<SelectionFilterConfig> getSelectionFilters() { return selectionFilters; }
        public int getActiveFiltersCount() { return activeFiltersCount; }
    }

    /**
     * Cria um modal de filtros baseado na configuração fornecida.
     * 
     * @param config Configuração do modal de filtros
     * @return Componente SDUI representando o modal de filtros
     */
    public SDUIComponent createFilterModal(FilterModalConfig config) {
        // Container wrapper com overlay e modal
        Map<String, Object> wrapperStyle = new HashMap<>();
        wrapperStyle.put("position", "fixed");
        wrapperStyle.put("top", "0");
        wrapperStyle.put("left", "0");
        wrapperStyle.put("right", "0");
        wrapperStyle.put("bottom", "0");
        wrapperStyle.put("zIndex", "999");
        wrapperStyle.put("display", "flex");
        wrapperStyle.put("alignItems", "flex-end");
        wrapperStyle.put("justifyContent", "center");

        SDUIComponent wrapper = componentFactory.createContainer(config.getId() + "-wrapper")
                .style(wrapperStyle);

        // Overlay/backdrop escuro
        Map<String, Object> overlayStyle = new HashMap<>();
        overlayStyle.put("position", "absolute");
        overlayStyle.put("top", "0");
        overlayStyle.put("left", "0");
        overlayStyle.put("right", "0");
        overlayStyle.put("bottom", "0");
        overlayStyle.put("backgroundColor", "rgba(0, 0, 0, 0.5)");
        overlayStyle.put("zIndex", "1");

        SDUIComponent overlay = componentFactory.createContainer(config.getId() + "-overlay")
                .style(overlayStyle)
                .onPress("close_filter_modal");

        // Container principal do modal (bottom sheet)
        Map<String, Object> modalStyle = new HashMap<>();
        modalStyle.put("position", "relative");
        modalStyle.put("width", "100%");
        modalStyle.put("maxWidth", "100%");
        modalStyle.put("borderTopLeftRadius", "24px");
        modalStyle.put("borderTopRightRadius", "24px");
        modalStyle.put("paddingTop", "24px");
        modalStyle.put("paddingBottom", "40px");
        modalStyle.put("paddingHorizontal", "24px");
        modalStyle.put("backgroundColor", "#FFFFFF");
        modalStyle.put("boxShadow", "0 -8px 32px rgba(0,0,0,0.2)");
        modalStyle.put("zIndex", "2");
        modalStyle.put("maxHeight", "90vh");
        modalStyle.put("overflowY", "auto");

        SDUIComponent modal = componentFactory.createContainer(config.getId() + "-modal")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .style(modalStyle);

        // Header do modal
        SDUIComponent header = buildModalHeader(config);
        modal.getChildren().add(header);

        // Conteúdo dos filtros
        SDUIComponent content = componentFactory.createContainer(config.getId() + "-content")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .style(Map.of("marginTop", "24px", "flex", "1", "overflowY", "auto"));

        // Adicionar filtros de texto
        for (TextFilterConfig textFilter : config.getTextFilters()) {
            content.getChildren().add(buildTextFilter(textFilter));
        }

        // Adicionar filtros de data (range slider simulado com campos de data)
        for (DateRangeFilterConfig dateFilter : config.getDateRangeFilters()) {
            content.getChildren().add(buildDateRangeFilter(dateFilter));
        }

        // Adicionar filtros de seleção
        for (SelectionFilterConfig selectionFilter : config.getSelectionFilters()) {
            content.getChildren().add(buildSelectionFilter(selectionFilter));
        }

        modal.getChildren().add(content);

        // Rodapé com botões de ação
        SDUIComponent footer = buildModalFooter(config);
        modal.getChildren().add(footer);

        // Adicionar overlay e modal ao wrapper
        wrapper.getChildren().add(overlay);
        wrapper.getChildren().add(modal);

        return wrapper;
    }

    private SDUIComponent buildModalHeader(FilterModalConfig config) {
        SDUIComponent header = componentFactory.createContainer(config.getId() + "-header")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                .alignItems(SDUIComponent.AlignItemsEnum.CENTER)
                .style(Map.of("marginBottom", "8px"));

        // Indicador de arraste (handle)
        Map<String, Object> handleStyle = new HashMap<>();
        handleStyle.put("width", "40px");
        handleStyle.put("height", "4px");
        handleStyle.put("backgroundColor", "#D1D5DB");
        handleStyle.put("borderRadius", "2px");
        handleStyle.put("marginBottom", "16px");
        handleStyle.put("alignSelf", "center");

        SDUIComponent handle = componentFactory.createContainer(config.getId() + "-handle")
                .style(handleStyle);

        // Título
        Map<String, Object> titleStyle = new HashMap<>();
        titleStyle.put("flex", "1");
        titleStyle.put("marginLeft", "0");

        SDUIComponent title = componentFactory.createTextBlock(
                config.getId() + "-title", config.getTitle(),
                SDUIComponent.VariantEnum.TITLE)
                .color("#111827")
                .fontSize(Integer.valueOf(24))
                .fontWeight("700")
                .style(titleStyle);

        // Botão de fechar
        Map<String, Object> closeButtonStyle = new HashMap<>();
        closeButtonStyle.put("width", "36px");
        closeButtonStyle.put("height", "36px");
        closeButtonStyle.put("backgroundColor", "#F3F4F6");
        closeButtonStyle.put("borderRadius", "18px");
        closeButtonStyle.put("padding", "0");
        closeButtonStyle.put("display", "flex");
        closeButtonStyle.put("alignItems", "center");
        closeButtonStyle.put("justifyContent", "center");
        closeButtonStyle.put("border", "none");

        SDUIComponent closeButton = componentFactory.createButton(
                config.getId() + "-close", "", "close_filter_modal")
                .style(closeButtonStyle);

        // Ícone X dentro do botão
        SDUIComponent closeIcon = componentFactory.createTextBlock(
                config.getId() + "-close-icon", "✕",
                SDUIComponent.VariantEnum.BODY)
                .color("#6B7280")
                .fontSize(Integer.valueOf(20))
                .fontWeight("400");

        closeButton.getChildren().add(closeIcon);

        header.getChildren().add(title);
        header.getChildren().add(closeButton);

        // Container para header e handle
        SDUIComponent headerContainer = componentFactory.createContainer(config.getId() + "-header-container")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .style(Map.of("marginBottom", "24px"));

        headerContainer.getChildren().add(handle);
        headerContainer.getChildren().add(header);

        return headerContainer;
    }

    private SDUIComponent buildTextFilter(TextFilterConfig config) {
        SDUIComponent container = componentFactory.createContainer(config.getId() + "-filter")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .style(Map.of("marginBottom", "24px"));

        // Label
        SDUIComponent label = componentFactory.createTextBlock(
                config.getId() + "-label", config.getLabel(),
                SDUIComponent.VariantEnum.BODY)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(16))
                .fontWeight("500")
                .style(Map.of("marginBottom", "8px"));

        // Input
        Map<String, Object> inputStyle = new HashMap<>();
        inputStyle.put("backgroundColor", "#FFFFFF");
        inputStyle.put("borderRadius", "12px");
        inputStyle.put("border", "2px solid #E5E7EB");
        inputStyle.put("padding", "14px 16px");
        inputStyle.put("fontSize", "16px");
        inputStyle.put("width", "100%");
        inputStyle.put("color", "#111827");
        inputStyle.put("minHeight", "48px");

        SDUIComponent input = componentFactory.createInput(
                config.getId() + "-input", config.getLabel(),
                SDUIComponent.InputTypeEnum.TEXT)
                .placeholder(config.getPlaceholder())
                .style(inputStyle);

        if (config.getCurrentValue() != null) {
            input.setText(config.getCurrentValue());
        }

        container.getChildren().add(label);
        container.getChildren().add(input);

        return container;
    }

    private SDUIComponent buildDateRangeFilter(DateRangeFilterConfig config) {
        SDUIComponent container = componentFactory.createContainer(config.getId() + "-filter")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .style(Map.of("marginBottom", "24px"));

        // Label
        SDUIComponent label = componentFactory.createContainer(config.getId() + "-label-row")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                .alignItems(SDUIComponent.AlignItemsEnum.CENTER);

        SDUIComponent labelText = componentFactory.createTextBlock(
                config.getId() + "-label", config.getLabel(),
                SDUIComponent.VariantEnum.BODY)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(16))
                .fontWeight("500");

        // Valor selecionado (se houver)
        String dateRange = "";
        if (config.getFromValue() != null && config.getToValue() != null) {
            dateRange = config.getFromValue() + " – " + config.getToValue();
        } else if (config.getFromValue() != null) {
            dateRange = config.getFromValue() + " –";
        } else if (config.getToValue() != null) {
            dateRange = "– " + config.getToValue();
        }

        SDUIComponent valueText = componentFactory.createTextBlock(
                config.getId() + "-value", dateRange,
                SDUIComponent.VariantEnum.BODY)
                .color("#8B5CF6")
                .fontSize(Integer.valueOf(14))
                .fontWeight("600");

        if (!dateRange.isEmpty()) {
            label.getChildren().add(labelText);
            label.getChildren().add(valueText);
        } else {
            label.getChildren().add(labelText);
        }

        // Container para os dois campos de data
        SDUIComponent dateInputs = componentFactory.createContainer(config.getId() + "-inputs")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .style(Map.of("marginTop", "8px", "gap", "12px"));

        // Data inicial
        Map<String, Object> dateInputStyle = new HashMap<>();
        dateInputStyle.put("flex", "1");
        dateInputStyle.put("backgroundColor", "#F8F9FA");
        dateInputStyle.put("borderRadius", "12px");
        dateInputStyle.put("border", "1px solid #E9ECEF");
        dateInputStyle.put("padding", "14px 16px");
        dateInputStyle.put("fontSize", "15px");

        SDUIComponent fromInput = componentFactory.createInput(
                config.getId() + "-from", config.getFromPlaceholder(),
                SDUIComponent.InputTypeEnum.TEXT)
                .placeholder(config.getFromPlaceholder())
                .style(dateInputStyle);

        if (config.getFromValue() != null) {
            fromInput.setText(config.getFromValue());
        }

        // Data final
        SDUIComponent toInput = componentFactory.createInput(
                config.getId() + "-to", config.getToPlaceholder(),
                SDUIComponent.InputTypeEnum.TEXT)
                .placeholder(config.getToPlaceholder())
                .style(dateInputStyle);

        if (config.getToValue() != null) {
            toInput.setText(config.getToValue());
        }

        dateInputs.getChildren().add(fromInput);
        dateInputs.getChildren().add(toInput);

        container.getChildren().add(label);
        container.getChildren().add(dateInputs);

        return container;
    }

    private SDUIComponent buildSelectionFilter(SelectionFilterConfig config) {
        SDUIComponent container = componentFactory.createContainer(config.getId() + "-filter")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .style(Map.of("marginBottom", "24px"));

        // Label
        SDUIComponent label = componentFactory.createTextBlock(
                config.getId() + "-label", config.getLabel(),
                SDUIComponent.VariantEnum.BODY)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(16))
                .fontWeight("500")
                .style(Map.of("marginBottom", "12px"));

        // Container para os botões de seleção
        SDUIComponent buttonsContainer = componentFactory.createContainer(config.getId() + "-buttons")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .style(Map.of("flexWrap", "wrap", "gap", "8px"));

        for (SelectionFilterConfig.SelectionOption option : config.getOptions()) {
            boolean isSelected = config.getSelectedValues().contains(option.getValue());
            
            Map<String, Object> buttonStyle = new HashMap<>();
            buttonStyle.put("backgroundColor", isSelected ? "#EDE9FE" : "#FFFFFF");
            buttonStyle.put("border", "2px solid " + (isSelected ? "#8B5CF6" : "#E5E7EB"));
            buttonStyle.put("borderRadius", "12px");
            buttonStyle.put("padding", "12px 20px");
            buttonStyle.put("fontSize", "15px");
            buttonStyle.put("fontWeight", isSelected ? "600" : "500");
            buttonStyle.put("color", isSelected ? "#6D28D9" : "#374151");
            buttonStyle.put("minWidth", "90px");
            buttonStyle.put("display", "flex");
            buttonStyle.put("alignItems", "center");
            buttonStyle.put("justifyContent", "center");
            buttonStyle.put("gap", "8px");
            buttonStyle.put("transition", "all 0.2s");

            Map<String, Object> actionParams = new HashMap<>();
            actionParams.put("filterId", config.getId());
            actionParams.put("optionValue", option.getValue());
            actionParams.put("toggle", "true");
            
            SDUIComponent button = componentFactory.createButton(
                    config.getId() + "-option-" + option.getValue(), option.getLabel(),
                    "toggle_filter_option")
                    .style(buttonStyle)
                    .actionParams(actionParams);

            // Adicionar ícone se houver
            if (option.getIcon() != null && !option.getIcon().isEmpty()) {
                SDUIComponent icon = componentFactory.createTextBlock(
                        config.getId() + "-icon-" + option.getValue(), option.getIcon(),
                        SDUIComponent.VariantEnum.BODY)
                        .fontSize(Integer.valueOf(16))
                        .style(Map.of("marginRight", "4px"));
                
                // Tentar adicionar como filho do botão (pode precisar de ajuste dependendo da estrutura)
                button.getChildren().add(icon);
            }

            buttonsContainer.getChildren().add(button);
        }

        container.getChildren().add(label);
        container.getChildren().add(buttonsContainer);

        return container;
    }

    private SDUIComponent buildModalFooter(FilterModalConfig config) {
        Map<String, Object> footerStyle = new HashMap<>();
        footerStyle.put("marginTop", "32px");
        footerStyle.put("paddingTop", "20px");
        footerStyle.put("borderTop", "1px solid #E9ECEF");
        footerStyle.put("gap", "12px");

        SDUIComponent footer = componentFactory.createContainer(config.getId() + "-footer")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .style(footerStyle);

        // Botão Limpar
        Map<String, Object> clearButtonStyle = new HashMap<>();
        clearButtonStyle.put("backgroundColor", "#FFFFFF");
        clearButtonStyle.put("border", "2px solid #E5E7EB");
        clearButtonStyle.put("color", "#6B7280");
        clearButtonStyle.put("borderRadius", "14px");
        clearButtonStyle.put("padding", "16px 24px");
        clearButtonStyle.put("fontSize", "16px");
        clearButtonStyle.put("fontWeight", "600");
        clearButtonStyle.put("flex", "1");
        clearButtonStyle.put("minHeight", "52px");

        SDUIComponent clearButton = componentFactory.createButton(
                config.getId() + "-clear", "Limpar",
                config.getClearAction())
                .style(clearButtonStyle);

        // Botão Aplicar
        Map<String, Object> applyButtonStyle = new HashMap<>();
        applyButtonStyle.put("backgroundColor", "#8B5CF6");
        applyButtonStyle.put("color", "#FFFFFF");
        applyButtonStyle.put("borderRadius", "14px");
        applyButtonStyle.put("padding", "16px 24px");
        applyButtonStyle.put("fontSize", "17px");
        applyButtonStyle.put("fontWeight", "700");
        applyButtonStyle.put("flex", "1.5");
        applyButtonStyle.put("position", "relative");
        applyButtonStyle.put("display", "flex");
        applyButtonStyle.put("alignItems", "center");
        applyButtonStyle.put("justifyContent", "center");
        applyButtonStyle.put("gap", "8px");
        applyButtonStyle.put("minHeight", "52px");
        applyButtonStyle.put("boxShadow", "0 4px 12px rgba(139, 92, 246, 0.3)");

        SDUIComponent applyButton = componentFactory.createButton(
                config.getId() + "-apply", "Filtrar",
                config.getApplyAction())
                .style(applyButtonStyle);

        // Badge com contador de filtros ativos
        if (config.getActiveFiltersCount() > 0) {
            Map<String, Object> badgeStyle = new HashMap<>();
            badgeStyle.put("position", "absolute");
            badgeStyle.put("top", "-8px");
            badgeStyle.put("right", "-8px");
            badgeStyle.put("backgroundColor", "#EF4444");
            badgeStyle.put("color", "#FFFFFF");
            badgeStyle.put("borderRadius", "14px");
            badgeStyle.put("minWidth", "24px");
            badgeStyle.put("height", "24px");
            badgeStyle.put("display", "flex");
            badgeStyle.put("alignItems", "center");
            badgeStyle.put("justifyContent", "center");
            badgeStyle.put("fontSize", "12px");
            badgeStyle.put("fontWeight", "700");
            badgeStyle.put("border", "2px solid #FFFFFF");
            badgeStyle.put("boxShadow", "0 2px 8px rgba(0,0,0,0.2)");

            SDUIComponent badge = componentFactory.createContainer(config.getId() + "-badge")
                    .style(badgeStyle);

            SDUIComponent badgeText = componentFactory.createTextBlock(
                    config.getId() + "-badge-text", String.valueOf(config.getActiveFiltersCount()),
                    SDUIComponent.VariantEnum.CAPTION)
                    .color("#8B5CF6")
                    .fontSize(Integer.valueOf(12))
                    .fontWeight("700");

            badge.getChildren().add(badgeText);
            applyButton.getChildren().add(badge);

            // Ícone de funil
            SDUIComponent filterIcon = componentFactory.createTextBlock(
                    config.getId() + "-filter-icon", "🔍",
                    SDUIComponent.VariantEnum.BODY)
                    .fontSize(Integer.valueOf(16));
            
            applyButton.getChildren().add(filterIcon);
        }

        footer.getChildren().add(clearButton);
        footer.getChildren().add(applyButton);

        return footer;
    }
}
