package br.com.unimedceara.portalcomunicacao.shared.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UuidUtilsTest {

    @Test
    void shouldGenerateRandomUuid() {
        UUID first = UuidUtils.random();
        UUID second = UuidUtils.random();

        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldReturnUuidFromValidString() {
        UUID expected = UUID.randomUUID();

        Optional<UUID> result = UuidUtils.fromString(expected.toString());

        assertThat(result).contains(expected);
        assertThat(UuidUtils.isValid(expected.toString())).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "not-a-uuid", "123", "00000000-0000-0000-0000-00000000000g"})
    void shouldRejectInvalidUuidValues(String value) {
        assertThat(UuidUtils.fromString(value)).isEmpty();
        assertThat(UuidUtils.isValid(value)).isFalse();
    }

    @Test
    void shouldTrimUuidBeforeParsing() {
        UUID expected = UUID.randomUUID();

        Optional<UUID> result = UuidUtils.fromString("  " + expected + "  ");

        assertThat(result).contains(expected);
    }
}
