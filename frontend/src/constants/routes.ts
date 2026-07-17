/**
 * Route paths — structural constants for navigation and guards.
 * Features must reference these paths instead of hardcoded strings.
 */
export const ROUTE_PATHS = {
  HOME: "/",
  SHOWCASE: "/showcase",
  AUTH: "/auth",
  LOGIN: "/login",
  APP: "/app",
  ADMIN: "/admin",
  UNAUTHORIZED: "/unauthorized",
  SINGULAR_HUB: "/app/administrador/singulares",
  SINGULAR_LIST: "/app/administrador/singulares/lista",
  SINGULAR_CREATE: "/app/administrador/singulares/novo",
  SINGULAR_DETAIL: "/app/administrador/singulares/:id",
  SINGULAR_EDIT: "/app/administrador/singulares/:id/editar",
  EQUIPE_HUB: "/app/administrador/equipes",
  EQUIPE_LIST: "/app/administrador/equipes/lista",
  EQUIPE_CREATE: "/app/administrador/equipes/novo",
  EQUIPE_DETAIL: "/app/administrador/equipes/:id",
  EQUIPE_EDIT: "/app/administrador/equipes/:id/editar"
} as const;

export const ROUTE_NAMES = {
  HOME: "home",
  SHOWCASE: "showcase",
  AUTH: "auth",
  APP: "app",
  ADMIN: "admin",
  NOT_FOUND: "not-found",
  UNAUTHORIZED: "unauthorized",
  SINGULAR_HUB: "singular-hub",
  SINGULAR_LIST: "singular-list",
  SINGULAR_CREATE: "singular-create",
  SINGULAR_DETAIL: "singular-detail",
  SINGULAR_EDIT: "singular-edit",
  EQUIPE_HUB: "equipe-hub",
  EQUIPE_LIST: "equipe-list",
  EQUIPE_CREATE: "equipe-create",
  EQUIPE_DETAIL: "equipe-detail",
  EQUIPE_EDIT: "equipe-edit"
} as const;

export type RoutePath = (typeof ROUTE_PATHS)[keyof typeof ROUTE_PATHS];

/** Resolves detail path for programmatic singular routes (outside file-based RouteNamedMap). */
export function singularDetailPath(id: string | number): string {
  return `/app/administrador/singulares/${id}`;
}

/** Resolves edit path for programmatic singular routes (outside file-based RouteNamedMap). */
export function singularEditPath(id: string | number): string {
  return `/app/administrador/singulares/${id}/editar`;
}

export function equipeDetailPath(id: string | number): string {
  return `/app/administrador/equipes/${id}`;
}

export function equipeEditPath(id: string | number): string {
  return `/app/administrador/equipes/${id}/editar`;
}
