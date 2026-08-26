package br.com.unimedceara.portalcomunicacao.support.security;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

/**
 * Utilitários para simular autenticação JWT em testes unitários e de integração.
 */
public final class TestSecurityContextFactory {

    private TestSecurityContextFactory() {
    }

    /**
     * Define um usuário autenticado no {@link SecurityContextHolder} atual.
     *
     * @param subject identificador do usuário (claim {@code sub})
     */
    public static void setAuthenticatedUser(String subject) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                subject,
                null,
                Collections.emptyList());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    /**
     * Limpa o contexto de segurança do thread atual.
     */
    public static void clear() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Constrói um JWT assinado para testes via {@link JwtTokenService}.
     */
    public static String buildJwtToken(JwtTokenService jwtTokenService, long colaboradorId) {
        return jwtTokenService.issueToken(colaboradorId, "test-session", "user@test.com", "Test User");
    }

    /**
     * Cria cookie de access token assinado para requisições HTTP em testes de integração.
     */
    public static Cookie jwtCookie(JwtTokenService jwtTokenService, long colaboradorId) {
        return new Cookie(SecurityConstants.ACCESS_TOKEN_COOKIE, buildJwtToken(jwtTokenService, colaboradorId));
    }

    /**
     * Cria cookie de access token com Contexto Ativo explícito (federação/singular/área/equipe)
     * — necessário para testar autorização por {@code PERMISSAO_PASTA} (FT-DOCUMENTO), que não
     * é exercitada pelos fixtures padrão de {@link #jwtCookie(JwtTokenService, long)}.
     */
    public static Cookie jwtCookieWithContext(
            JwtTokenService jwtTokenService,
            long colaboradorId,
            Long federationId,
            Long singularId,
            Long areaId,
            Long teamId) {
        String token = jwtTokenService.issueToken(
                colaboradorId, "test-session", "user@test.com", "Test User",
                federationId, singularId, areaId, teamId, null);
        return new Cookie(SecurityConstants.ACCESS_TOKEN_COOKIE, token);
    }
}
