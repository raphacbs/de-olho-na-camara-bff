package br.com.deolhonacamara.api.service.sdui.builders;

import org.springframework.stereotype.Component;

/**
 * Implementação da factory para SDUIScreenBuilder.
 */
@Component
public class SDUIScreenBuilderFactoryImpl implements SDUIScreenBuilderFactory {

    @Override
    public SDUIScreenBuilder createBuilder() {
        return new SDUIScreenBuilderImpl();
    }
}