package br.com.unimedceara.portalcomunicacao.shared.util;

import java.util.Optional;
import java.util.UUID;

/**
 * Operações reutilizáveis relacionadas a {@link UUID}.
 */
public final class UuidUtils {

    private UuidUtils() {
    }

    /**
     * Verifica se o valor informado representa um UUID válido.
     *
     * @param value valor a ser validado
     * @return {@code true} quando o valor é um UUID válido
     */
    public static boolean isValid(String value) {
        return fromString(value).isPresent();
    }

    /**
     * Gera um novo UUID aleatório.
     *
     * @return UUID gerado
     */
    public static UUID random() {
        return UUID.randomUUID();
    }

    /**
     * Converte uma {@link String} em {@link UUID} quando o valor é válido.
     *
     * @param value representação textual do UUID
     * @return UUID correspondente ou vazio quando o valor é inválido
     */
    public static Optional<UUID> fromString(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(UUID.fromString(value.trim()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
