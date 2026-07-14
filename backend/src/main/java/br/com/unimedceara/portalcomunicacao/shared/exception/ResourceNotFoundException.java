package br.com.unimedceara.portalcomunicacao.shared.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * Código padrão para recurso não encontrado.
     */
    public static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    /**
     * Constrói uma exceção de recurso não encontrado.
     *
     * @param message mensagem descritiva do erro
     */
    public ResourceNotFoundException(String message) {
        super(ERROR_CODE, message);
    }
}
