import { describe, expect, it } from "vitest";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";
import { equipeRoutes } from "@/router/routes/organization/equipe.routes";

describe("equipe routes", () => {
  it("registers hub, list, create, detail and edit routes", () => {
    const names = equipeRoutes.map(route => route.name);
    const paths = equipeRoutes.map(route => route.path);

    expect(names).toEqual([
      ROUTE_NAMES.EQUIPE_HUB,
      ROUTE_NAMES.EQUIPE_LIST,
      ROUTE_NAMES.EQUIPE_CREATE,
      ROUTE_NAMES.EQUIPE_DETAIL,
      ROUTE_NAMES.EQUIPE_EDIT
    ]);

    expect(paths).toEqual([
      ROUTE_PATHS.EQUIPE_HUB,
      ROUTE_PATHS.EQUIPE_LIST,
      ROUTE_PATHS.EQUIPE_CREATE,
      ROUTE_PATHS.EQUIPE_DETAIL,
      ROUTE_PATHS.EQUIPE_EDIT
    ]);
  });

  it("defines breadcrumbs for admin equipe routes", () => {
    for (const route of equipeRoutes) {
      expect(route.meta?.showBreadcrumbs).toBe(true);
      expect(route.meta?.requiresAuth).toBe(true);
      expect(route.meta?.layout).toBe("admin");
      expect(Array.isArray(route.meta?.breadcrumbs)).toBe(true);
    }
  });
});
