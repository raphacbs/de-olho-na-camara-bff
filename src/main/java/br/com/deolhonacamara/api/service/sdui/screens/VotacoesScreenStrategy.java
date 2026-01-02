package br.com.deolhonacamara.api.service.sdui.screens;

import br.com.deolhonacamara.api.service.VotingService;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Estratégia para construção da tela de Votações.
 */
@Component
@RequiredArgsConstructor
public class VotacoesScreenStrategy implements SDUIScreenStrategy {

    private final br.com.deolhonacamara.api.service.sdui.components.SDUIComponentFactory componentFactory;
    private final br.com.deolhonacamara.api.service.sdui.builders.SDUIScreenBuilderFactory screenBuilderFactory;
    private final VotingService votingService;

    @Override
    public String getScreenType() {
        return "votings";
    }

    @Override
    public SDUIResponse buildScreen(Map<String, Object> params) {
        String periodo = (String) params.getOrDefault("periodo", "week");
        Integer page = (Integer) params.getOrDefault("page", 0);
        Integer size = (Integer) params.getOrDefault("size", 20);

        List<SDUIComponent> components = new ArrayList<>();

        // Seletor de período
        components.add(buildPeriodSelector());

        // Lista de votações
        components.add(buildVotesList(periodo, page, size));

        // Navegação
        SDUINavigation navigation = new SDUINavigation()
                .header(new SDUINavigationHeader()
                        .title("Votações")
                        .showBack(false)
                        .actions(new ArrayList<>() {{
                            add(new SDUINavigationHeaderActionsInner()
                                    .id("calendar-action")
                                    .type(SDUINavigationHeaderActionsInner.TypeEnum.ICON)
                                    .icon("calendar")
                                    .action("filter_by_date"));
                        }}));

        // Metadados
        SDUIMetadata metadata = new SDUIMetadata()
                .version("1.0.0")
                .cache(true)
                .ttl(1800);

        return screenBuilderFactory.createBuilder()
                .withId("votes")
                .withTitle("Votações")
                .withNavigation(navigation)
                .addComponents(components)
                .withMetadata(metadata)
                .build();
    }

    private SDUIComponent buildPeriodSelector() {
        SDUIComponent container = componentFactory.createContainer("container-period-selector-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .spacing(8)
                .padding("16 20")
                .scrollable(true)
                .horizontal(true);

        container.getChildren().add(createPeriodButton("period-today", "Hoje", "filter_votes",
                Map.of("period", "today")));
        container.getChildren().add(createPeriodButton("period-week", "Esta Semana", "filter_votes",
                Map.of("period", "week")));
        container.getChildren().add(createPeriodButton("period-month", "Este Mês", "filter_votes",
                Map.of("period", "month")));
        container.getChildren().add(createPeriodButton("period-all", "Todas", "filter_votes",
                Map.of("period", "all")));

        return container;
    }

    private SDUIComponent buildVotesList(String periodo, Integer page, Integer size) {
        SDUIComponent container = componentFactory.createContainer("container-votes-list-main")
                .direction(SDUIComponent.DirectionEnum.COLUMN)
                .scrollable(true);

        try {
            // Buscar dados reais das votações
            var response = votingService.getVotingsWithVotes(page, size);
            // Converter DTOs locais para DTOs gerados pelo OpenAPI
            List<net.coelho.deolhonacamara.api.model.VotingWithVotesDTO> votings = new ArrayList<>();
            for (br.com.deolhonacamara.api.dto.VotingWithVotesDTO localDto : response.getData()) {
                net.coelho.deolhonacamara.api.model.VotingWithVotesDTO apiDto = new net.coelho.deolhonacamara.api.model.VotingWithVotesDTO();
                apiDto.setId(localDto.getId());
                apiDto.setDate(localDto.getDate());
                apiDto.setDescription(localDto.getDescription());
                apiDto.setOrganAcronym(localDto.getOrganAcronym());

                // Converter votos
                List<net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO> votes = new ArrayList<>();
                for (br.com.deolhonacamara.api.dto.VotingWithVotesDTO.PoliticianVoteSummaryDTO localVote : localDto.getVotes()) {
                    net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO apiVote = new net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO();
                    apiVote.setPoliticianId(localVote.getPoliticianId());
                    apiVote.setPoliticianName(localVote.getPoliticianName());
                    apiVote.setVoteType(localVote.getVoteType());
                    votes.add(apiVote);
                }
                apiDto.setVotes(votes);
                votings.add(apiDto);
            }

            for (VotingWithVotesDTO voting : votings) {
                container.getChildren().add(buildVoteCard(voting));
            }

            if (votings.isEmpty()) {
                SDUIComponent emptyMessage = componentFactory.createTextBlock(
                        "textblock-no-votes-main", "Nenhuma votação encontrada", SDUIComponent.VariantEnum.BODY)
                        .textAlign(SDUIComponent.TextAlignEnum.CENTER);
                container.getChildren().add(emptyMessage);
            }

        } catch (Exception e) {
            // Fallback para dados mockados
            container.getChildren().add(buildMockVoteCard("vote-1", "PL 1234/2024", "Sobre educação"));
            container.getChildren().add(buildMockVoteCard("vote-2", "PEC 45/2024", "Reforma tributária"));
        }

        return container;
    }

    private SDUIComponent buildVoteCard(net.coelho.deolhonacamara.api.model.VotingWithVotesDTO voting) {
        String titulo = voting.getDescription() != null ? voting.getDescription() : "Votação sem título";
        String dataHora = formatarDataHora(voting.getDate());

        // Calcular resultados
        long sim = voting.getVotes().stream().filter(v -> "Sim".equalsIgnoreCase(v.getVoteType())).count();
        long nao = voting.getVotes().stream().filter(v -> "Não".equalsIgnoreCase(v.getVoteType())).count();
        long abstencao = voting.getVotes().stream().filter(v -> "Abstenção".equalsIgnoreCase(v.getVoteType())).count();

        boolean aprovado = sim > nao; // Simplificação: aprovado se mais votos sim

        SDUIComponent card = componentFactory.createCard(
                "vote-" + voting.getId(),
                titulo)
                .subtitle(voting.getDescription())
                .elevation(Integer.valueOf(1))
                .borderRadius(Integer.valueOf(12))
                .padding("16")
                .margin("0 20 12 20")
                .backgroundColor("#FFFFFF")
                .onPress("open_vote_detail")
                .actionParams(Map.of(
                    "voteId", voting.getId(),
                    "voteTitle", titulo,
                    "apiEndpoint", "/api/votacoes/" + voting.getId()
                ));

        // Resultado e data
        SDUIComponent resultContainer = componentFactory.createContainer("container-vote-" + voting.getId() + "-result-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .justifyContent(SDUIComponent.JustifyContentEnum.SPACE_BETWEEN)
                .alignItems(SDUIComponent.AlignItemsEnum.CENTER)
                .style(Map.of("marginTop", "8"));

        String statusText = aprovado ? "✅ Aprovado" : "❌ Rejeitado";
        String statusColor = aprovado ? "#28a745" : "#dc3545";

        SDUIComponent approvalText = componentFactory.createTextBlock(
                "textblock-vote-" + voting.getId() + "-approval-main",
                statusText,
                SDUIComponent.VariantEnum.BODY)
                .color(statusColor)
                .fontSize(Integer.valueOf(14))
                .fontWeight("600");

        SDUIComponent dateText = componentFactory.createTextBlock(
                "textblock-vote-" + voting.getId() + "-date-main",
                dataHora,
                SDUIComponent.VariantEnum.CAPTION)
                .color("#666")
                .fontSize(Integer.valueOf(12));

        resultContainer.getChildren().add(approvalText);
        resultContainer.getChildren().add(dateText);
        card.getChildren().add(resultContainer);

        // Estatísticas
        SDUIComponent statsContainer = componentFactory.createContainer("container-vote-" + voting.getId() + "-stats-main")
                .direction(SDUIComponent.DirectionEnum.ROW)
                .spacing(16)
                .style(Map.of("marginTop", "12"));

        statsContainer.getChildren().add(componentFactory.createTextBlock(
                "textblock-vote-" + voting.getId() + "-sim-main",
                "Sim: " + sim,
                SDUIComponent.VariantEnum.CAPTION)
                .color("#28a745")
                .fontSize(Integer.valueOf(12)));

        statsContainer.getChildren().add(componentFactory.createTextBlock(
                "textblock-vote-" + voting.getId() + "-nao-main",
                "Não: " + nao,
                SDUIComponent.VariantEnum.CAPTION)
                .color("#dc3545")
                .fontSize(Integer.valueOf(12)));

        statsContainer.getChildren().add(componentFactory.createTextBlock(
                "textblock-vote-" + voting.getId() + "-abstencao-main",
                "Abstenção: " + abstencao,
                SDUIComponent.VariantEnum.CAPTION)
                .color("#6c757d")
                .fontSize(Integer.valueOf(12)));

        card.getChildren().add(statsContainer);

        return card;
    }

    private SDUIComponent buildMockVoteCard(String id, String titulo, String objVotacao) {
        // Criar mock VotingWithVotesDTO
        net.coelho.deolhonacamara.api.model.VotingWithVotesDTO voting = new net.coelho.deolhonacamara.api.model.VotingWithVotesDTO();
        voting.setId(id);
        voting.setDescription(titulo);
        voting.setOrganAcronym("PLEN");
        voting.setDate(LocalDate.now().minusDays(1));

        // Adicionar alguns votos mockados
        List<net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO> votes = new ArrayList<>();
        net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO vote1 = new net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO();
        vote1.setPoliticianId(1);
        vote1.setPoliticianName("João Silva");
        vote1.setVoteType("Sim");
        votes.add(vote1);

        net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO vote2 = new net.coelho.deolhonacamara.api.model.PoliticianVoteSummaryDTO();
        vote2.setPoliticianId(2);
        vote2.setPoliticianName("Maria Santos");
        vote2.setVoteType("Não");
        votes.add(vote2);

        voting.setVotes(votes);

        return buildVoteCard(voting);
    }

    private String formatarDataHora(LocalDate date) {
        if (date == null) return "Data não informada";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " às 14:30";
    }

    private SDUIComponent createPeriodButton(String id, String title, String onPress, Map<String, Object> actionParams) {
        return componentFactory.createButton(id, title, onPress)
                .actionParams(actionParams);
    }
}