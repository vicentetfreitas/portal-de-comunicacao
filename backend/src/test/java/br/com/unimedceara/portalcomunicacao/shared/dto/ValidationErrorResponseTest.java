package br.com.unimedceara.portalcomunicacao.shared.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationErrorResponseTest {

    @Test
    void shouldCreateValidationErrorResponseWithFieldErrors() {
        Instant timestamp = Instant.parse("2026-07-08T14:00:00Z");
        List<FieldValidationError> errors = List.of(
                new FieldValidationError("title", "must not be blank"),
                new FieldValidationError("description", "size must be between 1 and 500"));

        ValidationErrorResponse response = new ValidationErrorResponse(
                timestamp, 400, "VALIDATION_ERROR", "Validation failed", "/api/v1/documents", errors);

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getError()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getMessage()).isEqualTo("Validation failed");
        assertThat(response.getPath()).isEqualTo("/api/v1/documents");
        assertThat(response.getErrors()).hasSize(2);
        assertThat(response.getErrors().get(0).field()).isEqualTo("title");
        assertThat(response.getErrors().get(0).message()).isEqualTo("must not be blank");
        assertThat(response.getErrors().get(1).field()).isEqualTo("description");
    }

    @Test
    void shouldDefensivelyCopyErrorsList() {
        Instant timestamp = Instant.parse("2026-07-08T14:00:00Z");
        List<FieldValidationError> mutableErrors = new ArrayList<>();
        mutableErrors.add(new FieldValidationError("field", "error"));

        ValidationErrorResponse response = new ValidationErrorResponse(
                timestamp, 400, "VALIDATION_ERROR", "Validation failed", "/api/v1/test", mutableErrors);

        mutableErrors.add(new FieldValidationError("other", "other error"));

        assertThat(response.getErrors()).hasSize(1);
    }

    @Test
    void shouldExtendErrorResponse() {
        Instant timestamp = Instant.parse("2026-07-08T14:00:00Z");
        ValidationErrorResponse response = new ValidationErrorResponse(
                timestamp, 422, "VALIDATION_ERROR", "Validation failed", "/api/v1/test", List.of());

        assertThat(response).isInstanceOf(ErrorResponse.class);
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getStatus()).isEqualTo(422);
    }
}
