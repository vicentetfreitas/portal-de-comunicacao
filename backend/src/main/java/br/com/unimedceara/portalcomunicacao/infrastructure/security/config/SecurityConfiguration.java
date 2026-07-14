package br.com.unimedceara.portalcomunicacao.infrastructure.security.config;

import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.security.entrypoint.RestAuthenticationEntryPoint;
import br.com.unimedceara.portalcomunicacao.infrastructure.security.filter.JwtAuthenticationFilter;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuração principal da cadeia de filtros de segurança stateless.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Cadeia de filtros stateless com CSRF, CORS e filtro JWT esqueleto.
     *
     * @param http configuração HTTP security
     * @param entryPoint ponto de entrada para erros 401
     * @param jwtAuthenticationFilter filtro JWT esqueleto
     * @param securityProperties propriedades de segurança
     * @param csrfTokenRepository repositório CSRF
     * @param corsConfigurationSource configuração CORS
     * @return cadeia de filtros configurada
     * @throws Exception quando a configuração falhar
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            RestAuthenticationEntryPoint entryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            SecurityProperties securityProperties,
            CsrfTokenRepository csrfTokenRepository,
            CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(csrf -> configureCsrf(csrf, securityProperties, csrfTokenRepository))
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityConstants.PUBLIC_ENDPOINT_PATTERNS).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void configureCsrf(
            org.springframework.security.config.annotation.web.configurers.CsrfConfigurer<HttpSecurity> csrf,
            SecurityProperties securityProperties,
            CsrfTokenRepository csrfTokenRepository) {
        if (securityProperties.csrfEnabled()) {
            CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
            requestHandler.setCsrfRequestAttributeName(null);
            csrf
                    .csrfTokenRepository(csrfTokenRepository)
                    .csrfTokenRequestHandler(requestHandler);
        } else {
            csrf.disable();
        }
    }
}
