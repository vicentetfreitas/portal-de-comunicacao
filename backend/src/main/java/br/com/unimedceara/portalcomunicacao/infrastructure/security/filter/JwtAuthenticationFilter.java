package br.com.unimedceara.portalcomunicacao.infrastructure.security.filter;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtClaims;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Filtro JWT — extrai Access Token do cookie e valida criptograficamente via {@link JwtTokenService}.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            extractAccessToken(request).ifPresent(token -> authenticate(request, token));
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, String token) {
        Optional<JwtClaims> claims = jwtTokenService.validateAndParse(token);
        if (claims.isEmpty()) {
            return;
        }

        JwtClaims jwtClaims = claims.get();
        JwtAuthenticatedPrincipal principal = new JwtAuthenticatedPrincipal(
                jwtClaims.colaboradorId(),
                jwtClaims.sessionId(),
                jwtClaims.email(),
                jwtClaims.name(),
                jwtClaims.zimbraId(),
                jwtClaims.primeiroAcesso(),
                jwtClaims.federationId(),
                jwtClaims.singularId(),
                jwtClaims.areaId(),
                jwtClaims.teamId(),
                jwtClaims.papelAtribuicaoId());

        String authority = jwtClaims.primeiroAcesso()
                ? SecurityConstants.AUTHORITY_PRIMEIRO_ACESSO
                : SecurityConstants.AUTHORITY_OPERATIONAL;
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority(authority)));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Optional<String> extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (SecurityConstants.ACCESS_TOKEN_COOKIE.equals(cookie.getName())) {
                return Optional.ofNullable(cookie.getValue()).filter(value -> !value.isBlank());
            }
        }

        return Optional.empty();
    }
}
