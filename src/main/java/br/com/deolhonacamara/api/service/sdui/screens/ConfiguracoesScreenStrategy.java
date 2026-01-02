package br.com.deolhonacamara.api.service.sdui.screens;

import net.coelho.deolhonacamara.api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Estratégia para construção da tela de Configurações.
 */
@Component
public class ConfiguracoesScreenStrategy implements SDUIScreenStrategy {

    private final br.com.deolhonacamara.api.service.sdui.components.SDUIComponentFactory componentFactory;
    private final br.com.deolhonacamara.api.service.sdui.builders.SDUIScreenBuilderFactory screenBuilderFactory;

    public ConfiguracoesScreenStrategy(br.com.deolhonacamara.api.service.sdui.components.SDUIComponentFactory componentFactory, br.com.deolhonacamara.api.service.sdui.builders.SDUIScreenBuilderFactory screenBuilderFactory) {
        this.componentFactory = componentFactory;
        this.screenBuilderFactory = screenBuilderFactory;
    }

    @Override
    public String getScreenType() {
        return "settings";
    }

    @Override
    public SDUIResponse buildScreen(Map<String, Object> params) {
        List<SDUIComponent> components = new ArrayList<>();

        // Seção de perfil
        components.add(buildProfileSection());

        // Spacer
        components.add(componentFactory.createSpacer("spacer-profile-app-main", SDUIComponent.SizeEnum.MEDIUM));

        // Seção do aplicativo
        components.add(buildAppSection());

        // Spacer
        components.add(componentFactory.createSpacer("spacer-app-about-main", SDUIComponent.SizeEnum.MEDIUM));

        // Seção sobre
        components.add(buildAboutSection());

        // Ações
        List<SDUIAction> actions = new ArrayList<>();
        actions.add(new SDUIAction()
                .type(SDUIAction.TypeEnum.CUSTOM)
                .payload(Map.of(
                    "action", "update_user_settings",
                    "endpoint", "/api/user/settings"
                )));

        // Metadados
        SDUIMetadata metadata = new SDUIMetadata()
                .version("1.0.0")
                .cache(false)
                .ttl(0);

        return screenBuilderFactory.createBuilder()
                .withId("settings")
                .withTitle("Configurações")
                .addComponents(components)
                .withActions(actions)
                .withMetadata(metadata)
                .build();
    }

    private SDUIComponent buildProfileSection() {
        SDUIComponent section = componentFactory.createContainer("container-profile-main")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .padding("20");

        // Título da seção
        SDUIComponent title = componentFactory.createTextBlock(
                "textblock-profile-title-main", "Perfil", SDUIComponent.VariantEnum.TITLE)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(20))
                .fontWeight("600")
                .style(Map.of("marginBottom", "16"));

        section.getChildren().add(title);

        // Configuração de notificações
        SDUIComponent notificationsSetting = componentFactory.createContainer("container-notifications-setting-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                .alignItems(SDUIComponent.AlignItemsEnum.CENTER)
                .padding("12 0");

        SDUIComponent notificationsLabel = componentFactory.createTextBlock(
                "notifications-label", "Notificações", SDUIComponent.VariantEnum.BODY)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(16));

        SDUIComponent notificationsToggle = componentFactory.createButton(
                "notifications-toggle", "Ativado", "toggle_notifications");

        notificationsSetting.getChildren().add(notificationsLabel);
        notificationsSetting.getChildren().add(notificationsToggle);
        section.getChildren().add(notificationsSetting);

        return section;
    }

    private SDUIComponent buildAppSection() {
        SDUIComponent section = componentFactory.createContainer("container-app-main")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .padding("20");

        // Título da seção
        SDUIComponent title = componentFactory.createTextBlock(
                "textblock-app-title-main", "Aplicativo", SDUIComponent.VariantEnum.TITLE)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(20))
                .fontWeight("600")
                .style(Map.of("marginBottom", "16"));

        section.getChildren().add(title);

        // Configuração de tema
        SDUIComponent themeSetting = componentFactory.createContainer("container-theme-setting-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                .alignItems(SDUIComponent.AlignItemsEnum.CENTER)
                .padding("12 0");

        SDUIComponent themeLabel = componentFactory.createTextBlock(
                "theme-label", "Tema", SDUIComponent.VariantEnum.BODY)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(16));

        SDUIComponent themeSelector = componentFactory.createButton(
                "theme-selector", "Sistema", "select_theme");

        themeSetting.getChildren().add(themeLabel);
        themeSetting.getChildren().add(themeSelector);
        section.getChildren().add(themeSetting);

        return section;
    }

    private SDUIComponent buildAboutSection() {
        SDUIComponent section = componentFactory.createContainer("container-about-main")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .padding("20");

        // Título da seção
        SDUIComponent title = componentFactory.createTextBlock(
                "textblock-about-title-main", "Sobre", SDUIComponent.VariantEnum.TITLE)
                .color("#1a1a1a")
                .fontSize(Integer.valueOf(20))
                .fontWeight("600")
                .style(Map.of("marginBottom", "16"));

        section.getChildren().add(title);

        // Versão do app
        SDUIComponent versionInfo = componentFactory.createTextBlock(
                "version-info", "Versão 1.0.0", SDUIComponent.VariantEnum.CAPTION)
                .color("#666")
                .fontSize(Integer.valueOf(14));

        section.getChildren().add(versionInfo);

        return section;
    }
}