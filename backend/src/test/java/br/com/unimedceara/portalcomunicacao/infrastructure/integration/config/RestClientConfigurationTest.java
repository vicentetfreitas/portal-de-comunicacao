package br.com.unimedceara.portalcomunicacao.infrastructure.integration.config;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ZimbraProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-int-test.properties")
class RestClientConfigurationTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ZimbraProperties zimbraProperties;

    @Test
    void shouldExposeRestClientBean() {
        assertThat(restClient).isNotNull();
    }

    @Test
    void shouldLoadZimbraTimeoutForRestClientConfiguration() {
        assertThat(zimbraProperties.timeoutMs()).isEqualTo(10000);
    }
}
