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

/**
 * FT-SESSION — atribuição de papel (PAPEL_ATRIBUICAO) elegível ou ativa como contexto
 * operacional. Ortogonal ao vínculo cadastral (`ColaboradorOrganizationalLinks`).
 */
export interface PapelAtribuicaoSummary {
  id: number;
  papel: string;
  federacaoId: number | null;
  singularId: number | null;
  areaId: number | null;
  equipeId: number | null;
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
  /** FT-SESSION — atribuições de papel elegíveis do colaborador. */
  eligibleAssignments?: PapelAtribuicaoSummary[];
  /** FT-SESSION — atribuição de papel ativa (contexto operacional); ausente quando nenhuma foi selecionada. */
  activeAssignment?: PapelAtribuicaoSummary | null;
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
