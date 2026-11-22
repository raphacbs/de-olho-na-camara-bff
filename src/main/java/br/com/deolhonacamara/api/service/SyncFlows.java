package br.com.deolhonacamara.api.service;

import java.util.Map;
import java.util.Set;

/**
 * Nomes dos fluxos em {@code sync_progress} e quem impede quem de começar.
 * A trava vale para o processo que está vivo nesta instância.
 */
public final class SyncFlows {

    public static final String POLITICIANS = "Politicians";
    public static final String EXPENSES = "Expenses";
    public static final String VOTINGS = "Votings";
    public static final String SPEECHES = "Speeches";
    public static final String PROPOSITIONS = "Propositions";
    public static final String TRAMITATIONS = "PropositionTramitations";
    public static final String RECENT_PROPOSITIONS = "RecentPropositions";
    public static final String ALL = "All";
    public static final String ZONE = "America/Recife";

    public static final Map<String, String> DEFAULT_CRONS = Map.of(
            POLITICIANS, "0 0 23 * * *",
            EXPENSES, "0 0 1 * * *",
            VOTINGS, "0 0 2 * * *",
            SPEECHES, "0 0 3 * * *",
            PROPOSITIONS, "0 0 4 * * *",
            TRAMITATIONS, "0 0 5 * * *",
            RECENT_PROPOSITIONS, "0 0 6 * * *"
    );

    /**
     * Fluxos que precisam esperar. O pai não reescreve a lista enquanto o filho percorre,
     * e o filho não começa enquanto o pai está carregando essa lista.
     */
    public static final Map<String, Set<String>> BLOCKED_BY = Map.of(
            POLITICIANS, Set.of(EXPENSES, SPEECHES, PROPOSITIONS),
            EXPENSES, Set.of(POLITICIANS),
            SPEECHES, Set.of(POLITICIANS),
            PROPOSITIONS, Set.of(POLITICIANS, RECENT_PROPOSITIONS, TRAMITATIONS),
            TRAMITATIONS, Set.of(PROPOSITIONS, RECENT_PROPOSITIONS),
            RECENT_PROPOSITIONS, Set.of(PROPOSITIONS, TRAMITATIONS)
    );

    private SyncFlows() {
    }
}
