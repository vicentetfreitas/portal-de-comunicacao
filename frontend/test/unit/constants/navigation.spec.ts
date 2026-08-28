import { describe, expect, it } from "vitest";

import { MAIN_NAV_ITEMS } from "@/constants/navigation";
import { ROUTE_PATHS } from "@/constants/routes";

describe("MAIN_NAV_ITEMS", () => {
  it("no longer includes the Área hub entry (removed 2026-08-26 — Federação covers the same ground)", () => {
    const areaItem = MAIN_NAV_ITEMS.find(
      item => item.to === ROUTE_PATHS.AREA_COLABORADOR_HUB
    );

    expect(areaItem).toBeUndefined();
  });
});
