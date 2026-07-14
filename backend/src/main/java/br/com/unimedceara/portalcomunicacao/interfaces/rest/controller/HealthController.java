package br.com.unimedceara.portalcomunicacao.interfaces.rest.controller;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ApplicationProperties;
import br.com.unimedceara.portalcomunicacao.interfaces.rest.response.HealthResponse;
import br.com.unimedceara.portalcomunicacao.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de saúde da aplicação.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Verificação de saúde da aplicação")
public class HealthController {

    private static final String STATUS_UP = "UP";

    private final ApplicationProperties applicationProperties;

    /**
     * Constrói o controller de health.
     *
     * @param applicationProperties propriedades da aplicação
     */
    public HealthController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Retorna o status operacional da aplicação.
     *
     * @return resposta padronizada com status, nome e versão
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Retorna o status operacional da aplicação")
    public ApiResponse<HealthResponse> health() {
        HealthResponse healthResponse = new HealthResponse(
                STATUS_UP,
                applicationProperties.name(),
                applicationProperties.version());
        return ApiResponse.success(healthResponse);
    }
}
