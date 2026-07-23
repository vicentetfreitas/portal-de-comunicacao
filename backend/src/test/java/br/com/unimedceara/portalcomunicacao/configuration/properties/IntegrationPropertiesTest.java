package br.com.unimedceara.portalcomunicacao.configuration.properties;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
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
