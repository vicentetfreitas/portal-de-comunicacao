import { describe, expect, it } from "vitest";

import { createModularRoutes } from "@/router/routes";

describe("createModularRoutes", () => {
  it("registers foundation routes and catch-all 404", () => {
    const routes = createModularRoutes();
    const paths = routes.map(route => route.path);

    expect(paths).toContain("/");
    expect(paths).toContain("/showcase");
    expect(paths.some(path => path.includes(":pathMatch"))).toBe(true);
  });
});
