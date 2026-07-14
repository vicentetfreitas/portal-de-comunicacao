package br.com.unimedceara.portalcomunicacao.shared.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithAllFields() {
        Instant timestamp = Instant.parse("2026-07-08T14:00:00Z");
        ErrorResponse response = new ErrorResponse(
                timestamp, 404, "RESOURCE_NOT_FOUND", "Recurso não encontrado", "/api/v1/documents/1");

        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getError()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(response.getMessage()).isEqualTo("Recurso não encontrado");
        assertThat(response.getPath()).isEqualTo("/api/v1/documents/1");
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        Instant timestamp = Instant.parse("2026-07-08T14:00:00Z");
        ErrorResponse first = new ErrorResponse(timestamp, 500, "INTERNAL_SERVER_ERROR", "Error", "/api/v1/test");
        ErrorResponse second = new ErrorResponse(timestamp, 500, "INTERNAL_SERVER_ERROR", "Error", "/api/v1/test");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}
