package br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception;

import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;

/**
 * Exceção base para erros de integração com sistemas externos.
 */
public class IntegrationException extends BusinessException {

    /**
     * Código padrão para erros de integração.
     */
    public static final String ERROR_CODE = "INTEGRATION_ERROR";

    /**
     * Constrói uma exceção de integração.
     *
     * @param message mensagem descritiva do erro
     */
    public IntegrationException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Constrói uma exceção de integração com código customizado.
     *
     * @param errorCode código identificador do erro
     * @param message   mensagem descritiva do erro
     */
    protected IntegrationException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constrói uma exceção de integração com causa raiz.
     *
     * @param message mensagem descritiva do erro
     * @param cause   causa original da exceção
     */
    public IntegrationException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    /**
     * Constrói uma exceção de integração com código customizado e causa raiz.
     *
     * @param errorCode código identificador do erro
     * @param message   mensagem descritiva do erro
     * @param cause     causa original da exceção
     */
    protected IntegrationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
