package br.com.deolhonacamara.api.service.sdui.builders;

import net.coelho.deolhonacamara.api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta do SDUIScreenBuilder.
 */
@Component
public class SDUIScreenBuilderImpl implements SDUIScreenBuilder {

    private String id;
    private String title;
    private SDUINavigation navigation;
    private final List<SDUIComponent> components = new ArrayList<>();
    private List<SDUIAction> actions = new ArrayList<>();
    private SDUIMetadata metadata;

    @Override
    public SDUIScreenBuilder withId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public SDUIScreenBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public SDUIScreenBuilder withNavigation(SDUINavigation navigation) {
        this.navigation = navigation;
        return this;
    }

    @Override
    public SDUIScreenBuilder addComponent(SDUIComponent component) {
        this.components.add(component);
        return this;
    }

    @Override
    public SDUIScreenBuilder addComponents(List<SDUIComponent> components) {
        this.components.addAll(components);
        return this;
    }

    @Override
    public SDUIScreenBuilder withActions(List<SDUIAction> actions) {
        this.actions = actions != null ? actions : new ArrayList<>();
        return this;
    }

    @Override
    public SDUIScreenBuilder withMetadata(SDUIMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public SDUIResponse build() {
        SDUIScreen screen = new SDUIScreen()
                .id(id)
                .title(title)
                .navigation(navigation)
                .components(components);

        SDUIResponse response = new SDUIResponse()
                .screen(screen);

        if (!actions.isEmpty()) {
            response.setActions(actions);
        }

        if (metadata != null) {
            response.setMetadata(metadata);
        }

        return response;
    }

    /**
     * Método auxiliar para resetar o builder para reutilização.
     */
    public void reset() {
        this.id = null;
        this.title = null;
        this.navigation = null;
        this.components.clear();
        this.actions.clear();
        this.metadata = null;
    }
}
