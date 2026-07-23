import { describe, expect, it } from "vitest";

import { ROUTE_NAMES, ROUTE_PATHS } from "@/constants/routes";
import { colaboradorRoutes } from "@/router/routes/organization/colaborador.routes";

describe("colaborador routes", () => {
  it("registers hub, list, create, detail and edit routes", () => {
    const names = colaboradorRoutes.map(route => route.name);
    const paths = colaboradorRoutes.map(route => route.path);

    expect(names).toEqual([
      ROUTE_NAMES.COLABORADOR_HUB,
      ROUTE_NAMES.COLABORADOR_LIST,
      ROUTE_NAMES.COLABORADOR_CREATE,
      ROUTE_NAMES.COLABORADOR_DETAIL,
      ROUTE_NAMES.COLABORADOR_EDIT
    ]);

    expect(paths).toEqual([
      ROUTE_PATHS.COLABORADOR_HUB,
      ROUTE_PATHS.COLABORADOR_LIST,
      ROUTE_PATHS.COLABORADOR_CREATE,
      ROUTE_PATHS.COLABORADOR_DETAIL,
      ROUTE_PATHS.COLABORADOR_EDIT
    ]);
  });

  it("defines breadcrumbs for admin colaborador routes", () => {
    for (const route of colaboradorRoutes) {
      expect(route.meta?.showBreadcrumbs).toBe(true);
      expect(route.meta?.requiresAuth).toBe(true);
      expect(route.meta?.layout).toBe("admin");
      expect(Array.isArray(route.meta?.breadcrumbs)).toBe(true);
    }
  });
});
