package br.com.deolhonacamara.api.service.sdui.components;

import net.coelho.deolhonacamara.api.model.SDUIComponent;

/**
 * Factory interface para criação de componentes SDUI.
 * Implementa o padrão Factory Method para encapsular a criação de componentes.
 */
public interface SDUIComponentFactory {

    /**
     * Cria um componente SDUI baseado no tipo especificado.
     *
     * @param type Tipo do componente a ser criado
     * @return Instância do componente SDUI
     */
    SDUIComponent createComponent(SDUIComponent.TypeEnum type);

    /**
     * Cria um componente Container.
     *
     * @param id ID único do componente
     * @return Componente Container configurado
     */
    SDUIComponent createContainer(String id);

    /**
     * Cria um componente TextBlock.
     *
     * @param id ID único do componente
     * @param text Texto a ser exibido
     * @param variant Variante do texto (display, title, subtitle, body, caption)
     * @return Componente TextBlock configurado
     */
    SDUIComponent createTextBlock(String id, String text, SDUIComponent.VariantEnum variant);

    /**
     * Cria um componente Button.
     *
     * @param id ID único do componente
     * @param title Título do botão
     * @param onPress Ação a ser executada quando pressionado
     * @return Componente Button configurado
     */
    SDUIComponent createButton(String id, String title, String onPress);

    /**
     * Cria um componente Card.
     *
     * @param id ID único do componente
     * @param title Título do card
     * @return Componente Card configurado
     */
    SDUIComponent createCard(String id, String title);

    /**
     * Cria um componente Card com título e subtítulo customizáveis.
     *
     * @param id ID único do componente
     * @param titleComponent Componente para o título
     * @param subtitleComponent Componente para o subtítulo (opcional)
     * @return Componente Card configurado
     */
    SDUIComponent createCard(String id, SDUIComponent titleComponent, SDUIComponent subtitleComponent);

    /**
     * Cria um componente Image.
     *
     * @param id ID único do componente
     * @param source URL da imagem
     * @param width Largura da imagem
     * @param height Altura da imagem
     * @return Componente Image configurado
     */
    SDUIComponent createImage(String id, String source, Integer width, Integer height);

    /**
     * Cria um componente Input.
     *
     * @param id ID único do componente
     * @param placeholder Placeholder do input
     * @param inputType Tipo do input
     * @return Componente Input configurado
     */
    SDUIComponent createInput(String id, String placeholder, SDUIComponent.InputTypeEnum inputType);

    /**
     * Cria um componente Spacer.
     *
     * @param id ID único do componente
     * @param size Tamanho do spacer
     * @return Componente Spacer configurado
     */
    SDUIComponent createSpacer(String id, SDUIComponent.SizeEnum size);

    /**
     * Cria um componente Avatar.
     *
     * @param id ID único do componente
     * @param source URL da imagem
     * @param width Largura da imagem
     * @param height Altura da imagem
     * @return Componente Avatar configurado
     */
    SDUIComponent createAvatar(String id, String source, Integer size);
}