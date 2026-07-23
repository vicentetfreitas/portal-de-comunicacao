package br.com.unimedceara.portalcomunicacao.configuration.properties;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
class PersistencePropertiesTest {

    @Autowired
    private PersistenceProperties persistenceProperties;

    @Test
    void shouldLoadPersistenceProperties() {
        assertThat(persistenceProperties.poolMaxSize()).isEqualTo(10);
        assertThat(persistenceProperties.poolMinIdle()).isEqualTo(2);
    }
}
