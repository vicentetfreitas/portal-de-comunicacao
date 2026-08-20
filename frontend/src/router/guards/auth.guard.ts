import type { NavigationGuardNext, RouteLocationNormalized } from "vue-router";

import { routerGuardConfig } from "@/config/router";
import { ROUTE_PATHS } from "@/constants/routes";
import { useAuthStore } from "@/stores/auth-store";
import { useSessionStore } from "@/stores/session.store";

function buildRedirectQuery(target: string, redirectPath: string) {
  return {
    path: target,
    query: { redirect: redirectPath }
  };
}

/**
 * Auth guard — bootstraps session via session.store when idle/loading,
 * then enforces authentication using auth-store status.
 */
export function createAuthGuard() {
  return async (
    to: RouteLocationNormalized,
    _from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    const authStore = useAuthStore();
    const sessionStore = useSessionStore();

    if (
      authStore.status === "idle" ||
      authStore.status === "loading" ||
      sessionStore.status === "idle" ||
      sessionStore.status === "loading"
    ) {
      try {
        await authStore.hydrateSession();
      } catch {
        // Hydration errors on public routes should not block navigation.
      }
    }

    const requiresAuth = to.meta.requiresAuth === true;
    const guestOnly = to.meta.guestOnly === true;
    const allowsPrimeiroAcesso = to.meta.allowsPrimeiroAcesso === true;
    const isAuthenticated = authStore.isAuthenticated;
    const needsPrimeiroAcesso =
      sessionStore.needsPrimeiroAcesso || sessionStore.isBlocked;

    if (
      guestOnly &&
      isAuthenticated &&
      routerGuardConfig.enforceAuthentication
    ) {
      if (needsPrimeiroAcesso) {
        next(ROUTE_PATHS.PRIMEIRO_ACESSO);
        return;
      }
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

    if (
      requiresAuth &&
      isAuthenticated &&
      needsPrimeiroAcesso &&
      !allowsPrimeiroAcesso &&
      routerGuardConfig.enforceAuthentication
    ) {
      next(ROUTE_PATHS.PRIMEIRO_ACESSO);
      return;
    }

    if (
      allowsPrimeiroAcesso &&
      isAuthenticated &&
      !needsPrimeiroAcesso &&
      sessionStore.isReady
    ) {
      next(ROUTE_PATHS.APP);
      return;
    }

    next();
  };
}
