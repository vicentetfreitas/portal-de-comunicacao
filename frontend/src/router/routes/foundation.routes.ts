import type { RouteRecordRaw } from "vue-router";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";

/**
 * Lazy page loaders — explicit code-splitting for foundation routes.
 */
export const lazyPages = {
  home: () => import("@/pages/index.vue"),
  showcase: () => import("@/pages/showcase.vue"),
  auth: () => import("@/pages/auth/index.vue"),
  app: () => import("@/pages/app/index.vue"),
  primeiroAcesso: () => import("@/pages/primeiro-acesso/index.vue"),
  admin: () => import("@/pages/admin/index.vue"),
  unauthorized: () => import("@/pages/unauthorized.vue"),
  notFound: () => import("@/pages/[...path].vue")
} as const;

/**
 * Foundation routes with standardized meta and lazy components.
 */
export const foundationRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.HOME,
    name: ROUTE_NAMES.HOME,
    component: lazyPages.home,
    meta: {
      layout: "public",
      public: true,
      pageTitleKey: "layout.home.title"
    }
  },
  {
    path: ROUTE_PATHS.SHOWCASE,
    name: ROUTE_NAMES.SHOWCASE,
    component: lazyPages.showcase,
    meta: {
      layout: "main",
      pageTitleKey: "showcase.title",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "showcase.title", icon: "mdi-palette" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.AUTH,
    name: ROUTE_NAMES.AUTH,
    component: lazyPages.auth,
    meta: {
      layout: "auth",
      public: true,
      guestOnly: true,
      authFullBleed: true,
      pageTitleKey: "layout.auth.title"
    }
  },
  {
    path: ROUTE_PATHS.APP,
    name: ROUTE_NAMES.APP,
    component: lazyPages.app,
    meta: {
      layout: "main",
      requiresAuth: true,
      pageTitleKey: "layout.app.title"
      // Sem breadcrumbs: o frame `Home` do Figma (node 7:3) não tem trilha
      // de navegação — o conteúdo começa direto em "Fique por dentro".
    }
  },
  {
    path: ROUTE_PATHS.PRIMEIRO_ACESSO,
    name: ROUTE_NAMES.PRIMEIRO_ACESSO,
    component: lazyPages.primeiroAcesso,
    meta: {
      layout: "auth",
      requiresAuth: true,
      allowsPrimeiroAcesso: true,
      pageTitleKey: "layout.primeiroAcesso.title"
    }
  },
  {
    path: ROUTE_PATHS.ADMIN,
    name: ROUTE_NAMES.ADMIN,
    component: lazyPages.admin,
    meta: {
      layout: "admin",
      requiresAuth: true,
      roles: ["ADMIN"],
      pageTitleKey: "layout.admin.title",
      showBreadcrumbs: true,
      breadcrumbs: [
        { labelKey: "layout.nav.home", to: ROUTE_PATHS.HOME, icon: "mdi-home" },
        { labelKey: "layout.admin.title", icon: "mdi-shield-cog" }
      ]
    }
  },
  {
    path: ROUTE_PATHS.UNAUTHORIZED,
    name: ROUTE_NAMES.UNAUTHORIZED,
    component: lazyPages.unauthorized,
    meta: {
      layout: "public",
      public: true,
      pageTitleKey: "layout.unauthorized.title"
    }
  }
];
