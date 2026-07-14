package br.com.unimedceara.portalcomunicacao.shared.constants;

/**
 * Constantes reutilizáveis da infraestrutura de segurança.
 * Não implementa autenticação nem autorização; apenas valores compartilhados.
 */
public final class SecurityConstants {

    /**
     * Prefixo utilizado no header {@code Authorization} para tokens do tipo Bearer.
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Identificador do tipo de token Bearer.
     */
    public static final String TOKEN_TYPE_BEARER = "Bearer";

    /**
     * Nome do cookie que transporta o Access Token (JWT).
     */
    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    /**
     * Nome do cookie que transporta o Refresh Token opaco.
     */
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    /**
     * Nome do cookie que transporta o token CSRF.
     */
    public static final String CSRF_COOKIE = "XSRF-TOKEN";

    /**
     * Header HTTP utilizado para envio do token CSRF.
     */
    public static final String CSRF_HEADER = "X-XSRF-TOKEN";

    /**
     * Padrões de endpoints públicos (whitelist) da fundação de segurança.
     */
    public static final String[] PUBLIC_ENDPOINT_PATTERNS = {
            "/actuator/health",
            "/actuator/health/**",
            "/actuator/metrics",
            "/actuator/metrics/**",
            "/actuator/info",
            "/api/v1/health",
            "/api/v1/auth/login",
            "/api/v1/auth/callback",
            "/api/v1/auth/refresh",
            "/api/v1/auth/logout",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    private SecurityConstants() {
    }
}
