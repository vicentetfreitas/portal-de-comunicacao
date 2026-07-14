package br.com.unimedceara.portalcomunicacao.infrastructure.security.config;

import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração CORS consumindo origens permitidas de {@link SecurityProperties}.
 */
@Configuration
public class CorsConfiguration {

    /**
     * Fonte de configuração CORS para toda a aplicação.
     *
     * @param securityProperties propriedades de segurança
     * @return configuração CORS baseada em URL
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(SecurityProperties securityProperties) {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(securityProperties.corsAllowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
