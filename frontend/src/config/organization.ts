/**
 * Organization API paths — relative to HTTP client base URL (/api/v1).
 */
export const SINGULAR_API_PATHS = {
  base: "/singulares"
} as const;

export const FEDERACAO_API_PATHS = {
  base: "/federacoes"
} as const;

export const EQUIPE_API_PATHS = {
  base: "/equipes"
} as const;

export const AREA_API_PATHS = {
  base: "/areas"
} as const;

export const COLABORADOR_API_PATHS = {
  base: "/colaboradores"
} as const;

/**
 * Default federation from seed (dml/001-federacao.sql).
 * Prefer listagem via FT-FEDERACAO quando disponível na UI.
 */
export const DEFAULT_FEDERATION_ID = 1;
