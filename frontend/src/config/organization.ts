/**
 * Organization API paths — relative to HTTP client base URL (/api/v1).
 */
export const SINGULAR_API_PATHS = {
  base: "/singulares"
} as const;

export const EQUIPE_API_PATHS = {
  base: "/equipes"
} as const;

export const AREA_API_PATHS = {
  base: "/areas"
} as const;

/**
 * Default federation from seed (DDL 008-initial-data.sql) until FT-FEDERACAO.
 * @see DS-SINGULAR-FE-01
 */
export const DEFAULT_FEDERATION_ID = 1;
