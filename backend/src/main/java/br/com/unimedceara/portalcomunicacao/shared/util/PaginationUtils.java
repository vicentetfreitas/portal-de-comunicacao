package br.com.unimedceara.portalcomunicacao.shared.util;

import br.com.unimedceara.portalcomunicacao.shared.constants.ApiConstants;

/**
 * Operações reutilizáveis para normalização e cálculo de paginação.
 */
public final class PaginationUtils {

    private PaginationUtils() {
    }

    /**
     * Normaliza o número da página para o intervalo válido da API.
     *
     * @param page número da página informado
     * @return página normalizada
     */
    public static int normalizePage(int page) {
        return Math.max(page, ApiConstants.DEFAULT_PAGE);
    }

    /**
     * Normaliza o tamanho da página para o intervalo válido da API.
     *
     * @param size quantidade de elementos por página informada
     * @return tamanho normalizado
     */
    public static int normalizeSize(int size) {
        if (size <= 0) {
            return ApiConstants.DEFAULT_SIZE;
        }

        return Math.min(size, ApiConstants.MAX_PAGE_SIZE);
    }

    /**
     * Calcula o deslocamento inicial com base na página e no tamanho informados.
     *
     * @param page número da página (base zero)
     * @param size quantidade de elementos por página
     * @return deslocamento calculado
     */
    public static int calculateOffset(int page, int size) {
        return normalizePage(page) * normalizeSize(size);
    }
}
