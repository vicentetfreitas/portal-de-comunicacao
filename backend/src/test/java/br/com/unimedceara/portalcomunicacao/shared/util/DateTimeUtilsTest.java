package br.com.unimedceara.portalcomunicacao.shared.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeUtilsTest {

    @Test
    void shouldReturnCurrentInstantInUtc() {
        Instant before = Instant.now();
        Instant result = DateTimeUtils.nowUtc();
        Instant after = Instant.now();

        assertThat(result).isBetween(before, after);
    }

    @Test
    void shouldFormatIsoDate() {
        LocalDate date = LocalDate.of(2026, 7, 8);

        assertThat(DateTimeUtils.formatIsoDate(date)).isEqualTo("2026-07-08");
    }

    @Test
    void shouldFormatIsoDateTime() {
        OffsetDateTime dateTime = OffsetDateTime.of(2026, 7, 8, 14, 30, 0, 0, ZoneOffset.UTC);

        assertThat(DateTimeUtils.formatIsoDateTime(dateTime)).isEqualTo("2026-07-08T14:30:00Z");
    }

    @Test
    void shouldParseValidIsoDate() {
        assertThat(DateTimeUtils.parseIsoDate("2026-07-08"))
                .contains(LocalDate.of(2026, 7, 8));
    }

    @Test
    void shouldParseValidIsoDateTime() {
        assertThat(DateTimeUtils.parseIsoDateTime("2026-07-08T14:30:00Z"))
                .contains(OffsetDateTime.of(2026, 7, 8, 14, 30, 0, 0, ZoneOffset.UTC));
    }

    @Test
    void shouldRoundTripIsoDate() {
        LocalDate original = LocalDate.of(2026, 1, 15);
        String formatted = DateTimeUtils.formatIsoDate(original);

        assertThat(DateTimeUtils.parseIsoDate(formatted)).contains(original);
    }

    @Test
    void shouldRoundTripIsoDateTime() {
        OffsetDateTime original = OffsetDateTime.of(2026, 1, 15, 10, 15, 30, 0, ZoneOffset.UTC);
        String formatted = DateTimeUtils.formatIsoDateTime(original);

        assertThat(DateTimeUtils.parseIsoDateTime(formatted)).contains(original);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "invalid-date", "2026-13-40"})
    void shouldRejectInvalidIsoDate(String value) {
        assertThat(DateTimeUtils.parseIsoDate(value)).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "invalid-date-time", "2026-07-08"})
    void shouldRejectInvalidIsoDateTime(String value) {
        assertThat(DateTimeUtils.parseIsoDateTime(value)).isEmpty();
    }
}
