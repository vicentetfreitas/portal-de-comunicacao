package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdGeneratorTest {

    @Test
    void shouldGenerateValidUuid() {
        String correlationId = CorrelationIdGenerator.generate();

        assertThat(UUID.fromString(correlationId)).isNotNull();
    }

    @Test
    void shouldGenerateDifferentValuesOnEachCall() {
        String first = CorrelationIdGenerator.generate();
        String second = CorrelationIdGenerator.generate();

        assertThat(first).isNotEqualTo(second);
    }
}
