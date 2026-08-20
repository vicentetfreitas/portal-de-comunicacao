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
     * Authority de sessão operacional (COLABORADOR persistido).
     */
    public static final String AUTHORITY_OPERATIONAL = "OPERATIONAL";

    /**
     * Authority de credencial temporária de Primeiro Acesso (DH-PA-01).
     */
    public static final String AUTHORITY_PRIMEIRO_ACESSO = "PRIMEIRO_ACESSO";

    /**
     * Claim {@code typ} do Access Token de Primeiro Acesso.
     */
    public static final String JWT_TYP_PRIMEIRO_ACESSO = "pa";

    /**
     * Bloqueio de Primeiro Acesso quando o domínio autenticado não determina Singular (BR-044).
     */
    public static final String PA_DOMAIN_NO_SINGULAR = "PA_DOMAIN_NO_SINGULAR";

    /**
     * Endpoint de hidratação de identidade (FT-SESSION / PA-API-005).
     */
    public static final String AUTH_ME_ENDPOINT = "/api/v1/auth/me";

    /**
     * Endpoints de onboarding de Primeiro Acesso (PA-API-006 / PA-API-007).
     */
    public static final String[] PRIMEIRO_ACESSO_ENDPOINT_PATTERNS = {
            "/api/v1/auth/primeiro-acesso",
            "/api/v1/auth/primeiro-acesso/**"
    };

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
