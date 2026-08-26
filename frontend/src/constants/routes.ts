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
  PRIMEIRO_ACESSO: "/primeiro-acesso",
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
  EQUIPE_EDIT: "/app/administrador/equipes/:id/editar",
  COLABORADOR_HUB: "/app/administrador/colaboradores",
  COLABORADOR_LIST: "/app/administrador/colaboradores/lista",
  COLABORADOR_CREATE: "/app/administrador/colaboradores/novo",
  COLABORADOR_DETAIL: "/app/administrador/colaboradores/:id",
  COLABORADOR_EDIT: "/app/administrador/colaboradores/:id/editar",
  AREA_COLABORADOR_HUB: "/app/area",
  AREA_COLABORADOR_EQUIPE: "/app/area/equipe",
  AREA_COLABORADOR_ARQUIVOS: "/app/area/arquivos",
  FEDERACAO_AREA_DETAIL: "/app/federacao/areas/:id",
  FEDERACAO_AREA_EQUIPE: "/app/federacao/areas/:id/equipe",
  FEDERACAO_SINGULAR_DETAIL: "/app/federacao/singulares/:id",
  PERFIL: "/app/perfil"
} as const;

export const ROUTE_NAMES = {
  HOME: "home",
  SHOWCASE: "showcase",
  AUTH: "auth",
  APP: "app",
  PRIMEIRO_ACESSO: "primeiro-acesso",
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
  EQUIPE_EDIT: "equipe-edit",
  COLABORADOR_HUB: "colaborador-hub",
  COLABORADOR_LIST: "colaborador-list",
  COLABORADOR_CREATE: "colaborador-create",
  COLABORADOR_DETAIL: "colaborador-detail",
  COLABORADOR_EDIT: "colaborador-edit",
  AREA_COLABORADOR_HUB: "area-colaborador-hub",
  AREA_COLABORADOR_EQUIPE: "area-colaborador-equipe",
  AREA_COLABORADOR_ARQUIVOS: "area-colaborador-arquivos",
  FEDERACAO_AREA_DETAIL: "federacao-area-detail",
  FEDERACAO_AREA_EQUIPE: "federacao-area-equipe",
  FEDERACAO_SINGULAR_DETAIL: "federacao-singular-detail",
  PERFIL: "perfil"
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

export function colaboradorDetailPath(id: string | number): string {
  return `/app/administrador/colaboradores/${id}`;
}

export function colaboradorEditPath(id: string | number): string {
  return `/app/administrador/colaboradores/${id}/editar`;
}

export function federacaoAreaPath(id: string | number): string {
  return `/app/federacao/areas/${id}`;
}

export function federacaoAreaEquipePath(id: string | number): string {
  return `/app/federacao/areas/${id}/equipe`;
}

export function federacaoSingularPath(id: string | number): string {
  return `/app/federacao/singulares/${id}`;
}
