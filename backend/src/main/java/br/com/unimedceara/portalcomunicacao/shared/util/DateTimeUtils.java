package br.com.unimedceara.portalcomunicacao.shared.util;

import br.com.unimedceara.portalcomunicacao.shared.constants.DateTimeConstants;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Operações reutilizáveis com {@link java.time}.
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    /**
     * Obtém o instante atual em UTC.
     *
     * @return instante atual em UTC
     */
    public static Instant nowUtc() {
        return Instant.now();
    }

    /**
     * Formata uma data no padrão ISO-8601.
     *
     * @param date data a ser formatada
     * @return representação textual da data
     */
    public static String formatIsoDate(LocalDate date) {
        return DateTimeConstants.ISO_DATE.format(date);
    }

    /**
     * Formata uma data e hora no padrão ISO-8601 com offset.
     *
     * @param dateTime data e hora a ser formatada
     * @return representação textual da data e hora
     */
    public static String formatIsoDateTime(OffsetDateTime dateTime) {
        return DateTimeConstants.ISO_DATE_TIME.format(dateTime);
    }

    /**
     * Converte uma {@link String} em {@link LocalDate} quando o valor é válido.
     *
     * @param value representação textual da data
     * @return data correspondente ou vazio quando o valor é inválido
     */
    public static Optional<LocalDate> parseIsoDate(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(LocalDate.parse(value.trim(), DateTimeConstants.ISO_DATE));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    /**
     * Converte uma {@link String} em {@link OffsetDateTime} quando o valor é válido.
     *
     * @param value representação textual da data e hora
     * @return data e hora correspondentes ou vazio quando o valor é inválido
     */
    public static Optional<OffsetDateTime> parseIsoDateTime(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(OffsetDateTime.parse(value.trim(), DateTimeConstants.ISO_DATE_TIME));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
}
