package br.com.unimedceara.portalcomunicacao.shared.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * Representa uma resposta de erro de validação da API.
 * Especialização de {@link ErrorResponse} que inclui a lista de erros por campo.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {

    private final List<FieldValidationError> errors;

    /**
     * Constrói uma resposta de erro de validação.
     *
     * @param timestamp momento em que o erro ocorreu
     * @param status    código HTTP do erro
     * @param error     código identificador do erro
     * @param message   mensagem descritiva do erro
     * @param path      caminho da requisição que originou o erro
     * @param errors    lista de erros de validação por campo
     */
    public ValidationErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path,
            List<FieldValidationError> errors) {
        super(timestamp, status, error, message, path);
        this.errors = List.copyOf(errors);
    }
}
