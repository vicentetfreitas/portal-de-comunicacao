/**
 * Authentication types — contracts for FT-AUTH integration.
 * Session is represented by HttpOnly cookies; user data comes from /auth/me.
 */
export type AuthSessionStatus =
  | "idle"
  | "loading"
  | "authenticated"
  | "unauthenticated";

export interface ColaboradorOrganizationalLinks {
  federationId: number | null;
  singularId: number | null;
  areaId: number | null;
  teamId: number | null;
}

export interface ResolvedPrimeiroAcessoOrganization {
  singularId: number;
  federationId: number;
}

export interface PrimeiroAcessoArea {
  id: number;
  name: string;
  acronym: string | null;
}

export interface CompletePrimeiroAcessoRequest {
  areaId: number;
  teamId?: number | null;
}

export interface AuthenticatedUser {
  id: number | null;
  email: string;
  name: string;
  permissions: string[];
  sessionId: string | null;
  organizationalLinks: ColaboradorOrganizationalLinks | null;
  primeiroAcesso: boolean;
  resolvedOrganization?: ResolvedPrimeiroAcessoOrganization | null;
  primeiroAcessoBlockCode?: string | null;
  roles?: string[];
}

/**
 * Auth cycle state only — authenticated user lives in session.store.
 */
export interface AuthSessionState {
  status: AuthSessionStatus;
}

/**
 * FT-AUTH — hydrate session from GET /auth/me.
 */
export interface AuthSessionHydrator {
  hydrate(): Promise<void>;
}

/**
 * FT-AUTH — login redirect handler.
 */
export interface AuthLoginHandler {
  login(options?: { rememberMe?: boolean; redirectTo?: string }): void;
}

/**
 * FT-AUTH — logout handler.
 */
export interface AuthLogoutHandler {
  logout(): Promise<void>;
}

/**
 * FT-AUTH — token refresh handler.
 */
export interface AuthRefreshHandler {
  refresh(): Promise<boolean>;
}
