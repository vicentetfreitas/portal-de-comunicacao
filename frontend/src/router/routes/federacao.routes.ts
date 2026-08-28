import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

export const federacaoLazyPages = {
  areaHub: () => import("@/pages/federacao/FederacaoAreaHubPage.vue"),
  areaEquipe: () => import("@/pages/federacao/FederacaoAreaEquipePage.vue"),
  singular: () => import("@/pages/federacao/FederacaoSingularPage.vue")
} as const;

export const federacaoRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.FEDERACAO_AREA_DETAIL,
    name: ROUTE_NAMES.FEDERACAO_AREA_DETAIL,
    component: federacaoLazyPages.areaHub,
    props: true,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "federacao.area.breadcrumbLabel",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "federacao.area.breadcrumbLabel", icon: "mdi-domain" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.FEDERACAO_AREA_EQUIPE,
    name: ROUTE_NAMES.FEDERACAO_AREA_EQUIPE,
    component: federacaoLazyPages.areaEquipe,
    props: true,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "federacao.equipe.title",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "federacao.area.breadcrumbLabel",
          to: ROUTE_PATHS.FEDERACAO_AREA_DETAIL,
          icon: "mdi-domain"
        },
        { labelKey: "federacao.equipe.title", icon: "mdi-account-group" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.FEDERACAO_SINGULAR_DETAIL,
    name: ROUTE_NAMES.FEDERACAO_SINGULAR_DETAIL,
    component: federacaoLazyPages.singular,
    props: true,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "federacao.singular.breadcrumbLabel",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "federacao.singular.breadcrumbLabel", icon: "mdi-domain" }
      ]
    }
  }
];
