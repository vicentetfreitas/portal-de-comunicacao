package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

/**
 * Constantes reutilizáveis da infraestrutura de logging.
 */
public final class LoggingConstants {

    /**
     * Identificador lógico do Correlation ID.
     */
    public static final String CORRELATION_ID = "correlationId";

    /**
     * Identificador lógico do Request ID.
     */
    public static final String REQUEST_ID = "requestId";

    /**
     * Chave do MDC para o Correlation ID.
     */
    public static final String MDC_CORRELATION_ID = "correlationId";

    /**
     * Chave do MDC para o Request ID.
     */
    public static final String MDC_REQUEST_ID = "requestId";

    private LoggingConstants() {
    }
}
