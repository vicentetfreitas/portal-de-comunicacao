package br.com.unimedceara.portalcomunicacao.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.interfaces.rest.response.HealthResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste end-to-end do health endpoint via servidor embarcado ({@link AbstractIntegrationTest}).
 */
class HealthEndpointE2ETest extends AbstractIntegrationTest {

    private static final String HEALTH_PATH = "/api/v1/health";

    @Test
    void shouldReturnHealthUpWithApiResponseEnvelope() {
        ApiResponse<HealthResponse> body = restClient.get()
                .uri(HEALTH_PATH)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertThat(body).isNotNull();
        assertThat(body.success()).isTrue();
        assertThat(body.data().status()).isEqualTo("UP");
        assertThat(body.data().application()).isEqualTo("portal-comunicacao");
    }
}
