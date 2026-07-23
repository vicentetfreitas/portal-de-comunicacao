package br.com.unimedceara.portalcomunicacao.support.config;

import br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance.TestIdentityProviderClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * Substitui {@code ZimbraIdentityProviderAdapter} ({@code @Profile("!test")}) no perfil {@code test}.
 */
@AutoConfiguration
@Profile("test")
public class TestIdentityProviderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TestIdentityProviderClient.class)
    TestIdentityProviderClient testIdentityProviderClient() {
        return new TestIdentityProviderClient();
    }
}
