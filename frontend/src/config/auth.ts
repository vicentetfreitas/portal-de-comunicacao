/**
 * Auth API paths — relative to HTTP client base URL (/api/v1).
 * No HTTP calls in Sprint 0; contracts for FT-AUTH.
 */
export const AUTH_API_PATHS = {
  login: "/auth/login",
  callback: "/auth/callback",
  me: "/auth/me",
  logout: "/auth/logout",
  refresh: "/auth/refresh",
  primeiroAcessoAreas: "/auth/primeiro-acesso/areas",
  completePrimeiroAcesso: "/auth/primeiro-acesso"
} as const;

/**
 * Cookie names — HttpOnly session cookies are not readable by JavaScript (RN-AUTH-007).
 * Only CSRF cookie is accessed client-side.
 */
export const AUTH_COOKIE_NAMES = {
  accessToken: "access_token",
  refreshToken: "refresh_token",
  xsrfToken: "XSRF-TOKEN"
} as const;
