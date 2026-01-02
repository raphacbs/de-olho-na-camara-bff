package br.com.deolhonacamara.api.service.sdui.components;

import br.com.deolhonacamara.api.model.PropositionType;
import br.com.deolhonacamara.api.model.screen.PropositionScreen;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.SDUIComponent;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Factory para criação centralizada de cards de proposições.
 * Elimina código duplicado entre diferentes estratégias de tela.
 */
@Component
@RequiredArgsConstructor
public class PropositionCardFactory {

    private final SDUIComponentFactory componentFactory;

    /**
     * Cria um card de proposição para telas que usam PropositionScreen (mais completo).
     * Usado principalmente na tela Home.
     */
    public SDUIComponent createPropositionCard(PropositionScreen proposition) {
        return createPropositionCard(proposition, "10");
    }

    /**
     * Cria um card de proposição para telas que usam PropositionScreen com margem customizada.
     */
    public SDUIComponent createPropositionCard(PropositionScreen proposition, String margin) {
        // Cria componente de título customizado (com tipo no topo direito)
        SDUIComponent titleComponent = createCustomTitleComponent(proposition);

        // Cria componente de subtítulo customizado
        SDUIComponent subtitleComponent = createCustomSubtitleComponent(proposition);

        SDUIComponent card = componentFactory.createCard(
                "proposition-" + proposition.getId(),
                titleComponent,
                subtitleComponent)
                .onPress("open_proposition_detail")
                .margin(margin)
                .actionParams(Map.of(
                        "propositionId", proposition.getId(),
                        "propositionTitle", proposition.getType() + " " + proposition.getNumber() + "/" + proposition.getYear(),
                        "apiEndpoint", "/api/proposicoes/" + proposition.getId()));

        // Acessa o container interno criado pelo createCard customizado
        SDUIComponent contentContainer = card.getChildren().get(0);

        // Container inferior com autores e data lado a lado
        SDUIComponent bottomContainer = componentFactory.createContainer(
                        "container-prop-" + proposition.getId() + "-bottom")
                        .direction(SDUIComponent.DirectionEnum.ROW)
                        .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                        .alignItems(SDUIComponent.AlignItemsEnum.FLEX_END)
                        .style(Map.of("marginTop", "8"));

        // Autores da proposição (lado esquerdo)
        if (proposition.getAuthors() != null && !proposition.getAuthors().isEmpty()) {
                SDUIComponent authorsContainer = componentFactory.createContainer(
                                "container-prop-" + proposition.getId() + "-authors")
                                .direction(SDUIComponent.DirectionEnum.COLUMN)
                                .spacing(2);

                SDUIComponent authorsTitle = componentFactory.createTextBlock(
                                "textblock-prop-" + proposition.getId() + "-authors-title",
                                "Apresentado por:",
                                SDUIComponent.VariantEnum.CAPTION)
                                .color("#666")
                                .fontSize(Integer.valueOf(11))
                                .fontWeight("600")
                                .style(Map.of("marginBottom", "4"));

                authorsContainer.getChildren().add(authorsTitle);

                for (var author : proposition.getAuthors()) {
                        SDUIComponent authorContainer = componentFactory.createContainer(
                                        "container-prop-" + proposition.getId() + "-author-" + author.getId())
                                        .direction(SDUIComponent.DirectionEnum.ROW)
                                        .alignItems(SDUIComponent.AlignItemsEnum.CENTER)
                                        .spacing(1);

                        // Container para informações do autor (nome e partido)
                        SDUIComponent authorInfoContainer = componentFactory.createContainer(
                                        "container-prop-" + proposition.getId() + "-author-info-"
                                                        + author.getId())
                                        .direction(SDUIComponent.DirectionEnum.COLUMN)
                                        .alignItems(SDUIComponent.AlignItemsEnum.FLEX_START);

                        // Nome do autor
                        SDUIComponent authorNameText = componentFactory.createTextBlock(
                                        "textblock-prop-" + proposition.getId() + "-author-name-"
                                                        + author.getId(),
                                        author.getName(),
                                        SDUIComponent.VariantEnum.BODY)
                                        .color("#666")
                                        .fontSize(Integer.valueOf(12))
                                        .style(Map.of("marginLeft", "5"))
                                        .fontWeight("500");

                        // Partido do autor
                        String authorParty = author.getParty() != null ? author.getParty()+"/"+author.getState() : "Sem partido - Sem estado";
                        SDUIComponent authorPartyText = componentFactory.createTextBlock(
                                        "textblock-prop-" + proposition.getId() + "-author-party-"
                                                        + author.getId(),
                                        authorParty,
                                        SDUIComponent.VariantEnum.CAPTION)
                                        .color("#999")
                                        .style(Map.of("marginLeft", "5", "marginTop", "-3"))
                                        .fontSize(Integer.valueOf(10));

                        authorInfoContainer.getChildren().add(authorNameText);
                        authorInfoContainer.getChildren().add(authorPartyText);

                        // Foto do autor (à direita)
                        if (author.getPhotoUrl() != null && !author.getPhotoUrl().isEmpty()) {
                                SDUIComponent authorImage = componentFactory.createAvatar(
                                                        "image-prop-" + proposition.getId() + "-author-photo-"
                                                                        + author.getId(),
                                                        author.getPhotoUrl(),
                                                        Integer.valueOf(32));

                                authorContainer.getChildren().add(authorImage);
                        }

                        // Adicionar informações do autor (nome e partido à esquerda)
                        authorContainer.getChildren().add(authorInfoContainer);

                        authorsContainer.getChildren().add(authorContainer);
                }

                bottomContainer.getChildren().add(authorsContainer);
        }

        // Status e data lado a lado (lado direito)
        SDUIComponent statusDateContainer = componentFactory.createContainer(
                        "container-prop-" + proposition.getId() + "-status-date")
                        .direction(SDUIComponent.DirectionEnum.COLUMN)
                        .alignItems(SDUIComponent.AlignItemsEnum.FLEX_END)
                        .spacing(4);

        // Status da tramitação
        if (proposition.getStatusTramitationDescription() != null && !proposition.getStatusTramitationDescription().trim().isEmpty()) {
                String statusColor = getStatusColor(proposition.getStatusTramitationDescription());
                SDUIComponent statusText = componentFactory.createTextBlock(
                                "textblock-prop-" + proposition.getId() + "-status-main",
                                proposition.getStatusTramitationDescription(),
                                SDUIComponent.VariantEnum.CAPTION)
                                .color(statusColor)
                                .fontSize(Integer.valueOf(11))
                                .fontWeight("600")
                                .textAlign(SDUIComponent.TextAlignEnum.RIGHT)
                                .style(Map.of("textTransform", "uppercase", "letterSpacing", "0.5px"));

                statusDateContainer.getChildren().add(statusText);
        }

        // Data de apresentação
        if (proposition.getPresentationDate() != null) {
                SDUIComponent dateText = componentFactory.createTextBlock(
                                "textblock-prop-" + proposition.getId() + "-date-main",
                                "Apresentada em " + proposition.getPresentationDate().format(
                                                DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                SDUIComponent.VariantEnum.CAPTION)
                                .color("#666")
                                .fontSize(Integer.valueOf(10))
                                .textAlign(SDUIComponent.TextAlignEnum.RIGHT);

                statusDateContainer.getChildren().add(dateText);
        }

        if (!statusDateContainer.getChildren().isEmpty()) {
                bottomContainer.getChildren().add(statusDateContainer);
        }

        // Adicionar container inferior ao contentContainer apenas se tiver filhos
        if (!bottomContainer.getChildren().isEmpty()) {
                contentContainer.getChildren().add(bottomContainer);
        }

        return card;
    }

    /**
     * Cria um card de proposição para telas que usam PropositionDto (mais simples).
     * Usado principalmente na tela de Proposições.
     */
    public SDUIComponent createPropositionCard(net.coelho.deolhonacamara.api.model.PropositionDto proposition) {
        String tipo = proposition.getType() != null ? proposition.getType() : "PL";
        String numero = proposition.getNumber() != null ? proposition.getNumber().toString() : "0";
        String ano = proposition.getYear() != null ? proposition.getYear().toString() : "2024";
        String ementa = proposition.getSummary() != null ? proposition.getSummary() : "Sem descrição";

        String statusDescricao = proposition.getStatusTramitationDescription() != null ?
                                proposition.getStatusTramitationDescription() : "Status não informado";

        String statusColor = getStatusColor(statusDescricao);
        String autor = "Autor não informado"; // Informação não disponível no DTO simplificado

        String dataApresentacao = proposition.getPresentationDate() != null ?
                                proposition.getPresentationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                                "Data não informada";

        SDUIComponent card = componentFactory.createCard(
                "proposition-" + proposition.getId(),
                tipo + " " + numero + "/" + ano)
                .subtitle(ementa)
                .elevation(Integer.valueOf(1))
                .borderRadius(Integer.valueOf(12))
                .padding("16")
                .margin("0 20 12 20")
                .backgroundColor("#FFFFFF")
                .onPress("open_proposition_detail")
                .actionParams(Map.of(
                    "propositionId", proposition.getId(),
                    "propositionType", tipo,
                    "year", ano,
                    "title", ementa,
                    "status", statusDescricao,
                    "apiEndpoint", "/api/proposicoes/" + proposition.getId()
                ));

        // Status e data
        SDUIComponent statusContainer = componentFactory.createContainer(
                "container-proposition-" + proposition.getId() + "-status-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                .alignItems(SDUIComponent.AlignItemsEnum.CENTER);

        SDUIComponent statusText = componentFactory.createTextBlock(
                "textblock-proposition-" + proposition.getId() + "-status-main",
                statusDescricao,
                SDUIComponent.VariantEnum.CAPTION)
                .color(statusColor)
                .fontSize(Integer.valueOf(12))
                .fontWeight("600");

        SDUIComponent dateText = componentFactory.createTextBlock(
                "textblock-proposition-" + proposition.getId() + "-date-main",
                dataApresentacao,
                SDUIComponent.VariantEnum.CAPTION)
                .color("#666")
                .fontSize(Integer.valueOf(12));

        statusContainer.getChildren().add(statusText);
        statusContainer.getChildren().add(dateText);
        card.getChildren().add(statusContainer);

        // Autor
        SDUIComponent authorText = componentFactory.createTextBlock(
                "textblock-proposition-" + proposition.getId() + "-author-main",
                "Autor: " + autor,
                SDUIComponent.VariantEnum.BODY)
                .color("#666")
                .fontSize(Integer.valueOf(14))
                .style(Map.of("marginTop", "8"));

        card.getChildren().add(authorText);

        return card;
    }

    private SDUIComponent createCustomTitleComponent(PropositionScreen proposition) {
        // Header do card com título e tipo lado a lado
        SDUIComponent cardHeader = componentFactory.createContainer(
                        "container-prop-" + proposition.getId() + "-header")
                        .direction(SDUIComponent.DirectionEnum.ROW)
                        .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                        .alignItems(SDUIComponent.AlignItemsEnum.CENTER);

        // Título à esquerda
        SDUIComponent titleText = componentFactory.createTextBlock(
                        "textblock-prop-" + proposition.getId() + "-title",
                        proposition.getType() + " " + proposition.getNumber() + "/" + proposition.getYear(),
                        SDUIComponent.VariantEnum.TITLE)
                        .color("#1a1a1a")
                        .fontSize(Integer.valueOf(16))
                        .fontWeight("600");

        // Tipo à direita (mesmo estilo do "Apresentado por")
        String typeDescription = getPropositionTypeDescription(proposition.getType());
        SDUIComponent typeText = componentFactory.createTextBlock(
                        "textblock-prop-" + proposition.getId() + "-type",
                        typeDescription,
                        SDUIComponent.VariantEnum.CAPTION)
                        .color("#666")
                        .fontSize(Integer.valueOf(11))
                        .fontWeight("600")
                        .textAlign(SDUIComponent.TextAlignEnum.RIGHT);

        cardHeader.getChildren().add(titleText);
        cardHeader.getChildren().add(typeText);

        return cardHeader;
    }

    private SDUIComponent createCustomSubtitleComponent(PropositionScreen proposition) {
        String subtitle = proposition.getSummary() != null ? proposition.getSummary() : "Sem resumo disponível";

        return componentFactory.createTextBlock(
                        "textblock-prop-" + proposition.getId() + "-subtitle",
                        subtitle,
                        SDUIComponent.VariantEnum.BODY)
                        .color("#666")
                        .fontSize(Integer.valueOf(14))
                        .style(Map.of("marginBottom", "8"));
    }

    private String getPropositionTypeDescription(String type) {
        return PropositionType.getDescription(type);
    }


    private String getStatusColor(String status) {
        if (status == null) return "#666";

        String normalizedStatus = status.trim().toLowerCase();

        // Status de aprovação/sucesso (verde)
        if (normalizedStatus.contains("aprovada") || normalizedStatus.contains("aprovado") ||
            normalizedStatus.contains("sancionad") || normalizedStatus.contains("transformado em norma jurídica") ||
            normalizedStatus.contains("deferido o requerimento") || normalizedStatus.contains("apresentação de requerimento") ||
            normalizedStatus.contains("aprovado de urgência") || normalizedStatus.contains("ratificação de parecer") ||
            normalizedStatus.contains("aprovado o parecer") || normalizedStatus.contains("apresentação de requerimento") ||
            normalizedStatus.contains("aprovado requerimento") || normalizedStatus.contains("aprovado de requerimento")) {
            return "#4CAF50"; // Verde
        }
        // Status de tramitação/analise (laranja)
        else if (normalizedStatus.contains("tramitação") || normalizedStatus.contains("análise") ||
                 normalizedStatus.contains("parecer") || normalizedStatus.contains("distribuição") ||
                 normalizedStatus.contains("recebimento") || normalizedStatus.contains("encaminhamento") ||
                 normalizedStatus.contains("designação") || normalizedStatus.contains("relator") ||
                 normalizedStatus.contains("leitura") || normalizedStatus.contains("publicação") ||
                 normalizedStatus.contains("apensação") || normalizedStatus.contains("despacho") ||
                 normalizedStatus.contains("sessão solene") || normalizedStatus.contains("votação") ||
                 normalizedStatus.contains("encerramento") || normalizedStatus.contains("abertura de prazo") ||
                 normalizedStatus.contains("notificação") || normalizedStatus.contains("comunicação") ||
                 normalizedStatus.contains("providência interna") || normalizedStatus.contains("arquivamento de ofício")) {
            return "#FF9800"; // Laranja
        }
        // Status de rejeição/arquivamento (vermelho)
        else if (normalizedStatus.contains("rejeitad") || normalizedStatus.contains("rejeição") ||
                 normalizedStatus.contains("arquivad") || normalizedStatus.contains("arquivamento") ||
                 normalizedStatus.contains("prejudicado") || normalizedStatus.contains("prejudicialidade") ||
                 normalizedStatus.contains("retirada pelo autor") || normalizedStatus.contains("retirada de pauta") ||
                 normalizedStatus.contains("retirada de requerimento") || normalizedStatus.contains("saída de relator") ||
                 normalizedStatus.contains("devolução ao autor") || normalizedStatus.contains("devolução à ccp")) {
            return "#F44336"; // Vermelho
        }
        // Status neutros/default (cinza)
        else {
            return "#666"; // Cinza
        }
    }
}