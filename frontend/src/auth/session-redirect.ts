import { ROUTE_PATHS } from "@/constants/routes";

export type AuthErrorCode =
  | "unauthorized"
  | "forbidden"
  | "unavailable"
  | "unknown";

const AUTH_ERROR_QUERY_KEY = "error";

export function buildAuthRouteWithError(code: AuthErrorCode): string {
  return `${ROUTE_PATHS.AUTH}?${AUTH_ERROR_QUERY_KEY}=${code}`;
}

export function parseAuthErrorCode(value: unknown): AuthErrorCode | undefined {
  if (value === "unauthorized" || value === "forbidden") {
    return value;
  }
  if (value === "unavailable" || value === "service_unavailable") {
    return "unavailable";
  }
  if (value === "unknown") {
    return "unknown";
  }
  return undefined;
}

export function redirectToLogin(redirectPath?: string): void {
  const target = new URL(ROUTE_PATHS.AUTH, window.location.origin);
  if (redirectPath && redirectPath !== ROUTE_PATHS.AUTH) {
    target.searchParams.set("redirect", redirectPath);
  }
  window.location.assign(target.toString());
}

export function redirectAfterLogout(): void {
  window.location.assign(ROUTE_PATHS.AUTH);
}

export function isAuthApiRequestUrl(url: string | undefined): boolean {
  if (!url) {
    return false;
  }

  return (
    url.includes(AUTH_API_FRAGMENT.me) ||
    url.includes(AUTH_API_FRAGMENT.refresh) ||
    url.includes(AUTH_API_FRAGMENT.logout)
  );
}

const AUTH_API_FRAGMENT = {
  me: "/auth/me",
  refresh: "/auth/refresh",
  logout: "/auth/logout"
} as const;
