package br.com.unimedceara.portalcomunicacao.shared.constants;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Formatos e referências temporais reutilizáveis em toda a aplicação.
 */
public final class DateTimeConstants {

    /**
     * Fuso horário padrão da aplicação.
     */
    public static final ZoneId UTC = ZoneId.of("UTC");

    /**
     * Formatador ISO-8601 para datas sem componente de hora.
     */
    public static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Formatador ISO-8601 para data e hora com offset.
     */
    public static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private DateTimeConstants() {
    }
}
