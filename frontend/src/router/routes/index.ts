import type { RouteRecordRaw } from "vue-router";

import { foundationRoutes, lazyPages } from "./foundation.routes";
import { structuralRoutes } from "./structural.routes";

export { foundationRoutes, lazyPages, structuralRoutes };

/**
 * Modular route registry — structural redirects, foundation pages, catch-all.
 */
export function createModularRoutes(): RouteRecordRaw[] {
  return [
    ...structuralRoutes,
    ...foundationRoutes,
    {
      path: "/:pathMatch(.*)*",
      name: "not-found",
      component: lazyPages.notFound,
      meta: {
        layout: "public",
        public: true,
        pageTitleKey: "common.notFound"
      }
    }
  ];
}
