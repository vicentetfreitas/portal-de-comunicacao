package br.com.unimedceara.portalcomunicacao.shared.util;

import java.util.Collection;
import java.util.Map;

/**
 * Operações reutilizáveis para coleções e mapas.
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Verifica se a coleção informada é {@code null} ou vazia.
     *
     * @param collection coleção a ser verificada
     * @return {@code true} quando a coleção é {@code null} ou vazia
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Verifica se o mapa informado é {@code null} ou vazio.
     *
     * @param map mapa a ser verificado
     * @return {@code true} quando o mapa é {@code null} ou vazio
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
