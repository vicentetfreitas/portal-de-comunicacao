import { describe, expect, it } from "vitest";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";
import { areaColaboradorRoutes } from "@/router/routes/area-colaborador.routes";

describe("area-colaborador routes", () => {
  it("registers the hub and equipe routes", () => {
    const names = areaColaboradorRoutes.map(route => route.name);
    const paths = areaColaboradorRoutes.map(route => route.path);

    expect(names).toEqual([
      ROUTE_NAMES.AREA_COLABORADOR_HUB,
      ROUTE_NAMES.AREA_COLABORADOR_EQUIPE
    ]);
    expect(paths).toEqual([
      ROUTE_PATHS.AREA_COLABORADOR_HUB,
      ROUTE_PATHS.AREA_COLABORADOR_EQUIPE
    ]);
  });

  it("requires an authenticated session, with no role restriction (self-service)", () => {
    for (const route of areaColaboradorRoutes) {
      expect(route.meta?.requiresAuth).toBe(true);
      expect(route.meta?.roles).toBeUndefined();
      expect(route.meta?.layout).toBe("main");
      expect(route.meta?.showBreadcrumbs).toBe(true);
      expect(Array.isArray(route.meta?.breadcrumbs)).toBe(true);
    }
  });
});
