package br.com.unimedceara.portalcomunicacao.support.base;

import br.com.unimedceara.portalcomunicacao.support.web.MockMvcTestSupport;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * MockMvc + perfil de integração com rollback ({@code @IntegrationTest} na subclasse).
 */
public abstract class AbstractTransactionalMockMvcIntegrationTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    protected Cookie obtainCsrfCookie() throws Exception {
        return MockMvcTestSupport.obtainCsrfCookie(mockMvc);
    }
}
