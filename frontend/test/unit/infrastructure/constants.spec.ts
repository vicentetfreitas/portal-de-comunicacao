import { describe, expect, it } from "vitest";

import { BREADCRUMB_ICONS } from "@/constants/breadcrumbs";
import { DEFAULT_LAYOUT, LAYOUT_NAMES } from "@/constants/layouts";
import { ROUTE_PATHS } from "@/constants/routes";

describe("foundation constants", () => {
  it("exposes structural route paths", () => {
    expect(ROUTE_PATHS.HOME).toBe("/");
    expect(ROUTE_PATHS.AUTH).toBe("/auth");
    expect(ROUTE_PATHS.UNAUTHORIZED).toBe("/unauthorized");
  });

  it("exposes layout names and default layout", () => {
    expect(LAYOUT_NAMES.MAIN).toBe("main");
    expect(DEFAULT_LAYOUT).toBe("public");
  });

  it("exposes breadcrumb icon constants", () => {
    expect(BREADCRUMB_ICONS.HOME).toBe("mdi-home");
    expect(BREADCRUMB_ICONS.ADMIN).toBe("mdi-shield-account");
  });
});
