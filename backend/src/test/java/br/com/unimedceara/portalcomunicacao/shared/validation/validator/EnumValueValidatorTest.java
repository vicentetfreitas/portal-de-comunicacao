package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.EnumValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnumValueValidatorTest {

    private final EnumValueValidator validator = new EnumValueValidator();

    @BeforeEach
    void setUp() {
        EnumValue annotation = mock(EnumValue.class);
        when(annotation.enumClass()).thenAnswer(invocation -> SampleColor.class);
        when(annotation.ignoreCase()).thenReturn(false);
        validator.initialize(annotation);
    }

    @ParameterizedTest
    @NullSource
    void shouldAcceptNull(String value) {
        assertThat(validator.isValid(value, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RED", "GREEN", "BLUE"})
    void shouldAcceptValidEnumNames(String value) {
        assertThat(validator.isValid(value, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"red", "YELLOW", "invalid", ""})
    void shouldRejectInvalidEnumNames(String value) {
        assertThat(validator.isValid(value, null)).isFalse();
    }

    @Test
    void shouldAcceptEnumNameIgnoringCaseWhenConfigured() {
        EnumValue annotation = mock(EnumValue.class);
        when(annotation.enumClass()).thenAnswer(invocation -> SampleColor.class);
        when(annotation.ignoreCase()).thenReturn(true);

        EnumValueValidator caseInsensitiveValidator = new EnumValueValidator();
        caseInsensitiveValidator.initialize(annotation);

        assertThat(caseInsensitiveValidator.isValid("red", null)).isTrue();
        assertThat(caseInsensitiveValidator.isValid("Green", null)).isTrue();
        assertThat(caseInsensitiveValidator.isValid("invalid", null)).isFalse();
    }

    private enum SampleColor {
        RED,
        GREEN,
        BLUE
    }
}
