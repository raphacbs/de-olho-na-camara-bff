package br.com.deolhonacamara.api.service.sdui.builders;

import net.coelho.deolhonacamara.api.model.*;

/**
 * Builder para construção de telas SDUI completas.
 * Implementa o padrão Builder para facilitar a construção de telas complexas.
 */
public interface SDUIScreenBuilder {

    /**
     * Define o ID da tela.
     *
     * @param id ID da tela
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder withId(String id);

    /**
     * Define o título da tela.
     *
     * @param title Título da tela
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder withTitle(String title);

    /**
     * Adiciona navegação à tela.
     *
     * @param navigation Configuração de navegação
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder withNavigation(SDUINavigation navigation);

    /**
     * Adiciona um componente à tela.
     *
     * @param component Componente a ser adicionado
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder addComponent(SDUIComponent component);

    /**
     * Adiciona múltiplos componentes à tela.
     *
     * @param components Lista de componentes a serem adicionados
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder addComponents(java.util.List<SDUIComponent> components);

    /**
     * Define as ações disponíveis na tela.
     *
     * @param actions Lista de ações
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder withActions(java.util.List<SDUIAction> actions);

    /**
     * Define os metadados da tela.
     *
     * @param metadata Metadados da tela
     * @return Instância do builder para encadeamento
     */
    SDUIScreenBuilder withMetadata(SDUIMetadata metadata);

    /**
     * Constrói a resposta SDUI completa.
     *
     * @return SDUIResponse configurada
     */
    SDUIResponse build();
}