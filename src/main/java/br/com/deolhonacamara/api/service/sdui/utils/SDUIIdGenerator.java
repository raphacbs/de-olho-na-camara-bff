package br.com.deolhonacamara.api.service.sdui.utils;

import org.springframework.stereotype.Component;

/**
 * Utilitário para geração consistente de IDs únicos seguindo o padrão {{tipo}}-{{identificador}}.
 *
 * Padrões suportados:
 * - Componentes fixos/layout: container-{secao}-{identificador}, spacer-{posicao}-{contexto}, textblock-{proposito}-{secao}
 * - Componentes dinâmicos: deputado-{id}, proposition-{numero}-{ano}, votacao-{id}-{data}
 * - Componentes repetíveis: Adicionar sufixo único ou usar índices
 */
@Component
public class SDUIIdGenerator {

    // Constantes para tipos de componentes
    public static final String CONTAINER = "container";
    public static final String TEXTBLOCK = "textblock";
    public static final String SPACER = "spacer";
    public static final String IMAGE = "image";
    public static final String INPUT = "input";
    public static final String BUTTON = "button";
    public static final String CARD = "card";

    // Constantes para contextos
    public static final String MAIN = "main";
    public static final String HEADER = "header";
    public static final String BOTTOM = "bottom";

    /**
     * Gera ID para container seguindo padrão container-{secao}-{contexto}
     */
    public String containerId(String section, String context) {
        return String.format("%s-%s-%s", CONTAINER, section, context);
    }

    /**
     * Gera ID para textblock seguindo padrão textblock-{proposito}-{contexto}
     */
    public String textBlockId(String purpose, String context) {
        return String.format("%s-%s-%s", TEXTBLOCK, purpose, context);
    }

    /**
     * Gera ID para spacer seguindo padrão spacer-{posicao}-{contexto}
     */
    public String spacerId(String position, String context) {
        return String.format("%s-%s-%s", SPACER, position, context);
    }

    /**
     * Gera ID para image seguindo padrão image-{proposito}-{contexto}
     */
    public String imageId(String purpose, String context) {
        return String.format("%s-%s-%s", IMAGE, purpose, context);
    }

    /**
     * Gera ID para input seguindo padrão input-{proposito}-{contexto}
     */
    public String inputId(String purpose, String context) {
        return String.format("%s-%s-%s", INPUT, purpose, context);
    }

    /**
     * Gera ID para button seguindo padrão button-{acao}-{contexto}
     */
    public String buttonId(String action, String context) {
        return String.format("%s-%s-%s", BUTTON, action, context);
    }

    /**
     * Gera ID para card seguindo padrão card-{tipo}-{identificador}-{contexto}
     */
    public String cardId(String type, String identifier, String context) {
        return String.format("%s-%s-%s-%s", CARD, type, identifier, context);
    }

    // Métodos específicos para componentes dinâmicos

    /**
     * Gera ID para deputado seguindo padrão deputado-{id}
     */
    public String deputadoId(Long id) {
        return String.format("deputado-%d", id);
    }

    /**
     * Gera ID para proposição seguindo padrão proposition-{numero}-{ano}
     */
    public String propositionId(String numero, Integer ano) {
        return String.format("proposition-%s-%d", numero, ano);
    }

    /**
     * Gera ID para proposição por ID seguindo padrão proposition-{id}-{contexto}
     */
    public String propositionId(Long id, String context) {
        return String.format("proposition-%d-%s", id, context);
    }

    /**
     * Gera ID para votação seguindo padrão vote-{id}-{contexto}
     */
    public String voteId(Long id, String context) {
        return String.format("vote-%d-%s", id, context);
    }

    // Métodos para componentes filhos de entidades dinâmicas

    /**
     * Gera ID para componentes filhos de deputados
     */
    public String deputadoChildId(Long deputadoId, String childType, String purpose) {
        return String.format("%s-deputado-%d-%s-%s", childType, deputadoId, purpose, MAIN);
    }

    /**
     * Gera ID para componentes filhos de proposições
     */
    public String propositionChildId(Long propositionId, String childType, String purpose) {
        return String.format("%s-proposition-%d-%s-%s", childType, propositionId, purpose, MAIN);
    }

    /**
     * Gera ID para componentes filhos de votações
     */
    public String voteChildId(Long voteId, String childType, String purpose) {
        return String.format("%s-vote-%d-%s-%s", childType, voteId, purpose, MAIN);
    }

    /**
     * Gera ID único para componentes repetíveis adicionando um sufixo
     */
    public String uniqueId(String baseId, String suffix) {
        return String.format("%s-%s", baseId, suffix);
    }

    /**
     * Gera ID único para componentes repetíveis usando índice
     */
    public String indexedId(String baseId, int index) {
        return String.format("%s-%d", baseId, index);
    }
}