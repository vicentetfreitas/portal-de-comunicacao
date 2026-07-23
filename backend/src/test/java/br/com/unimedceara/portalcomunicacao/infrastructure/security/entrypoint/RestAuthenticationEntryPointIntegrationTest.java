package br.com.unimedceara.portalcomunicacao.infrastructure.security.entrypoint;

import br.com.unimedceara.portalcomunicacao.support.annotation.PlatformFoundationSliceTest;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@PlatformFoundationSliceTest
@Import(RestAuthenticationEntryPointIntegrationTest.EntryPointTestConfiguration.class)
class RestAuthenticationEntryPointIntegrationTest {

    private static final String PROTECTED_PATH = "/api/v1/test/entrypoint";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void shouldReturnStandardizedErrorResponseForUnauthorizedAccess() throws Exception {
        mockMvc.perform(get(PROTECTED_PATH))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value(UnauthorizedException.ERROR_CODE))
                .andExpect(jsonPath("$.message").value("Authentication required"))
                .andExpect(jsonPath("$.path").value(PROTECTED_PATH))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @TestConfiguration
    static class EntryPointTestConfiguration {

        @Bean
        EntryPointTestController entryPointTestController() {
            return new EntryPointTestController();
        }
    }

    @RestController
    static class EntryPointTestController {

        @GetMapping(PROTECTED_PATH)
        String protectedResource() {
            return "protected";
        }
    }
}
