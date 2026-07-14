package br.com.unimedceara.portalcomunicacao.shared.exception;

/**
 * Exceção lançada quando uma operação entra em conflito com o estado atual do recurso.
 */
public class ConflictException extends BusinessException {

    /**
     * Código padrão para conflito de estado.
     */
    public static final String ERROR_CODE = "CONFLICT";

    /**
     * Constrói uma exceção de conflito.
     *
     * @param message mensagem descritiva do erro
     */
    public ConflictException(String message) {
        super(ERROR_CODE, message);
    }
}
