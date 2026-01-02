package br.com.deolhonacamara.api.service.sdui.components;

import net.coelho.deolhonacamara.api.model.SDUIComponent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementação concreta da SDUIComponentFactory.
 * Responsável por criar instâncias dos componentes SDUI.
 */
@Component
public class SDUIComponentFactoryImpl implements SDUIComponentFactory {

    @Override
    public SDUIComponent createComponent(SDUIComponent.TypeEnum type) {
        SDUIComponent component = new SDUIComponent();
        component.setType(type);
        return component;
    }

    @Override
    public SDUIComponent createContainer(String id) {
        SDUIComponent container = createComponent(SDUIComponent.TypeEnum.CONTAINER);
        container.setId(id);
        container.setChildren(new ArrayList<>());
        return container;
    }

    @Override
    public SDUIComponent createTextBlock(String id, String text, SDUIComponent.VariantEnum variant) {
        SDUIComponent textBlock = createComponent(SDUIComponent.TypeEnum.TEXT_BLOCK);
        textBlock.setId(id);
        textBlock.setText(text);
        textBlock.setVariant(variant);
        return textBlock;
    }

    @Override
    public SDUIComponent createButton(String id, String title, String onPress) {
        SDUIComponent button = createComponent(SDUIComponent.TypeEnum.BUTTON);
        button.setId(id);
        button.setTitle(title);
        button.setSize(SDUIComponent.SizeEnum.SMALL);
        button.setOnPress(onPress);
        return button;
    }

    @Override
    public SDUIComponent createCard(String id, String title) {
        SDUIComponent card = createComponent(SDUIComponent.TypeEnum.CARD);
        card.setId(id);
        card.setTitle(title);
        card.setElevation(Integer.valueOf(1));
        card.setBorderRadius(Integer.valueOf(12));
        card.setPadding("16");
        card.setBackgroundColor("#FFFFFF");
        card.setChildren(new ArrayList<>());
        return card;
    }

    @Override
    public SDUIComponent createCard(String id, SDUIComponent titleComponent, SDUIComponent subtitleComponent) {
        SDUIComponent card = createComponent(SDUIComponent.TypeEnum.CARD);
        card.setId(id);
        card.setElevation(Integer.valueOf(1));
        card.setBorderRadius(Integer.valueOf(12));
        card.setPadding("16");
        card.setBackgroundColor("#FFFFFF");

        // Cria container para o conteúdo do card
        SDUIComponent contentContainer = createContainer(id + "-content")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .spacing(8);

        // Adiciona título se fornecido
        if (titleComponent != null) {
            contentContainer.getChildren().add(titleComponent);
        }

        // Adiciona subtítulo se fornecido
        if (subtitleComponent != null) {
            contentContainer.getChildren().add(subtitleComponent);
        }

        card.setChildren(List.of(contentContainer));
        return card;
    }

    @Override
    public SDUIComponent createImage(String id, String source, Integer width, Integer height) {
        SDUIComponent image = createComponent(SDUIComponent.TypeEnum.IMAGE);
        image.setId(id);
        image.setSource(source);
        image.setWidth(width);
        image.setHeight(height);
        image.setResizeMode(SDUIComponent.ResizeModeEnum.COVER);
        return image;
    }

    @Override
    public SDUIComponent createInput(String id, String placeholder, SDUIComponent.InputTypeEnum inputType) {
        SDUIComponent input = createComponent(SDUIComponent.TypeEnum.INPUT);
        input.setId(id);
        input.setPlaceholder(placeholder);
        input.setInputType(inputType);
        return input;
    }

    @Override
    public SDUIComponent createSpacer(String id, SDUIComponent.SizeEnum size) {
        SDUIComponent spacer = createComponent(SDUIComponent.TypeEnum.SPACER);
        spacer.setId(id);
        spacer.setSize(size);
        return spacer;
    }

    @Override
    public SDUIComponent createAvatar(String id, String source, Integer size) {
        SDUIComponent avatar = createComponent(SDUIComponent.TypeEnum.AVATAR);
        avatar.setId(id);
        avatar.setSource(source);
        avatar.setStyle(Map.of("size", size));
        return avatar;
    }
}