package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.exception;

import br.com.unimedceara.portalcomunicacao.shared.exception.BusinessException;

/**
 * Exceção de infraestrutura para erros de persistência.
 */
public class PersistenceException extends BusinessException {

    /**
     * Código padrão para erros de persistência.
     */
    public static final String ERROR_CODE = "PERSISTENCE_ERROR";

    /**
     * Constrói uma exceção de persistência.
     *
     * @param message mensagem descritiva do erro
     */
    public PersistenceException(String message) {
        super(ERROR_CODE, message);
    }

    /**
     * Constrói uma exceção de persistência com causa raiz.
     *
     * @param message mensagem descritiva do erro
     * @param cause   causa original da exceção
     */
    public PersistenceException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }
}
