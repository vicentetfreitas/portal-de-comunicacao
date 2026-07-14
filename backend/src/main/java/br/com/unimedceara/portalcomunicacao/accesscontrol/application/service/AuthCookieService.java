package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Configuração de cookies HttpOnly para tokens de autenticação FT-AUTH.
 */
@Service
public class AuthCookieService {

    private static final String AUTH_COOKIE_PATH = "/api/v1/auth";
    private static final String ROOT_COOKIE_PATH = "/";
    private static final String SAME_SITE_STRICT = "Strict";

    private final SecurityProperties securityProperties;

    public AuthCookieService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * Define o cookie do Access Token (JWT).
     */
    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from(SecurityConstants.ACCESS_TOKEN_COOKIE, accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path(ROOT_COOKIE_PATH)
                .maxAge(Duration.ofMinutes(securityProperties.jwtAccessTtlMinutes()))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Define o cookie do Refresh Token opaco.
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, boolean rememberMe) {
        Duration ttl = rememberMe
                ? Duration.ofDays(securityProperties.refreshTokenRememberMeDays())
                : Duration.ofHours(securityProperties.refreshTokenTtlHours());

        ResponseCookie cookie = ResponseCookie.from(SecurityConstants.REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path(AUTH_COOKIE_PATH)
                .maxAge(ttl)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Remove os cookies de autenticação da resposta.
     */
    public void clearAuthCookies(HttpServletResponse response) {
        clearCookie(response, SecurityConstants.ACCESS_TOKEN_COOKIE, ROOT_COOKIE_PATH);
        clearCookie(response, SecurityConstants.REFRESH_TOKEN_COOKIE, AUTH_COOKIE_PATH);
    }

    /**
     * Extrai o Refresh Token do array de cookies da requisição.
     */
    public String extractRefreshToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (SecurityConstants.REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                String value = cookie.getValue();
                return value == null || value.isBlank() ? null : value;
            }
        }
        return null;
    }

    private void clearCookie(HttpServletResponse response, String name, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path(path)
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
