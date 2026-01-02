package br.com.deolhonacamara.api.service.sdui.screens;

import net.coelho.deolhonacamara.api.model.SDUIResponse;

/**
 * Estratégia para construção de telas SDUI específicas.
 * Implementa o padrão Strategy para diferentes tipos de tela.
 */
public interface SDUIScreenStrategy {

    /**
     * Retorna o tipo de tela que esta estratégia suporta.
     *
     * @return Tipo da tela
     */
    String getScreenType();

    /**
     * Constrói a tela SDUI baseada nos parâmetros fornecidos.
     *
     * @param params Parâmetros específicos da tela (podem ser filtros, paginação, etc.)
     * @return SDUIResponse configurada
     */
    SDUIResponse buildScreen(java.util.Map<String, Object> params);
}