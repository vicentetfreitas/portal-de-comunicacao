package br.com.unimedceara.portalcomunicacao.support.base;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.security.TestSecurityContextFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Testes de API autenticados como administrador de sessão ({@code colaborador@unimedceara.com.br}).
 */
public abstract class AbstractMockMvcIntegrationTest extends AbstractTransactionalMockMvcIntegrationTest {

    @Autowired
    protected JwtTokenService jwtTokenService;

    @Autowired
    protected ColaboradorRepository colaboradorRepository;

    @Autowired
    protected AuthProperties authProperties;

    private ColaboradorEntity sessionAdministrator;

    @BeforeEach
    void setUpSessionAdministrator() {
        sessionAdministrator = ColaboradorTestBuilder.sessionAdministrator(authProperties.defaultFederationId())
                .persist(colaboradorRepository);
    }

    protected ColaboradorEntity sessionAdministrator() {
        return sessionAdministrator;
    }

    protected Cookie adminCookie() {
        return TestSecurityContextFactory.jwtCookie(jwtTokenService, sessionAdministrator.getId());
    }
}
