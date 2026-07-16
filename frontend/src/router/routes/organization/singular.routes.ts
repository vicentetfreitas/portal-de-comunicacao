import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

export const singularLazyPages = {
  hub: () => import("@/pages/organization/singular/SingularHubPage.vue"),
  list: () => import("@/pages/organization/singular/SingularListPage.vue"),
  create: () => import("@/pages/organization/singular/SingularCreatePage.vue"),
  detail: () => import("@/pages/organization/singular/SingularDetailPage.vue"),
  edit: () => import("@/pages/organization/singular/SingularEditPage.vue")
} as const;

const singularAdminMeta = {
  layout: "admin" as const,
  requiresAuth: true,
  roles: ["ADMIN"],
  showBreadcrumbs: true
};

export const singularRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.SINGULAR_HUB,
    name: ROUTE_NAMES.SINGULAR_HUB,
    component: singularLazyPages.hub,
    meta: {
      ...singularAdminMeta,
      pageTitleKey: "singular.hub.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "singular.hub.title", icon: "mdi-domain" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.SINGULAR_LIST,
    name: ROUTE_NAMES.SINGULAR_LIST,
    component: singularLazyPages.list,
    meta: {
      ...singularAdminMeta,
      pageTitleKey: "singular.list.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "singular.hub.title",
          to: ROUTE_PATHS.SINGULAR_HUB,
          icon: "mdi-domain"
        },
        { labelKey: "singular.list.title", icon: "mdi-format-list-bulleted" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.SINGULAR_CREATE,
    name: ROUTE_NAMES.SINGULAR_CREATE,
    component: singularLazyPages.create,
    meta: {
      ...singularAdminMeta,
      pageTitleKey: "singular.create.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "singular.hub.title",
          to: ROUTE_PATHS.SINGULAR_HUB,
          icon: "mdi-domain"
        },
        { labelKey: "singular.create.title", icon: "mdi-plus-circle" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.SINGULAR_DETAIL,
    name: ROUTE_NAMES.SINGULAR_DETAIL,
    component: singularLazyPages.detail,
    props: true,
    meta: {
      ...singularAdminMeta,
      pageTitleKey: "singular.detail.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "singular.hub.title",
          to: ROUTE_PATHS.SINGULAR_HUB,
          icon: "mdi-domain"
        },
        { labelKey: "singular.detail.title", icon: "mdi-eye" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.SINGULAR_EDIT,
    name: ROUTE_NAMES.SINGULAR_EDIT,
    component: singularLazyPages.edit,
    props: true,
    meta: {
      ...singularAdminMeta,
      pageTitleKey: "singular.edit.title",
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        {
          labelKey: "singular.hub.title",
          to: ROUTE_PATHS.SINGULAR_HUB,
          icon: "mdi-domain"
        },
        { labelKey: "singular.edit.title", icon: "mdi-pencil" }
      ]
    }
  }
];
