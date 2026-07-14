package br.com.unimedceara.portalcomunicacao.shared.util;

import br.com.unimedceara.portalcomunicacao.shared.constants.ApiConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationUtilsTest {

    @Test
    void shouldNormalizeNegativePageToDefault() {
        assertThat(PaginationUtils.normalizePage(-1)).isEqualTo(ApiConstants.DEFAULT_PAGE);
        assertThat(PaginationUtils.normalizePage(-10)).isEqualTo(ApiConstants.DEFAULT_PAGE);
    }

    @Test
    void shouldKeepValidPage() {
        assertThat(PaginationUtils.normalizePage(0)).isEqualTo(0);
        assertThat(PaginationUtils.normalizePage(3)).isEqualTo(3);
    }

    @Test
    void shouldNormalizeNegativeOrZeroSizeToDefault() {
        assertThat(PaginationUtils.normalizeSize(0)).isEqualTo(ApiConstants.DEFAULT_SIZE);
        assertThat(PaginationUtils.normalizeSize(-5)).isEqualTo(ApiConstants.DEFAULT_SIZE);
    }

    @Test
    void shouldNormalizeSizeAboveMaximum() {
        assertThat(PaginationUtils.normalizeSize(ApiConstants.MAX_PAGE_SIZE + 1))
                .isEqualTo(ApiConstants.MAX_PAGE_SIZE);
        assertThat(PaginationUtils.normalizeSize(500)).isEqualTo(ApiConstants.MAX_PAGE_SIZE);
    }

    @Test
    void shouldKeepValidSize() {
        assertThat(PaginationUtils.normalizeSize(10)).isEqualTo(10);
        assertThat(PaginationUtils.normalizeSize(ApiConstants.MAX_PAGE_SIZE))
                .isEqualTo(ApiConstants.MAX_PAGE_SIZE);
    }

    @Test
    void shouldCalculateOffsetUsingNormalizedValues() {
        assertThat(PaginationUtils.calculateOffset(2, 10)).isEqualTo(20);
        assertThat(PaginationUtils.calculateOffset(0, 20)).isEqualTo(0);
    }

    @Test
    void shouldCalculateOffsetWithNormalizedPageAndSize() {
        assertThat(PaginationUtils.calculateOffset(-1, -1))
                .isEqualTo(ApiConstants.DEFAULT_PAGE * ApiConstants.DEFAULT_SIZE);
        assertThat(PaginationUtils.calculateOffset(1, ApiConstants.MAX_PAGE_SIZE + 50))
                .isEqualTo(ApiConstants.MAX_PAGE_SIZE);
    }
}
