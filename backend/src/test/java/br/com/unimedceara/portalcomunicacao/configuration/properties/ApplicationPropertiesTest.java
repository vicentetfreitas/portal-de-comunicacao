package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "application.name=portal-comunicacao",
        "application.version=0.0.1-SNAPSHOT",
        "application.timezone=UTC",
        "application.locale=pt-BR"
})
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
