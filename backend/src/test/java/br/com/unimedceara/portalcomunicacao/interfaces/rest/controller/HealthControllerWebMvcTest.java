package br.com.unimedceara.portalcomunicacao.interfaces.rest.controller;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes focados do {@link HealthController} (slice MVC).
 */
class HealthControllerWebMvcTest {

    private static final String HEALTH_PATH = "/api/v1/health";

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ApplicationProperties applicationProperties = new ApplicationProperties(
                "portal-comunicacao", "0.0.1-SNAPSHOT", "UTC", "pt-BR");
        HealthController healthController = new HealthController(applicationProperties);
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get(HEALTH_PATH))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnJsonContentType() throws Exception {
        mockMvc.perform(get(HEALTH_PATH))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnApiResponseStructure() throws Exception {
        mockMvc.perform(get(HEALTH_PATH))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.application").value("portal-comunicacao"))
                .andExpect(jsonPath("$.data.version").value("0.0.1-SNAPSHOT"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
