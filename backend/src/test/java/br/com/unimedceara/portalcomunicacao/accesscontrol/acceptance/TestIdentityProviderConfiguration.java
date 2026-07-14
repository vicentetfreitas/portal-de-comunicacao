package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestIdentityProviderConfiguration {

    @Bean
    TestIdentityProviderClient testIdentityProviderClient() {
        return new TestIdentityProviderClient();
    }

    @Bean
    @Primary
    IdentityProviderClient identityProviderClient(TestIdentityProviderClient testIdentityProviderClient) {
        return testIdentityProviderClient;
    }
}
