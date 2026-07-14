package br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception;

/**
 * Exceção para indisponibilidade de sistema externo (HTTP 503).
 */
public class IntegrationUnavailableException extends IntegrationException {

    /**
     * Código padrão para indisponibilidade de integração.
     */
    public static final String ERROR_CODE = "INTEGRATION_UNAVAILABLE";

    /**
     * Constrói uma exceção de indisponibilidade de integração.
     *
     * @param message mensagem descritiva do erro
     */
    public IntegrationUnavailableException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Constrói uma exceção de indisponibilidade de integração com causa raiz.
     *
     * @param message mensagem descritiva do erro
     * @param cause   causa original da exceção
     */
    public IntegrationUnavailableException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
