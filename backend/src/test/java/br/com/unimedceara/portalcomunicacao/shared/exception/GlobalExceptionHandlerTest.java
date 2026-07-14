package br.com.unimedceara.portalcomunicacao.shared.exception;

import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationException;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.infrastructure.persistence.exception.PersistenceException;
import br.com.unimedceara.portalcomunicacao.shared.dto.ErrorResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.FieldValidationError;
import br.com.unimedceara.portalcomunicacao.shared.dto.ValidationErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(new ExceptionResponseBuilder());
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/v1/documents");
        webRequest = new ServletWebRequest(servletRequest);
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("Documento não encontrado"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo(ResourceNotFoundException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Documento não encontrado");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/documents");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    void shouldHandleUnauthorizedException() {
        ResponseEntity<ErrorResponse> response = handler.handleUnauthorizedException(
                new UnauthorizedException("Credenciais inválidas"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(401);
        assertThat(response.getBody().getError()).isEqualTo(UnauthorizedException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Credenciais inválidas");
    }

    @Test
    void shouldHandleForbiddenException() {
        ResponseEntity<ErrorResponse> response = handler.handleForbiddenException(
                new ForbiddenException("Acesso negado"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getError()).isEqualTo(ForbiddenException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Acesso negado");
    }

    @Test
    void shouldHandleConflictException() {
        ResponseEntity<ErrorResponse> response = handler.handleConflictException(
                new ConflictException("Recurso já existe"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getError()).isEqualTo(ConflictException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Recurso já existe");
    }

    @Test
    void shouldHandleValidationException() {
        List<FieldValidationError> errors = List.of(new FieldValidationError("status", "Status inválido"));
        ResponseEntity<ValidationErrorResponse> response = handler.handleValidationException(
                new ValidationException("Validação funcional falhou", errors), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(422);
        assertThat(response.getBody().getError()).isEqualTo(ValidationException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Validação funcional falhou");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).field()).isEqualTo("status");
        assertThat(response.getBody().getErrors().get(0).message()).isEqualTo("Status inválido");
    }

    @Test
    void shouldHandleBusinessException() {
        ResponseEntity<ErrorResponse> response = handler.handleBusinessException(
                new BusinessException("BUSINESS_RULE_VIOLATION", "Regra de negócio violada"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(422);
        assertThat(response.getBody().getError()).isEqualTo("BUSINESS_RULE_VIOLATION");
        assertThat(response.getBody().getMessage()).isEqualTo("Regra de negócio violada");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "title", "must not be blank"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ValidationErrorResponse> response = handler.handleMethodArgumentNotValidException(
                exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).field()).isEqualTo("title");
        assertThat(response.getBody().getErrors().get(0).message()).isEqualTo("must not be blank");
    }

    @Test
    void shouldHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("page");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be greater than or equal to 0");

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ValidationErrorResponse> response = handler.handleConstraintViolationException(
                exception, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getErrors()).hasSize(1);
        assertThat(response.getBody().getErrors().get(0).field()).isEqualTo("page");
        assertThat(response.getBody().getErrors().get(0).message()).isEqualTo("must be greater than or equal to 0");
    }

    @Test
    void shouldHandlePersistenceException() {
        ResponseEntity<ErrorResponse> response = handler.handlePersistenceException(
                new PersistenceException("Falha ao persistir dados"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo(PersistenceException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Falha ao persistir dados");
    }

    @Test
    void shouldHandleDataAccessException() {
        ResponseEntity<ErrorResponse> response = handler.handlePersistenceException(
                new org.springframework.dao.DataRetrievalFailureException("database unavailable"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo(PersistenceException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("A persistence error occurred");
    }

    @Test
    void shouldHandleIntegrationUnavailableException() {
        ResponseEntity<ErrorResponse> response = handler.handleIntegrationUnavailableException(
                new IntegrationUnavailableException("Zimbra indisponível"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(503);
        assertThat(response.getBody().getError()).isEqualTo(IntegrationUnavailableException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Zimbra indisponível");
    }

    @Test
    void shouldHandleIntegrationException() {
        ResponseEntity<ErrorResponse> response = handler.handleIntegrationException(
                new IntegrationException("Falha na integração"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(502);
        assertThat(response.getBody().getError()).isEqualTo(IntegrationException.ERROR_CODE);
        assertThat(response.getBody().getMessage()).isEqualTo("Falha na integração");
    }

    @Test
    void shouldHandleGenericException() {
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(
                new RuntimeException("unexpected"), webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getError()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().getPath()).isEqualTo("/api/v1/documents");
    }
}
