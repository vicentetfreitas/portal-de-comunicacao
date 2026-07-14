package br.com.unimedceara.portalcomunicacao.interfaces.rest.config;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ApplicationProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração OpenAPI 3 / SpringDoc para documentação da API.
 * Estratégia CD-S1A-002: springdoc-openapi-starter-webmvc-ui 3.x (Spring Boot 4).
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * Bean OpenAPI com metadados da aplicação.
     *
     * @param applicationProperties propriedades da aplicação
     * @return especificação OpenAPI
     */
    @Bean
    public OpenAPI openAPI(ApplicationProperties applicationProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationProperties.name())
                        .version(applicationProperties.version())
                        .description("Portal de Comunicação — API REST"));
    }
}
