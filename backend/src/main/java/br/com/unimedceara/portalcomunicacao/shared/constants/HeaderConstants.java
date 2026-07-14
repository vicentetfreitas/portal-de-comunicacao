package br.com.unimedceara.portalcomunicacao.shared.constants;

/**
 * Nomes de headers HTTP reutilizáveis em toda a infraestrutura do backend.
 */
public final class HeaderConstants {

    /**
     * Header padrão para credenciais de autenticação.
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * Header que indica o tipo de conteúdo da requisição.
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * Header que indica os tipos de conteúdo aceitos na resposta.
     */
    public static final String ACCEPT = "Accept";

    /**
     * Header que indica o idioma preferido para a resposta.
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    /**
     * Header que indica a localização de um recurso criado.
     */
    public static final String LOCATION = "Location";

    /**
     * Header para identificação única da requisição.
     */
    public static final String X_REQUEST_ID = "X-Request-Id";

    /**
     * Header para rastreamento distribuído de operações.
     */
    public static final String X_CORRELATION_ID = "X-Correlation-Id";

    private HeaderConstants() {
    }
}
