package br.com.unimedceara.portalcomunicacao.configuration.properties;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@PlatformFoundationSliceTest
class ApplicationPropertiesTest {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Test
    void shouldLoadApplicationProperties() {
        assertThat(applicationProperties.name()).isEqualTo("portal-comunicacao");
        assertThat(applicationProperties.version()).isEqualTo("0.0.1-SNAPSHOT");
        assertThat(applicationProperties.timezone()).isEqualTo("UTC");
        assertThat(applicationProperties.locale()).isEqualTo("pt-BR");
    }
}
