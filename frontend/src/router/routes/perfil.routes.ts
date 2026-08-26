import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

export const perfilLazyPages = {
  perfil: () => import("@/pages/perfil/PerfilPage.vue")
} as const;

/**
 * "Editar perfil" (sidebar) now navigates here — `FT-PERFIL` is `DRAFT`
 * (no approved edit-form spec), so this is a read-only view of the already-
 * loaded session user (`GET /auth/me`, no new API call) rather than a
 * fabricated edit form. Explicit product decision, 2026-08-26.
 */
export const perfilRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.PERFIL,
    name: ROUTE_NAMES.PERFIL,
    component: perfilLazyPages.perfil,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "perfil.title",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "perfil.title", icon: "mdi-account" }
      ]
    }
  }
];
