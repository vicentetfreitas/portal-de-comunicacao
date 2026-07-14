package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MdcUtilsTest {

    @AfterEach
    void tearDown() {
        MdcUtils.clear();
    }

    @Test
    void shouldPutAndGetValue() {
        MdcUtils.put("testKey", "testValue");

        assertThat(MdcUtils.get("testKey")).isEqualTo("testValue");
    }

    @Test
    void shouldRemoveValue() {
        MdcUtils.put("testKey", "testValue");

        MdcUtils.remove("testKey");

        assertThat(MdcUtils.get("testKey")).isNull();
    }

    @Test
    void shouldClearAllValues() {
        MdcUtils.put("firstKey", "firstValue");
        MdcUtils.put("secondKey", "secondValue");

        MdcUtils.clear();

        assertThat(MdcUtils.get("firstKey")).isNull();
        assertThat(MdcUtils.get("secondKey")).isNull();
    }
}
