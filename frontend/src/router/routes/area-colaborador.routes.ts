import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

export const areaColaboradorLazyPages = {
  hub: () => import("@/pages/area-colaborador/AreaColaboradorHubPage.vue"),
  equipe: () => import("@/pages/area-colaborador/AreaColaboradorEquipePage.vue")
} as const;

export const areaColaboradorRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.AREA_COLABORADOR_HUB,
    name: ROUTE_NAMES.AREA_COLABORADOR_HUB,
    component: areaColaboradorLazyPages.hub,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "areaColaborador.hub.title",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "areaColaborador.hub.title", icon: "mdi-domain" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.AREA_COLABORADOR_EQUIPE,
    name: ROUTE_NAMES.AREA_COLABORADOR_EQUIPE,
    component: areaColaboradorLazyPages.equipe,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "areaColaborador.equipe.title",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "areaColaborador.hub.title",
          to: ROUTE_PATHS.AREA_COLABORADOR_HUB,
          icon: "mdi-domain"
        },
        { labelKey: "areaColaborador.equipe.title", icon: "mdi-account-group" }
      ]
    }
  }
];
