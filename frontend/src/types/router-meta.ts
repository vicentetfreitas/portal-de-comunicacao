import type { DsBreadcrumbItem } from "@/components/ds";

export type AppLayoutName = "auth" | "main" | "admin" | "public";

export interface AppBreadcrumbMeta {
  labelKey: string;
  to?: string;
  icon?: string;
}

/**
 * Standard route meta contract — Sprint 0 foundation.
 * FT-AUTH and Features extend via requiresAuth, roles and capabilities.
 */
export interface AppRouteMeta {
  /** Layout resolver key (App.vue) */
  layout?: AppLayoutName;

  /** i18n key for page heading and document title */
  pageTitleKey?: string;

  /** Static document title override (bypasses i18n) */
  documentTitle?: string;

  /** Breadcrumb trail */
  showBreadcrumbs?: boolean;
  breadcrumbs?: AppBreadcrumbMeta[];

  /** Route is public — no auth required */
  public?: boolean;

  /** Requires authenticated session (enforced when router guards enabled) */
  requiresAuth?: boolean;

  /** Only for guests — redirects authenticated users (FT-AUTH) */
  guestOnly?: boolean;

  /** RBAC roles required (any match) — enforced when authorization guard enabled */
  roles?: string[];

  /** Capability flags required (any match) — enforced when authorization guard enabled */
  capabilities?: string[];

  /** Full-bleed login canvas (Figma login — no auth chrome) */
  authFullBleed?: boolean;
}

declare module "vue-router" {
  interface RouteMeta extends AppRouteMeta {}
}

export type { DsBreadcrumbItem };
