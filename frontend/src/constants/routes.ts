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
  SINGULAR_EDIT: "/app/administrador/singulares/:id/editar"
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
  SINGULAR_EDIT: "singular-edit"
} as const;

export type RoutePath = (typeof ROUTE_PATHS)[keyof typeof ROUTE_PATHS];
