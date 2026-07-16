import type { NavigationGuardNext, RouteLocationNormalized } from "vue-router";

import { routerGuardConfig } from "@/config/router";
import { ROUTE_PATHS } from "@/constants/routes";

import { authContext } from "@/auth";

export function createAuthorizationGuard() {
  return (
    to: RouteLocationNormalized,
    _from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    if (!routerGuardConfig.enforceAuthorization) {
      next();
      return;
    }

    const requiredRoles = to.meta.roles ?? [];
    const requiredCapabilities = to.meta.capabilities ?? [];

    if (requiredRoles.length > 0 && !authContext.hasAnyRole(requiredRoles)) {
      next(ROUTE_PATHS.UNAUTHORIZED);
      return;
    }

    if (
      requiredCapabilities.length > 0 &&
      !authContext.hasAnyCapability(requiredCapabilities)
    ) {
      next(ROUTE_PATHS.UNAUTHORIZED);
      return;
    }

    next();
  };
}
