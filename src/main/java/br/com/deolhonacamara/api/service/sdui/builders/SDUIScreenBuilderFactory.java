package br.com.deolhonacamara.api.service.sdui.builders;

/**
 * Factory para criação de instâncias SDUIScreenBuilder.
 * Garante que cada chamada tenha seu próprio builder isolado.
 */
public interface SDUIScreenBuilderFactory {

    /**
     * Cria uma nova instância de SDUIScreenBuilder.
     *
     * @return Nova instância de SDUIScreenBuilder
     */
    SDUIScreenBuilder createBuilder();
}