package br.com.unimedceara.portalcomunicacao.shared.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Exceção base para erros de domínio e regras de negócio.
 * Todas as exceções funcionais do sistema devem herdar desta classe.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;

    private final Map<String, Object> context;

    /**
     * Constrói uma exceção de negócio.
     *
     * @param errorCode código identificador do erro
     * @param message   mensagem descritiva do erro
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.context = Map.of();
    }

    /**
     * Constrói uma exceção de negócio com contexto adicional.
     *
     * @param errorCode código identificador do erro
     * @param message   mensagem descritiva do erro
     * @param context   informações contextuais para rastreabilidade
     */
    public BusinessException(String errorCode, String message, Map<String, Object> context) {
        super(message);
        this.errorCode = errorCode;
        this.context = context == null ? Map.of() : Map.copyOf(context);
    }

    /**
     * Constrói uma exceção de negócio com causa raiz.
     *
     * @param errorCode código identificador do erro
     * @param message   mensagem descritiva do erro
     * @param cause     causa original da exceção
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.context = Map.of();
    }
}
