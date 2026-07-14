package br.com.unimedceara.portalcomunicacao.shared.dto;

import java.util.List;

/**
 * Representa uma resposta paginada padronizada da API.
 *
 * @param <T> tipo dos elementos contidos na página
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    /**
     * Cria uma resposta paginada a partir dos parâmetros informados.
     *
     * @param content       elementos da página atual
     * @param page          número da página (base zero)
     * @param size          quantidade de elementos por página
     * @param totalElements total de elementos disponíveis
     * @param totalPages    total de páginas disponíveis
     * @param first         indica se é a primeira página
     * @param last          indica se é a última página
     * @param <T>           tipo dos elementos
     * @return resposta paginada
     */
    public static <T> PageResponse<T> of(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last) {
        return new PageResponse<>(List.copyOf(content), page, size, totalElements, totalPages, first, last);
    }
}
