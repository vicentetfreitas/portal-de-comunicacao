import { describe, expect, it } from "vitest";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";
import { singularRoutes } from "@/router/routes/organization/singular.routes";

describe("singular routes", () => {
  it("registers hub, list, create, detail and edit routes", () => {
    const names = singularRoutes.map(route => route.name);
    const paths = singularRoutes.map(route => route.path);

    expect(names).toEqual([
      ROUTE_NAMES.SINGULAR_HUB,
      ROUTE_NAMES.SINGULAR_LIST,
      ROUTE_NAMES.SINGULAR_CREATE,
      ROUTE_NAMES.SINGULAR_DETAIL,
      ROUTE_NAMES.SINGULAR_EDIT
    ]);

    expect(paths).toEqual([
      ROUTE_PATHS.SINGULAR_HUB,
      ROUTE_PATHS.SINGULAR_LIST,
      ROUTE_PATHS.SINGULAR_CREATE,
      ROUTE_PATHS.SINGULAR_DETAIL,
      ROUTE_PATHS.SINGULAR_EDIT
    ]);
  });

  it("defines breadcrumbs for admin singular routes", () => {
    for (const route of singularRoutes) {
      expect(route.meta?.showBreadcrumbs).toBe(true);
      expect(route.meta?.requiresAuth).toBe(true);
      expect(route.meta?.layout).toBe("admin");
      expect(Array.isArray(route.meta?.breadcrumbs)).toBe(true);
    }
  });
});
