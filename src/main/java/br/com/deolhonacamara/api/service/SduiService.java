package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.service.sdui.screens.SDUIScreenStrategy;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.SDUIResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Serviço principal para Server-Driven UI.
 * Utiliza padrões de projeto para melhor organização e manutenção do código.
 */
@Service
@RequiredArgsConstructor
public class SduiService {

    private final List<SDUIScreenStrategy> screenStrategies;

    /**
     * Retorna a tela inicial (Home) usando a estratégia apropriada.
     *
     * @return SDUIResponse com a tela Home configurada
     */
    public SDUIResponse getHomeScreen() {
        return getScreenStrategy("home").buildScreen(Map.of());
    }

    /**
     * Retorna a tela de deputados usando a estratégia apropriada.
     *
     * @param search Termo de busca
     * @param uf Filtro por estado
     * @param page Página
     * @param size Tamanho da página
     * @return SDUIResponse com a tela de deputados configurada
     */
    public SDUIResponse getDeputadosScreen(String search, String uf, Integer page, Integer size) {
        Map<String, Object> params = Map.of(
            "search", search != null ? search : "",
            "uf", uf != null ? uf : "",
            "page", page != null ? page : 0,
            "size", size != null ? size : 20
        );
        return getScreenStrategy("politicians").buildScreen(params);
    }

    /**
     * Retorna a tela de proposições usando a estratégia apropriada.
     *
     * @param tipos Lista de filtros por tipo (múltiplos valores permitidos)
     * @param statuses Lista de filtros por status (múltiplos valores permitidos)
     * @param politico Filtro por político
     * @param dataInicio Filtro por data inicial
     * @param dataFim Filtro por data final
     * @param page Página
     * @param size Tamanho da página
     * @return SDUIResponse com a tela de proposições configurada
     */
    public SDUIResponse getProposicoesScreen(List<String> tipos, List<String> statuses, String politico, String dataInicio, String dataFim, Integer page, Integer size) {
        Map<String, Object> params = Map.of(
            "tipos", tipos != null ? tipos : List.of(),
            "statuses", statuses != null ? statuses : List.of(),
            "politico", politico != null ? politico : "",
            "dataInicio", dataInicio != null ? dataInicio : "",
            "dataFim", dataFim != null ? dataFim : "",
            "page", page != null ? page : 0,
            "size", size != null ? size : 20
        );
        return getScreenStrategy("propositions").buildScreen(params);
    }

    /**
     * Retorna a tela de votações usando a estratégia apropriada.
     *
     * @param periodo Filtro por período
     * @param page Página
     * @param size Tamanho da página
     * @return SDUIResponse com a tela de votações configurada
     */
    public SDUIResponse getVotacoesScreen(String periodo, Integer page, Integer size) {
        Map<String, Object> params = Map.of(
            "periodo", periodo != null ? periodo : "week",
            "page", page != null ? page : 0,
            "size", size != null ? size : 20
        );
        return getScreenStrategy("votings").buildScreen(params);
    }

    /**
     * Retorna a tela de configurações usando a estratégia apropriada.
     *
     * @return SDUIResponse com a tela de configurações configurada
     */
    public SDUIResponse getConfiguracoesScreen() {
        return getScreenStrategy("settings").buildScreen(Map.of());
    }

    /**
     * Encontra a estratégia apropriada para o tipo de tela solicitado.
     *
     * @param screenType Tipo da tela
     * @return Estratégia correspondente
     * @throws IllegalArgumentException se a estratégia não for encontrada
     */
    private SDUIScreenStrategy getScreenStrategy(String screenType) {
        return screenStrategies.stream()
                .filter(strategy -> strategy.getScreenType().equals(screenType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estratégia não encontrada para tela: " + screenType));
    }

}
