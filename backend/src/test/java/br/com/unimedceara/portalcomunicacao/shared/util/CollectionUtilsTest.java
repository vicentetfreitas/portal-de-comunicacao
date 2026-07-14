package br.com.unimedceara.portalcomunicacao.shared.util;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CollectionUtilsTest {

    @Test
    void shouldDetectNullCollection() {
        assertThat(CollectionUtils.isNullOrEmpty((Collection<?>) null)).isTrue();
    }

    @Test
    void shouldDetectEmptyCollection() {
        assertThat(CollectionUtils.isNullOrEmpty(List.of())).isTrue();
    }

    @Test
    void shouldDetectNonEmptyCollection() {
        assertThat(CollectionUtils.isNullOrEmpty(List.of("value"))).isFalse();
    }

    @Test
    void shouldDetectNullMap() {
        assertThat(CollectionUtils.isNullOrEmpty((Map<?, ?>) null)).isTrue();
    }

    @Test
    void shouldDetectEmptyMap() {
        assertThat(CollectionUtils.isNullOrEmpty(Map.of())).isTrue();
    }

    @Test
    void shouldDetectNonEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");

        assertThat(CollectionUtils.isNullOrEmpty(map)).isFalse();
    }
}
