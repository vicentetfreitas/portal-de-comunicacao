package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-conf-test.properties")
class IntegrationPropertiesTest {

    @Autowired
    private IntegrationProperties integrationProperties;

    @Test
    void shouldLoadIntegrationPropertiesWithSafeDefaults() {
        assertThat(integrationProperties.connectTimeoutMs()).isEqualTo(5000);
        assertThat(integrationProperties.readTimeoutMs()).isEqualTo(5000);
        assertThat(integrationProperties.maxRetryAttempts()).isEqualTo(3);
        assertThat(integrationProperties.circuitBreakerThreshold()).isEqualTo(50);
    }
}
