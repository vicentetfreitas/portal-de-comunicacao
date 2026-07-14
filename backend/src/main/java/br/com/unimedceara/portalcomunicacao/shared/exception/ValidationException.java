package br.com.unimedceara.portalcomunicacao.shared.exception;

import br.com.unimedceara.portalcomunicacao.shared.dto.FieldValidationError;
import lombok.Getter;

import java.util.List;

/**
 * Exceção lançada para erros de validação funcionais.
 * Não substitui o Bean Validation; representa falhas de validação de regras de negócio.
 */
@Getter
public class ValidationException extends BusinessException {

    /**
     * Código padrão para erro de validação funcional.
     */
    public static final String ERROR_CODE = "VALIDATION_ERROR";

    private final List<FieldValidationError> errors;

    /**
     * Constrói uma exceção de validação funcional.
     *
     * @param message mensagem descritiva do erro
     * @param errors  lista de erros de validação por campo
     */
    public ValidationException(String message, List<FieldValidationError> errors) {
        super(ERROR_CODE, message);
        this.errors = List.copyOf(errors);
    }
}
