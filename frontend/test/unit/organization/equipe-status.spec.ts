import { describe, expect, it } from "vitest";

import {
  isEquipeDeactivation,
  resolveTargetEquipeStatus
} from "@/composables/organization/equipe-status";

describe("equipe-status", () => {
  it("resolves target status toggle", () => {
    expect(resolveTargetEquipeStatus("ACTIVE")).toBe("INACTIVE");
    expect(resolveTargetEquipeStatus("INACTIVE")).toBe("ACTIVE");
  });

  it("detects deactivation intent", () => {
    expect(isEquipeDeactivation("ACTIVE")).toBe(true);
    expect(isEquipeDeactivation("INACTIVE")).toBe(false);
  });
});
