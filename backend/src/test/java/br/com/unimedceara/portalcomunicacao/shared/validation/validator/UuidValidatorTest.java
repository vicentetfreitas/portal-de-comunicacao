package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.Uuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UuidValidatorTest {

    private final UuidValidator validator = new UuidValidator();

    @BeforeEach
    void setUp() {
        validator.initialize(mock(Uuid.class));
    }

    @ParameterizedTest
    @NullSource
    void shouldAcceptNull(String value) {
        assertThat(validator.isValid(value, null)).isTrue();
    }

    @Test
    void shouldAcceptValidUuid() {
        String validUuid = UUID.randomUUID().toString();
        assertThat(validator.isValid(validUuid, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "   ",
            "not-a-uuid",
            "123",
            "00000000-0000-0000-0000-00000000000g",
            "00000000-0000-0000-0000"
    })
    void shouldRejectInvalidUuid(String value) {
        assertThat(validator.isValid(value, null)).isFalse();
    }
}
