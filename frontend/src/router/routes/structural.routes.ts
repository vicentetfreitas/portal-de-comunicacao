import type { RouteRecordRaw } from "vue-router";

import { ROUTE_PATHS } from "@/constants/routes";

/**
 * Structural redirects — no business logic.
 */
export const structuralRoutes: RouteRecordRaw[] = [
  {
    path: ROUTE_PATHS.LOGIN,
    redirect: ROUTE_PATHS.AUTH
  }
];
