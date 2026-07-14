package br.com.unimedceara.portalcomunicacao.shared.exception;

import br.com.unimedceara.portalcomunicacao.shared.dto.ErrorResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.FieldValidationError;
import br.com.unimedceara.portalcomunicacao.shared.dto.ValidationErrorResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Responsável pela construção padronizada de respostas de erro a partir de exceções.
 * Centraliza a lógica de conversão para evitar duplicação no tratamento global.
 */
@Component
public class ExceptionResponseBuilder {

    /**
     * Constrói uma resposta de erro padrão.
     *
     * @param status  código HTTP do erro
     * @param error   código identificador do erro
     * @param message mensagem descritiva do erro
     * @param path    caminho da requisição
     * @return resposta de erro padronizada
     */
    public ErrorResponse buildErrorResponse(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path);
    }

    /**
     * Constrói uma resposta de erro de validação.
     *
     * @param status  código HTTP do erro
     * @param error   código identificador do erro
     * @param message mensagem descritiva do erro
     * @param path    caminho da requisição
     * @param errors  lista de erros de validação por campo
     * @return resposta de erro de validação padronizada
     */
    public ValidationErrorResponse buildValidationErrorResponse(
            int status,
            String error,
            String message,
            String path,
            List<FieldValidationError> errors) {
        return new ValidationErrorResponse(Instant.now(), status, error, message, path, errors);
    }

    /**
     * Constrói uma resposta de erro a partir de uma exceção de negócio.
     *
     * @param status código HTTP do erro
     * @param ex     exceção de negócio
     * @param path   caminho da requisição
     * @return resposta de erro padronizada
     */
    public ErrorResponse fromBusinessException(int status, BusinessException ex, String path) {
        return buildErrorResponse(status, ex.getErrorCode(), ex.getMessage(), path);
    }

    /**
     * Constrói uma resposta de erro de validação a partir de uma exceção funcional.
     *
     * @param status código HTTP do erro
     * @param ex     exceção de validação funcional
     * @param path   caminho da requisição
     * @return resposta de erro de validação padronizada
     */
    public ValidationErrorResponse fromValidationException(int status, ValidationException ex, String path) {
        return buildValidationErrorResponse(status, ex.getErrorCode(), ex.getMessage(), path, ex.getErrors());
    }

    /**
     * Converte erros de binding do Spring em erros de validação por campo.
     *
     * @param fieldErrors lista de erros de campo do Spring
     * @return lista de erros de validação padronizados
     */
    public List<FieldValidationError> fromFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(error -> new FieldValidationError(error.getField(), error.getDefaultMessage()))
                .toList();
    }

    /**
     * Converte violações de constraint do Bean Validation em erros por campo.
     *
     * @param violations conjunto de violações de constraint
     * @return lista de erros de validação padronizados
     */
    public List<FieldValidationError> fromConstraintViolations(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> new FieldValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList();
    }

    /**
     * Extrai o caminho da requisição a partir do contexto web.
     *
     * @param request contexto da requisição web
     * @return caminho da requisição
     */
    public String resolvePath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }

        String description = request.getDescription(false);
        if (description.startsWith("uri=")) {
            return description.substring(4);
        }

        return description;
    }
}
