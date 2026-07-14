package br.com.unimedceara.portalcomunicacao.support.base;

import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

/**
 * Classe base para testes de integração com servidor embarcado e cliente HTTP.
 */
@IntegrationTest
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    protected RestClient restClient;

    @BeforeEach
    void setUpRestClient() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }
}
