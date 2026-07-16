import type { NavigationGuardNext, RouteLocationNormalized } from "vue-router";

import { routerGuardConfig } from "@/config/router";
import { ROUTE_PATHS } from "@/constants/routes";

import { authContext } from "@/auth";

function buildRedirectQuery(target: string, redirectPath: string) {
  return {
    path: target,
    query: { redirect: redirectPath }
  };
}

export function createAuthGuard() {
  return (
    to: RouteLocationNormalized,
    _from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    const requiresAuth = to.meta.requiresAuth === true;
    const guestOnly = to.meta.guestOnly === true;
    const isAuthenticated = authContext.isAuthenticated();

    if (
      guestOnly &&
      isAuthenticated &&
      routerGuardConfig.enforceAuthentication
    ) {
      next(ROUTE_PATHS.APP);
      return;
    }

    if (
      requiresAuth &&
      !isAuthenticated &&
      routerGuardConfig.enforceAuthentication
    ) {
      next(buildRedirectQuery(ROUTE_PATHS.AUTH, to.fullPath));
      return;
    }

    next();
  };
}
