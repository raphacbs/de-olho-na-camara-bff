package br.com.deolhonacamara.sdui.service;

import br.com.deolhonacamara.api.repository.DashboardRepository;
import br.com.deolhonacamara.api.repository.UserRepository;
import br.com.deolhonacamara.sdui.model.ComponentAction;
import br.com.deolhonacamara.sdui.model.properties.GreetingHeaderProperties;
import br.com.deolhonacamara.sdui.model.properties.QuickAccessGridProperties;
import br.com.deolhonacamara.sdui.model.properties.QuickAccessItem;
import br.com.deolhonacamara.sdui.model.properties.SectionHeaderWithBadgeProperties;
import br.com.deolhonacamara.sdui.model.properties.StatCardItem;
import br.com.deolhonacamara.sdui.model.properties.StatsGridProperties;
import br.com.deolhonacamara.sdui.model.properties.YearSelectorBannerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.model.HomeScreenResponse;
import net.coelho.deolhonacamara.api.model.ScreenComponent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class HomeScreenService {

    private static final String ACTION_NAVIGATE = "NAVIGATE";
    private static final String SCREEN_ID = "home";
    private static final String SCREEN_VERSION = "1.0";

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;

    public HomeScreenResponse buildHomeScreen(UUID userId, String userEmail, int year) {
        log.info("Building SDUI home screen for user {} and year {}", userId, year);

        String displayName = resolveDisplayName(userEmail);
        int currentMonth = LocalDate.now().getMonthValue();

        long totalPoliticians = dashboardRepository.countTotalPoliticians();
        long totalFollowing = dashboardRepository.countTotalFollowing(userId);
        long totalPropositions = dashboardRepository.countTotalPropositions(year);
        BigDecimal monthlyExpenses = dashboardRepository.sumMonthlyExpenses(year, currentMonth);

        var response = new HomeScreenResponse();
        response.setScreenId(SCREEN_ID);
        response.setVersion(SCREEN_VERSION);
        response.setComponents(List.of(
                buildYearSelectorBanner(year),
                buildGreetingHeader(displayName),
                buildStatsGrid(totalPoliticians, totalFollowing, totalPropositions, monthlyExpenses),
                buildQuickAccessGrid(),
                buildFollowedSectionHeader((int) totalFollowing)
        ));
        return response;
    }

    private String resolveDisplayName(String userEmail) {
        try {
            return userRepository.findByEmail(userEmail)
                    .map(u -> firstNameOf(u.getFullName()))
                    .orElseGet(() -> firstNameOf(userEmail));
        } catch (Exception e) {
            log.warn("Could not resolve display name for {}: {}", userEmail, e.getMessage());
            return firstNameOf(userEmail);
        }
    }

    private String firstNameOf(String fullNameOrEmail) {
        if (fullNameOrEmail == null || fullNameOrEmail.isBlank()) return "usuário";
        String base = fullNameOrEmail.contains("@") ? fullNameOrEmail.split("@")[0] : fullNameOrEmail;
        return base.split("\\s+")[0];
    }

    private ScreenComponent buildYearSelectorBanner(int year) {
        return component("year-selector-banner", "YEAR_SELECTOR_BANNER",
                YearSelectorBannerProperties.builder()
                        .title("Dados exibidos referentes ao ano selecionado")
                        .subtitle("Altere o ano para filtrar todas as consultas")
                        .selectedYear(year)
                        .buttonBackgroundColor("#D32F2F")
                        .build());
    }

    private ScreenComponent buildGreetingHeader(String displayName) {
        return component("greeting-header", "GREETING_HEADER",
                GreetingHeaderProperties.builder()
                        .greeting("Olá, " + displayName + " \uD83D\uDC4B")
                        .subtitle("Acompanhe a atividade dos deputados federais")
                        .build());
    }

    private ScreenComponent buildStatsGrid(long totalPoliticians, long totalFollowing,
                                           long totalPropositions, BigDecimal monthlyExpenses) {
        return component("stats-grid", "STATS_GRID",
                StatsGridProperties.builder()
                        .columns(2)
                        .items(List.of(
                                StatCardItem.builder()
                                        .id("active-politicians")
                                        .icon("people_outline")
                                        .value(String.valueOf(totalPoliticians))
                                        .label("Deputados Ativos")
                                        .backgroundColor("#1565C0")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/politicians")
                                                .build())
                                        .build(),
                                StatCardItem.builder()
                                        .id("following")
                                        .icon("star_outline")
                                        .value(String.valueOf(totalFollowing))
                                        .label("Seguindo")
                                        .backgroundColor("#F57C00")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/followed")
                                                .build())
                                        .build(),
                                StatCardItem.builder()
                                        .id("propositions")
                                        .icon("description_outline")
                                        .value(String.valueOf(totalPropositions))
                                        .label("Proposições")
                                        .backgroundColor("#2E7D32")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/propositions")
                                                .build())
                                        .build(),
                                StatCardItem.builder()
                                        .id("monthly-expenses")
                                        .icon("attach_money")
                                        .value(formatCurrency(monthlyExpenses))
                                        .label("Despesas do Mês")
                                        .backgroundColor("#C62828")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/expenses")
                                                .build())
                                        .build()
                        ))
                        .build());
    }

    private ScreenComponent buildQuickAccessGrid() {
        return component("quick-access-grid", "QUICK_ACCESS_GRID",
                QuickAccessGridProperties.builder()
                        .title("Acesso Rápido")
                        .columns(2)
                        .items(List.of(
                                QuickAccessItem.builder()
                                        .id("propositions-quick")
                                        .icon("description")
                                        .label("Proposições")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/propositions")
                                                .build())
                                        .build(),
                                QuickAccessItem.builder()
                                        .id("votacoes-quick")
                                        .icon("how_to_vote")
                                        .label("Votações")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/votings")
                                                .build())
                                        .build(),
                                QuickAccessItem.builder()
                                        .id("deputados-quick")
                                        .icon("people")
                                        .label("Deputados")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/politicians")
                                                .build())
                                        .build(),
                                QuickAccessItem.builder()
                                        .id("configuracoes-quick")
                                        .icon("settings")
                                        .label("Configurações")
                                        .action(ComponentAction.builder()
                                                .type(ACTION_NAVIGATE)
                                                .route("/settings")
                                                .build())
                                        .build()
                        ))
                        .build());
    }

    private ScreenComponent buildFollowedSectionHeader(int followingCount) {
        return component("followed-section-header", "SECTION_HEADER_WITH_BADGE",
                SectionHeaderWithBadgeProperties.builder()
                        .title("Deputados que Você Segue")
                        .badgeCount(followingCount)
                        .badgeBackgroundColor("#2E7D32")
                        .action(ComponentAction.builder()
                                .type(ACTION_NAVIGATE)
                                .route("/followed")
                                .build())
                        .build());
    }

    private ScreenComponent component(String id, String type, Object properties) {
        var comp = new ScreenComponent();
        comp.setId(id);
        comp.setType(type);
        comp.setProperties(properties);
        return comp;
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "R$ 0";
        double value = amount.doubleValue();
        if (value >= 1_000_000_000) {
            return String.format("R$ %.1fB", value / 1_000_000_000);
        } else if (value >= 1_000_000) {
            return String.format("R$ %.1fM", value / 1_000_000);
        } else if (value >= 1_000) {
            return String.format("R$ %.1fK", value / 1_000);
        } else {
            return String.format("R$ %.2f", value);
        }
    }
}
