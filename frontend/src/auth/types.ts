/**
 * Authentication types — contracts for FT-AUTH integration.
 * Session is represented by HttpOnly cookies; user data comes from /auth/me.
 */
export type AuthSessionStatus =
  | "idle"
  | "loading"
  | "authenticated"
  | "unauthenticated";

export interface AuthenticatedUser {
  id: number;
  email: string;
  name: string;
  permissions: string[];
  sessionId: string;
  roles?: string[];
}

export interface AuthSessionState {
  status: AuthSessionStatus;
  user: AuthenticatedUser | null;
}

/**
 * FT-AUTH — hydrate session from GET /auth/me (not implemented in Sprint 0).
 */
export interface AuthSessionHydrator {
  hydrate(): Promise<void>;
}

/**
 * FT-AUTH — login redirect handler (not implemented in Sprint 0).
 */
export interface AuthLoginHandler {
  login(options?: { rememberMe?: boolean; redirectTo?: string }): void;
}

/**
 * FT-AUTH — logout handler (not implemented in Sprint 0).
 */
export interface AuthLogoutHandler {
  logout(): Promise<void>;
}

/**
 * FT-AUTH — token refresh handler (not implemented in Sprint 0).
 */
export interface AuthRefreshHandler {
  refresh(): Promise<boolean>;
}
