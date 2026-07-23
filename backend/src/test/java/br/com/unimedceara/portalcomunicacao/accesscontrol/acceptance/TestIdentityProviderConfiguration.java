package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;

import br.com.unimedceara.portalcomunicacao.support.config.TestIdentityProviderAutoConfiguration;

/**
 * Import explícito opcional — o perfil {@code test} já registra os beans via
 * {@link TestIdentityProviderAutoConfiguration}. Mantido para compatibilidade com suítes FT-AUTH.
 */
@TestConfiguration
@Import(TestIdentityProviderAutoConfiguration.class)
public class TestIdentityProviderConfiguration {
}
