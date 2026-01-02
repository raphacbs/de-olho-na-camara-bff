package br.com.deolhonacamara.api.service.sdui.screens;

import br.com.deolhonacamara.api.model.screen.PropositionScreen;
import br.com.deolhonacamara.api.service.PropositionService;
import br.com.deolhonacamara.api.service.sdui.components.PropositionCardFactory;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Estratégia para construção da tela Home.
 * Busca dados reais da base e constrói a interface usando factories.
 */
@Component
@RequiredArgsConstructor
public class HomeScreenStrategy implements SDUIScreenStrategy {

        private final br.com.deolhonacamara.api.service.sdui.components.SDUIComponentFactory componentFactory;
        private final br.com.deolhonacamara.api.service.sdui.builders.SDUIScreenBuilderFactory screenBuilderFactory;
        private final PropositionService propositionService;
        private final PropositionCardFactory propositionCardFactory;

        @Override
        public String getScreenType() {
                return "home";
        }

        @Override
        public SDUIResponse buildScreen(Map<String, Object> params) {
                List<SDUIComponent> components = new ArrayList<>();

                // Header container com gradiente brasileiro
                components.add(buildHeaderContainer());

                // Spacer após header
                components.add(componentFactory.createSpacer("spacer-header-main", SDUIComponent.SizeEnum.LARGE));

                // Seção de proposições
                components.add(buildPropositionsSection());

                // Seção de estatísticas
                components.add(buildStatsSection());

                // Spacer final
                components.add(componentFactory.createSpacer("spacer-bottom-main", SDUIComponent.SizeEnum.LARGE));

                // Ações disponíveis
                List<SDUIAction> actions = new ArrayList<>();
                actions.add(new SDUIAction()
                                .type(SDUIAction.TypeEnum.NAVIGATE)
                                .payload(Map.of("screen", "propositions")));

                // Metadados
                SDUIMetadata metadata = new SDUIMetadata()
                                .version("1.0.0")
                                .cache(true)
                                .ttl(3600);

                return screenBuilderFactory.createBuilder()
                                .withId("home")
                                .withTitle("De Olho na Câmara")
                                .addComponents(components)
                                .withActions(actions)
                                .withMetadata(metadata)
                                .build();
        }

        private SDUIComponent buildHeaderContainer() {
                SDUIComponent headerContainer = componentFactory.createContainer("container-header-main")
                                .direction(SDUIComponent.DirectionEnum.COLUMN)
                                .padding("0")
                                .sticky(true)
                                .backgroundColor("#009C3B")
                                .style(Map.of(
                                                "paddingTop", "20",
                                                "paddingBottom", "30",
                                                "borderBottomLeftRadius", "20px",
                                                "borderBottomRightRadius", "20px"));

                // Welcome text
                SDUIComponent welcomeText = componentFactory.createTextBlock(
                                "welcome-text", "🇧🇷 Bem-vindo ao", SDUIComponent.VariantEnum.BODY)
                                .color("#FFFFFF")
                                .textAlign(SDUIComponent.TextAlignEnum.CENTER)
                                .style(Map.of("marginBottom", "8"));

                // Title
                SDUIComponent title = componentFactory.createTextBlock(
                                "title-main", "De Olho na Câmara", SDUIComponent.VariantEnum.TITLE)
                                .color("#FFFFFF")
                                .fontSize(Integer.valueOf(28))
                                .fontWeight("700")
                                .textAlign(SDUIComponent.TextAlignEnum.CENTER)
                                .style(Map.of( "letterSpacing", "-0.5"));

                // Subtitle
                SDUIComponent subtitle = componentFactory.createTextBlock(
                                "subtitle-main", "Acompanhe as atividades legislativas", SDUIComponent.VariantEnum.BODY)
                                .color("#E8F5E8")
                                .textAlign(SDUIComponent.TextAlignEnum.CENTER)
                                .fontSize(Integer.valueOf(14));

                headerContainer.getChildren().add(welcomeText);
                headerContainer.getChildren().add(title);
                headerContainer.getChildren().add(subtitle);

                return headerContainer;
        }

        private SDUIComponent buildPropositionsSection() {
                SDUIComponent section = componentFactory.createContainer("container-propositions-main")
                                .direction(SDUIComponent.DirectionEnum.COLUMN)
                                .spacing(20)
                                .padding("0 20");

                // Header da seção
                SDUIComponent header = componentFactory.createContainer("container-propositions-header-main")
                                .direction(SDUIComponent.DirectionEnum.ROW)
                                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                                .alignItems(SDUIComponent.AlignItemsEnum.CENTER);

                SDUIComponent title = componentFactory.createTextBlock(
                                "textblock-propositions-title-main", "Últimas Propostas",
                                SDUIComponent.VariantEnum.TITLE)
                                .color("#1a1a1a")
                                .fontSize(Integer.valueOf(20))
                                .margin("10")
                                .fontWeight("600");

                SDUIComponent viewAllBtn = componentFactory.createButton(
                                "view-all-btn", "Ver todas", "navigate_propositions");

                header.getChildren().add(title);
                header.getChildren().add(viewAllBtn);
                section.getChildren().add(header);

                // Buscar e adicionar proposições
                List<PropositionScreen> latestPropositions = propositionService.getLatestPropositionsScreen(5); // Limitar
                                                                                                                // a 3
                                                                                                                // para
                                                                                                                // a
                                                                                                                // tela
                                                                                                                // inicial
                for (PropositionScreen proposition : latestPropositions) {
                        section.getChildren().add(buildPropositionCard(proposition));
                }

                // Mensagem caso não haja proposições
                if (latestPropositions.isEmpty()) {
                        SDUIComponent emptyMessage = componentFactory.createTextBlock(
                                        "textblock-no-propositions-main", "Nenhuma proposição encontrada",
                                        SDUIComponent.VariantEnum.BODY)
                                        .textAlign(SDUIComponent.TextAlignEnum.CENTER)
                                        .color("#666");
                        section.getChildren().add(emptyMessage);
                }

                return section;
        }

        private SDUIComponent buildStatsSection() {
                SDUIComponent section = componentFactory.createContainer("container-stats-main")
                                .direction(SDUIComponent.DirectionEnum.COLUMN)
                                .spacing(16)
                                .padding("0 20");

                // Título da seção
                SDUIComponent title = componentFactory.createTextBlock(
                                "textblock-stats-title-main", "📊 Estatísticas da Semana",
                                SDUIComponent.VariantEnum.TITLE)
                                .color("#1a1a1a")
                                .fontSize(Integer.valueOf(20))
                                .fontWeight("600")
                                .style(Map.of("marginBottom", "8"));

                section.getChildren().add(title);

                // Grid de estatísticas
                SDUIComponent statsGrid = componentFactory.createContainer("container-stats-grid-main")
                                .direction(SDUIComponent.DirectionEnum.ROW)
                                .spacing(16)
                                .scrollable(true)
                                .horizontal(true)
                                .style(Map.of("paddingHorizontal", "20"));

                // Buscar dados reais das proposições
                List<PropositionScreen> latestPropositions = propositionService.getLatestPropositionsScreen(5);

                // Cards de estatísticas
                statsGrid.getChildren().add(buildStatsCard("Proposições", String.valueOf(latestPropositions.size()),
                                "Novas esta semana"));
                statsGrid.getChildren().add(buildStatsCard("Votações", "8", "Realizadas"));
                statsGrid.getChildren().add(buildStatsCard("Deputados", "513", "Ativos"));

                section.getChildren().add(statsGrid);

                return section;
        }

        private SDUIComponent buildStatsCard(String title, String value, String subtitle) {
                SDUIComponent card = componentFactory
                                .createCard("card-stats-" + title.toLowerCase().replace(" ", "-") + "-main", title)
                                .subtitle(subtitle)
                                .elevation(Integer.valueOf(2))
                                .borderRadius(Integer.valueOf(12))
                                .padding("16")
                                .backgroundColor("#FFFFFF");

                SDUIComponent valueText = componentFactory.createTextBlock(
                                "textblock-stats-value-" + title.toLowerCase().replace(" ", "-") + "-main", value,
                                SDUIComponent.VariantEnum.TITLE)
                                .color("#009C3B")
                                .fontSize(Integer.valueOf(24))
                                .fontWeight("700")
                                .textAlign(SDUIComponent.TextAlignEnum.CENTER)
                                .style(Map.of("marginBottom", "4"));

                card.getChildren().add(valueText);

                return card;
        }

        private SDUIComponent buildPropositionCard(PropositionScreen proposition) {
                return propositionCardFactory.createPropositionCard(proposition);
        }

}