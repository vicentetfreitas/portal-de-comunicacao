package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-conf-test.properties")
class PersistencePropertiesTest {

    @Autowired
    private PersistenceProperties persistenceProperties;

    @Test
    void shouldLoadPersistenceProperties() {
        assertThat(persistenceProperties.poolMaxSize()).isEqualTo(10);
        assertThat(persistenceProperties.poolMinIdle()).isEqualTo(2);
    }
}
