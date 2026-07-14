package br.com.unimedceara.portalcomunicacao.infrastructure.security.config;

import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

/**
 * Configuração CSRF baseada em cookie para fluxos autenticados por cookie (preparação FT-AUTH).
 */
@Configuration
public class CsrfConfiguration {

    /**
     * Repositório CSRF com cookie legível pelo frontend e header {@code X-XSRF-TOKEN}.
     *
     * @return repositório de tokens CSRF
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName(SecurityConstants.CSRF_COOKIE);
        repository.setHeaderName(SecurityConstants.CSRF_HEADER);
        repository.setCookiePath("/");
        return repository;
    }
}
