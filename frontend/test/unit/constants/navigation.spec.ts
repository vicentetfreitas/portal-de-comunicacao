import { describe, expect, it } from "vitest";

import { MAIN_NAV_ITEMS } from "@/constants/navigation";
import { ROUTE_PATHS } from "@/constants/routes";

describe("MAIN_NAV_ITEMS", () => {
  it("includes an entry for the Área hub (TK-AREA-COLAB-004)", () => {
    const areaItem = MAIN_NAV_ITEMS.find(
      item => item.to === ROUTE_PATHS.AREA_COLABORADOR_HUB
    );

    expect(areaItem).toBeDefined();
    expect(areaItem?.labelKey).toBe("areaColaborador.hub.title");
  });
});
