import type { NavigationGuardNext, RouteLocationNormalized } from "vue-router";

import { routerGuardConfig } from "@/config/router";
import { ROUTE_PATHS } from "@/constants/routes";
import { useAuthStore } from "@/stores/auth-store";

function buildRedirectQuery(target: string, redirectPath: string) {
  return {
    path: target,
    query: { redirect: redirectPath }
  };
}

export function createAuthGuard() {
  return async (
    to: RouteLocationNormalized,
    _from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    const authStore = useAuthStore();

    if (authStore.status === "idle" || authStore.status === "loading") {
      try {
        await authStore.hydrateSession();
      } catch {
        // Hydration errors on public routes should not block navigation.
      }
    }

    const requiresAuth = to.meta.requiresAuth === true;
    const guestOnly = to.meta.guestOnly === true;
    const isAuthenticated = authStore.isAuthenticated;

    if (
      guestOnly &&
      isAuthenticated &&
      routerGuardConfig.enforceAuthentication
    ) {
      const redirect =
        typeof to.query?.redirect === "string"
          ? to.query.redirect
          : ROUTE_PATHS.APP;
      next(redirect);
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
