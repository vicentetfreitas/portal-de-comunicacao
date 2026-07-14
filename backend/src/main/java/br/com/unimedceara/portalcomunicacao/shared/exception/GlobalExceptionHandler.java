package br.com.unimedceara.portalcomunicacao.shared.exception;

import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.infrastructure.persistence.exception.PersistenceException;
import br.com.unimedceara.portalcomunicacao.shared.dto.ErrorResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.ValidationErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Tratamento global de exceções da API.
 * Converte todas as exceções em respostas padronizadas {@link ErrorResponse}
 * ou {@link ValidationErrorResponse}.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "An unexpected error occurred";
    private static final String PERSISTENCE_ERROR_MESSAGE = "A persistence error occurred";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";

    private final ExceptionResponseBuilder responseBuilder;

    /**
     * Trata exceções de recurso não encontrado.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    /**
     * Trata exceções de acesso não autenticado.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 401
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    /**
     * Trata exceções de acesso não autorizado.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 403
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(
            ForbiddenException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex, request);
    }

    /**
     * Trata exceções de conflito de estado.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 409
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(
            ConflictException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    /**
     * Trata exceções de validação funcional.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro de validação com status 422
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            ValidationException ex, WebRequest request) {
        String path = responseBuilder.resolvePath(request);
        ValidationErrorResponse body = responseBuilder.fromValidationException(
                HttpStatus.UNPROCESSABLE_CONTENT.value(), ex, path);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(body);
    }

    /**
     * Trata exceções genéricas de negócio.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 422
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_CONTENT, ex, request);
    }

    /**
     * Trata erros de validação de argumentos de método (@Valid em request bodies).
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro de validação com status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        String path = responseBuilder.resolvePath(request);
        ValidationErrorResponse body = responseBuilder.buildValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                VALIDATION_FAILED_MESSAGE,
                path,
                responseBuilder.fromFieldErrors(ex.getBindingResult().getFieldErrors()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Trata violações de constraint do Bean Validation em parâmetros de método.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro de validação com status 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        String path = responseBuilder.resolvePath(request);
        ValidationErrorResponse body = responseBuilder.buildValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                VALIDATION_FAILED_MESSAGE,
                path,
                responseBuilder.fromConstraintViolations(ex.getConstraintViolations()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Trata exceções de indisponibilidade de integração externa.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 503
     */
    @ExceptionHandler(IntegrationUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleIntegrationUnavailableException(
            IntegrationUnavailableException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex, request);
    }

    /**
     * Trata exceções genéricas de integração externa.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 502
     */
    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrationException(
            IntegrationException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, ex, request);
    }

    /**
     * Trata exceções de persistência e acesso a dados.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 500
     */
    @ExceptionHandler({PersistenceException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse> handlePersistenceException(Exception ex, WebRequest request) {
        log.error("Persistence error at {}", responseBuilder.resolvePath(request), ex);
        String path = responseBuilder.resolvePath(request);
        String message = ex instanceof PersistenceException persistenceException
                ? persistenceException.getMessage()
                : PERSISTENCE_ERROR_MESSAGE;
        ErrorResponse body = responseBuilder.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                PersistenceException.ERROR_CODE,
                message,
                path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Trata exceções não previstas.
     *
     * @param ex      exceção lançada
     * @param request contexto da requisição
     * @return resposta de erro com status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error at {}", responseBuilder.resolvePath(request), ex);
        String path = responseBuilder.resolvePath(request);
        ErrorResponse body = responseBuilder.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR_CODE,
                INTERNAL_SERVER_ERROR_MESSAGE,
                path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status, BusinessException ex, WebRequest request) {
        String path = responseBuilder.resolvePath(request);
        ErrorResponse body = responseBuilder.fromBusinessException(status.value(), ex, path);
        return ResponseEntity.status(status).body(body);
    }
}
