import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

export const colaboradorLazyPages = {
  hub: () => import("@/pages/organization/colaborador/ColaboradorHubPage.vue"),
  list: () =>
    import("@/pages/organization/colaborador/ColaboradorListPage.vue"),
  create: () =>
    import("@/pages/organization/colaborador/ColaboradorCreatePage.vue"),
  detail: () =>
    import("@/pages/organization/colaborador/ColaboradorDetailPage.vue"),
  edit: () => import("@/pages/organization/colaborador/ColaboradorEditPage.vue")
} as const;

const colaboradorAdminMeta = {
  layout: "admin" as const,
  requiresAuth: true,
  roles: ["ADMIN"],
  showBreadcrumbs: true
};

export const colaboradorRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.COLABORADOR_HUB,
    name: ROUTE_NAMES.COLABORADOR_HUB,
    component: colaboradorLazyPages.hub,
    meta: {
      ...colaboradorAdminMeta,
      pageTitleKey: "colaborador.hub.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "colaborador.hub.title",
          icon: "mdi-account-multiple"
        }
      ]
    }
  },
  {
    path: ROUTE_PATHS.COLABORADOR_LIST,
    name: ROUTE_NAMES.COLABORADOR_LIST,
    component: colaboradorLazyPages.list,
    meta: {
      ...colaboradorAdminMeta,
      pageTitleKey: "colaborador.list.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "colaborador.hub.title",
          to: ROUTE_PATHS.COLABORADOR_HUB,
          icon: "mdi-account-multiple"
        },
        { labelKey: "colaborador.list.title", icon: "mdi-format-list-bulleted" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.COLABORADOR_CREATE,
    name: ROUTE_NAMES.COLABORADOR_CREATE,
    component: colaboradorLazyPages.create,
    meta: {
      ...colaboradorAdminMeta,
      pageTitleKey: "colaborador.create.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "colaborador.hub.title",
          to: ROUTE_PATHS.COLABORADOR_HUB,
          icon: "mdi-account-multiple"
        },
        { labelKey: "colaborador.create.title", icon: "mdi-plus-circle" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.COLABORADOR_DETAIL,
    name: ROUTE_NAMES.COLABORADOR_DETAIL,
    component: colaboradorLazyPages.detail,
    props: true,
    meta: {
      ...colaboradorAdminMeta,
      pageTitleKey: "colaborador.detail.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "colaborador.hub.title",
          to: ROUTE_PATHS.COLABORADOR_HUB,
          icon: "mdi-account-multiple"
        },
        { labelKey: "colaborador.detail.title", icon: "mdi-eye" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.COLABORADOR_EDIT,
    name: ROUTE_NAMES.COLABORADOR_EDIT,
    component: colaboradorLazyPages.edit,
    props: true,
    meta: {
      ...colaboradorAdminMeta,
      pageTitleKey: "colaborador.edit.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "colaborador.hub.title",
          to: ROUTE_PATHS.COLABORADOR_HUB,
          icon: "mdi-account-multiple"
        },
        { labelKey: "colaborador.edit.title", icon: "mdi-pencil" }
      ]
    }
  }
];
