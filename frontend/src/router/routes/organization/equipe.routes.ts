import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

export const equipeLazyPages = {
  hub: () => import("@/pages/organization/equipe/EquipeHubPage.vue"),
  list: () => import("@/pages/organization/equipe/EquipeListPage.vue"),
  create: () => import("@/pages/organization/equipe/EquipeCreatePage.vue"),
  detail: () => import("@/pages/organization/equipe/EquipeDetailPage.vue"),
  edit: () => import("@/pages/organization/equipe/EquipeEditPage.vue")
} as const;

const equipeAdminMeta = {
  layout: "admin" as const,
  requiresAuth: true,
  roles: ["ADMIN"],
  showBreadcrumbs: true
};

export const equipeRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.EQUIPE_HUB,
    name: ROUTE_NAMES.EQUIPE_HUB,
    component: equipeLazyPages.hub,
    meta: {
      ...equipeAdminMeta,
      pageTitleKey: "equipe.hub.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "equipe.hub.title", icon: "mdi-account-group" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.EQUIPE_LIST,
    name: ROUTE_NAMES.EQUIPE_LIST,
    component: equipeLazyPages.list,
    meta: {
      ...equipeAdminMeta,
      pageTitleKey: "equipe.list.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "equipe.hub.title",
          to: ROUTE_PATHS.EQUIPE_HUB,
          icon: "mdi-account-group"
        },
        { labelKey: "equipe.list.title", icon: "mdi-format-list-bulleted" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.EQUIPE_CREATE,
    name: ROUTE_NAMES.EQUIPE_CREATE,
    component: equipeLazyPages.create,
    meta: {
      ...equipeAdminMeta,
      pageTitleKey: "equipe.create.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "equipe.hub.title",
          to: ROUTE_PATHS.EQUIPE_HUB,
          icon: "mdi-account-group"
        },
        { labelKey: "equipe.create.title", icon: "mdi-plus-circle" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.EQUIPE_DETAIL,
    name: ROUTE_NAMES.EQUIPE_DETAIL,
    component: equipeLazyPages.detail,
    props: true,
    meta: {
      ...equipeAdminMeta,
      pageTitleKey: "equipe.detail.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "equipe.hub.title",
          to: ROUTE_PATHS.EQUIPE_HUB,
          icon: "mdi-account-group"
        },
        { labelKey: "equipe.detail.title", icon: "mdi-eye" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.EQUIPE_EDIT,
    name: ROUTE_NAMES.EQUIPE_EDIT,
    component: equipeLazyPages.edit,
    props: true,
    meta: {
      ...equipeAdminMeta,
      pageTitleKey: "equipe.edit.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "equipe.hub.title",
          to: ROUTE_PATHS.EQUIPE_HUB,
          icon: "mdi-account-group"
        },
        { labelKey: "equipe.edit.title", icon: "mdi-pencil" }
      ]
    }
  }
];
