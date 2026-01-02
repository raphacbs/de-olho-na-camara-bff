package br.com.deolhonacamara.api.model;

/**
 * Enumeração dos tipos de proposições legislativas.
 * Baseado nos tipos definidos na Câmara dos Deputados.
 */
public enum PropositionType {

    // Projetos de Lei
    PL("PL", "Projeto de Lei"),
    PLP("PLP", "Projeto de Lei Complementar"),
    PLV("PLV", "Projeto de Lei de Conversão"),

    // Propostas de Emenda
    PEC("PEC", "Proposta de Emenda Constitucional"),

    // Medidas Provisórias
    MPV("MPV", "Medida Provisória"),

    // Projetos de Decreto
    PDC("PDC", "Projeto de Decreto Legislativo"),

    // Projetos de Lei do Congresso
    PFC("PFC", "Projeto de Lei do Congresso"),

    // Projetos de Resolução
    PRC("PRC", "Projeto de Resolução do Congresso"),

    // Requerimentos
    REQ("REQ", "Requerimento"),
    RIC("RIC", "Requerimento de Informação"),

    // Indicações
    INC("INC", "Indicação"),

    // Recursos
    RCP("RCP", "Recurso"),

    // Mensagens
    MSC("MSC", "Mensagem"),
    MSG("MSG", "Mensagem"),

    // Avisos
    AVN("AVN", "Aviso");

    private final String code;
    private final String description;

    PropositionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Retorna a descrição completa do tipo de proposição.
     */
    public static String getDescription(String code) {
        if (code == null) return "Proposição";

        for (PropositionType type : values()) {
            if (type.code.equals(code.toUpperCase())) {
                return type.description;
            }
        }

        return code + " - Proposição Legislativa";
    }

    /**
     * Retorna o enum correspondente ao código informado.
     */
    public static PropositionType fromCode(String code) {
        if (code == null) return null;

        for (PropositionType type : values()) {
            if (type.code.equals(code.toUpperCase())) {
                return type;
            }
        }

        return null;
    }

    /**
     * Verifica se o código é válido.
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
}