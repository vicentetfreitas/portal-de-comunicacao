package br.com.unimedceara.portalcomunicacao.shared.exception;

/**
 * Exceção lançada quando o usuário autenticado não possui permissão para a operação.
 */
public class ForbiddenException extends BusinessException {

    /**
     * Código padrão para acesso não autorizado.
     */
    public static final String ERROR_CODE = "FORBIDDEN";

    /**
     * Constrói uma exceção de acesso não autorizado.
     *
     * @param message mensagem descritiva do erro
     */
    public ForbiddenException(String message) {
        super(ERROR_CODE, message);
    }
}
