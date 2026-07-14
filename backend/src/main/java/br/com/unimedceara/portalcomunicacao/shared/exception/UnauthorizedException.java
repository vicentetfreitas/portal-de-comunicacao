package br.com.unimedceara.portalcomunicacao.shared.exception;

/**
 * Exceção lançada quando o usuário não está autenticado.
 */
public class UnauthorizedException extends BusinessException {

    /**
     * Código padrão para acesso não autenticado.
     */
    public static final String ERROR_CODE = "UNAUTHORIZED";

    /**
     * Constrói uma exceção de acesso não autenticado.
     *
     * @param message mensagem descritiva do erro
     */
    public UnauthorizedException(String message) {
        super(ERROR_CODE, message);
    }
}
