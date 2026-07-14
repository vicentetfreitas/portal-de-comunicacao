package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.NullOrSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NullOrSizeValidatorTest {

    private final NullOrSizeValidator validator = new NullOrSizeValidator();

    @BeforeEach
    void setUp() {
        NullOrSize annotation = mock(NullOrSize.class);
        when(annotation.min()).thenReturn(2);
        when(annotation.max()).thenReturn(5);
        validator.initialize(annotation);
    }

    @ParameterizedTest
    @NullSource
    void shouldAcceptNull(String value) {
        assertThat(validator.isValid(value, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "abc", "abcd", "abcde"})
    void shouldAcceptValuesWithinRange(String value) {
        assertThat(validator.isValid(value, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdef", "abcdefg"})
    void shouldRejectValuesOutsideRange(String value) {
        assertThat(validator.isValid(value, null)).isFalse();
    }

    @Test
    void shouldAcceptMinimumBoundary() {
        assertThat(validator.isValid("ab", null)).isTrue();
    }

    @Test
    void shouldAcceptMaximumBoundary() {
        assertThat(validator.isValid("abcde", null)).isTrue();
    }

    @Test
    void shouldRejectEmptyStringWhenMinIsGreaterThanZero() {
        assertThat(validator.isValid("", null)).isFalse();
    }
}
