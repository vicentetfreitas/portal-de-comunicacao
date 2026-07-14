package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.NotBlankIfPresent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NotBlankIfPresentValidatorTest {

    private final NotBlankIfPresentValidator validator = new NotBlankIfPresentValidator();

    @BeforeEach
    void setUp() {
        validator.initialize(mock(NotBlankIfPresent.class));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"abc", "a", " value "})
    void shouldAcceptValidValues(String value) {
        assertThat(validator.isValid(value, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void shouldRejectBlankValues(String value) {
        assertThat(validator.isValid(value, null)).isFalse();
    }

    @Test
    void shouldRejectEmptyString() {
        assertThat(validator.isValid("", null)).isFalse();
    }
}
