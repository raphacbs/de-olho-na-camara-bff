package br.com.deolhonacamara.sdui.service;

import br.com.deolhonacamara.api.model.PoliticianDTO;
import br.com.deolhonacamara.api.model.PropositionDTO;
import br.com.deolhonacamara.api.service.PropositionService;
import br.com.deolhonacamara.sdui.model.ComponentAction;
import br.com.deolhonacamara.sdui.model.properties.AuthorCardListProperties;
import br.com.deolhonacamara.sdui.model.properties.AuthorCardProperties;
import br.com.deolhonacamara.sdui.model.properties.DetailSectionProperties;
import br.com.deolhonacamara.sdui.model.properties.PropositionCardListProperties;
import br.com.deolhonacamara.sdui.model.properties.PropositionCardProperties;
import br.com.deolhonacamara.sdui.model.properties.PropositionDetailHeaderProperties;
import br.com.deolhonacamara.sdui.model.properties.TextLinkSectionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.model.HomeScreenResponse;
import net.coelho.deolhonacamara.api.model.PropositionDto;
import net.coelho.deolhonacamara.api.model.ScreenComponent;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PropositionsScreenService {

    private static final String ACTION_NAVIGATE = "NAVIGATE";
    private static final String SCREEN_VERSION = "1.0";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PropositionService propositionService;

    public HomeScreenResponse buildPropositionsScreen(Integer politicianId, List<String> types,
                                                      List<String> statuses, LocalDate startDate,
                                                      LocalDate endDate, int page, int size) {
        log.info("Building SDUI propositions screen");

        Map<String, Object> filters = new HashMap<>();
        filters.put("page", page);
        filters.put("size", size);
        if (politicianId != null) filters.put("politicianId", politicianId);
        if (types != null && !types.isEmpty()) filters.put("types", types);
        if (statuses != null && !statuses.isEmpty()) filters.put("statuses", statuses);
        if (startDate != null) filters.put("startDate", startDate);
        if (endDate != null) filters.put("endDate", endDate);

        var propositionsPage = propositionService.getFilteredPropositions(filters);
        var propositions = propositionsPage.getData();

        List<PropositionCardProperties> cards = propositions == null ? List.of() :
                propositions.stream()
                        .map(this::toPropositionCard)
                        .collect(Collectors.toList());

        var cardListProps = PropositionCardListProperties.builder()
                .items(cards)
                .total(propositionsPage.getTotal())
                .currentPage(propositionsPage.getPage())
                .totalPages(propositionsPage.getTotalPages())
                .build();

        var response = new HomeScreenResponse();
        response.setScreenId("propositions");
        response.setVersion(SCREEN_VERSION);
        response.setComponents(List.of(
                component("propositions-list", "PROPOSITION_CARD_LIST", cardListProps)
        ));
        return response;
    }

    public HomeScreenResponse buildPropositionDetailScreen(Integer id) {
        log.info("Building SDUI proposition detail screen for id {}", id);

        PropositionDTO proposition = propositionService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Proposition not found: " + id));

        List<ScreenComponent> components = new ArrayList<>();

        String typeLabel = buildTypeLabel(proposition.getType(), proposition.getNumber(),
                proposition.getYear());
        String presentationDateStr = proposition.getPresentationDate() != null
                ? "Apresentada em: " + proposition.getPresentationDate().format(DATE_FORMATTER)
                : "";

        components.add(component("proposition-header", "PROPOSITION_DETAIL_HEADER",
                PropositionDetailHeaderProperties.builder()
                        .title(typeLabel)
                        .presentationDate(presentationDateStr)
                        .build()));

        if (proposition.getSummary() != null && !proposition.getSummary().isBlank()) {
            components.add(component("proposition-ementa", "DETAIL_SECTION",
                    DetailSectionProperties.builder()
                            .title("Ementa")
                            .content(proposition.getSummary())
                            .build()));
        }

        String situacao = buildSituacaoText(proposition);
        components.add(component("proposition-situacao", "DETAIL_SECTION",
                DetailSectionProperties.builder()
                        .title("Situação")
                        .content(situacao != null ? situacao : "Não informada")
                        .build()));

        String apreciacao = proposition.getStatusAppreciation();
        components.add(component("proposition-apreciacao", "DETAIL_SECTION",
                DetailSectionProperties.builder()
                        .title("Apreciação")
                        .content(apreciacao != null && !apreciacao.isBlank() ? apreciacao : "Indefinida")
                        .build()));

        String despacho = proposition.getStatusDispatch();
        if (despacho != null && !despacho.isBlank()) {
            components.add(component("proposition-despacho", "DETAIL_SECTION",
                    DetailSectionProperties.builder()
                            .title("Despacho")
                            .content(despacho)
                            .build()));
        }

        String urlInteiroTeor = proposition.getUrlInteiroTeor();
        if (urlInteiroTeor != null && !urlInteiroTeor.isBlank()) {
            components.add(component("proposition-inteiro-teor", "TEXT_LINK_SECTION",
                    TextLinkSectionProperties.builder()
                            .title("Inteiro Teor")
                            .linkLabel("Visualizar documento")
                            .url(urlInteiroTeor)
                            .build()));
        }

        List<PoliticianDTO> authors = proposition.getPoliticians();
        if (authors != null && !authors.isEmpty()) {
            List<AuthorCardProperties> authorCards = authors.stream()
                    .map(a -> AuthorCardProperties.builder()
                            .id(a.getId() != null ? a.getId().intValue() : null)
                            .photoUrl(a.getPhotoUrl())
                            .name(a.getName())
                            .partyState(buildPartyState(a.getParty(), a.getState()))
                            .build())
                    .collect(Collectors.toList());

            components.add(component("proposition-authors", "AUTHOR_CARD_LIST",
                    AuthorCardListProperties.builder()
                            .title("Autores (" + authorCards.size() + ")")
                            .searchPlaceholder("Buscar por nome, partido ou estado...")
                            .items(authorCards)
                            .build()));
        }

        var response = new HomeScreenResponse();
        response.setScreenId("proposition-detail-" + id);
        response.setVersion(SCREEN_VERSION);
        response.setComponents(components);
        return response;
    }

    private PropositionCardProperties toPropositionCard(PropositionDto p) {
        String title = buildTypeLabel(p.getType(), p.getNumber(), p.getYear());
        String date = p.getPresentationDate() != null
                ? p.getPresentationDate().format(DATE_FORMATTER) : "";
        String typeColor = resolveTypeColor(p.getType());
        String appreciation = p.getStatusAppreciation() != null
                ? "Apreciação: " + p.getStatusAppreciation() : "Apreciação: Indefinida";

        return PropositionCardProperties.builder()
                .id(p.getId())
                .type(p.getType())
                .number(p.getNumber())
                .year(p.getYear())
                .title(title)
                .date(date)
                .summary(p.getSummary())
                .appreciation(appreciation)
                .typeColor(typeColor)
                .action(ComponentAction.builder()
                        .type(ACTION_NAVIGATE)
                        .route("/propositions/" + p.getId())
                        .build())
                .build();
    }

    private String buildTypeLabel(String type, Integer number, Integer year) {
        if (type == null) return "Proposição";
        String fullType = expandType(type);
        return fullType + " " + (number != null ? number : "") + "/" + (year != null ? year : "");
    }

    private String expandType(String type) {
        if (type == null) return "";
        return switch (type.toUpperCase()) {
            case "PL" -> "Projeto de Lei";
            case "PEC" -> "PEC";
            case "PDL" -> "PDL";
            case "PLP" -> "PLP";
            case "MPV" -> "MPV";
            case "PROC" -> "Processo Interno";
            case "REQ" -> "Requerimento";
            case "REC" -> "Recurso";
            default -> type;
        };
    }

    private String resolveTypeColor(String type) {
        if (type == null) return "#9E9E9E";
        return switch (type.toUpperCase()) {
            case "PL" -> "#1565C0";
            case "PEC" -> "#AD1457";
            case "PLP" -> "#6A1B9A";
            case "MPV" -> "#E65100";
            case "PDL" -> "#00695C";
            default -> "#9E9E9E";
        };
    }

    private String buildSituacaoText(PropositionDTO p) {
        String situationCode = p.getStatusSituationCode();
        String situationDescription = p.getStatusSituationDescription();
        if (situationCode != null && situationDescription != null) {
            return situationCode + " - " + situationDescription;
        }
        if (situationDescription != null) return situationDescription;
        if (situationCode != null) return situationCode;
        return null;
    }

    private String buildPartyState(String party, String state) {
        if (party == null && state == null) return "";
        if (party == null) return state;
        if (state == null) return party;
        return party + "-" + state;
    }

    private ScreenComponent component(String id, String type, Object properties) {
        var comp = new ScreenComponent();
        comp.setId(id);
        comp.setType(type);
        comp.setProperties(properties);
        return comp;
    }
}
