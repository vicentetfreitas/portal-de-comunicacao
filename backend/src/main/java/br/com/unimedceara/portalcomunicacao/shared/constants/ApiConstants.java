package br.com.unimedceara.portalcomunicacao.shared.constants;

/**
 * Constantes reutilizáveis relacionadas à exposição e consumo da API REST.
 */
public final class ApiConstants {

    /**
     * Número da página padrão em consultas paginadas (base zero).
     */
    public static final int DEFAULT_PAGE = 0;

    /**
     * Quantidade padrão de elementos por página.
     */
    public static final int DEFAULT_SIZE = 20;

    /**
     * Quantidade máxima de elementos permitida por página.
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * Versão atual da API exposta pelo backend.
     */
    public static final String API_VERSION = "v1";

    /**
     * Caminho base das APIs REST versionadas.
     */
    public static final String API_BASE_PATH = "/api/" + API_VERSION;

    private ApiConstants() {
    }
}
