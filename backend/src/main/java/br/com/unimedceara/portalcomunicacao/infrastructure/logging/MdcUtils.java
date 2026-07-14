package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import org.slf4j.MDC;

/**
 * Utilitário para centralizar o acesso ao {@link MDC}.
 */
public final class MdcUtils {

    private MdcUtils() {
    }

    /**
     * Adiciona um valor ao contexto MDC.
     *
     * @param key   chave do contexto
     * @param value valor a ser armazenado
     */
    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * Obtém um valor do contexto MDC.
     *
     * @param key chave do contexto
     * @return valor armazenado ou {@code null} quando ausente
     */
    public static String get(String key) {
        return MDC.get(key);
    }

    /**
     * Remove uma entrada do contexto MDC.
     *
     * @param key chave do contexto
     */
    public static void remove(String key) {
        MDC.remove(key);
    }

    /**
     * Limpa todo o contexto MDC da thread atual.
     */
    public static void clear() {
        MDC.clear();
    }
}
