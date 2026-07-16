import { describe, expect, it } from "vitest";

import {
  isSingularDeactivation,
  resolveTargetSingularStatus
} from "@/composables/organization/singular-status";

describe("singular-status helpers", () => {
  it("resolves INACTIVE when current status is ACTIVE", () => {
    expect(resolveTargetSingularStatus("ACTIVE")).toBe("INACTIVE");
  });

  it("resolves ACTIVE when current status is INACTIVE", () => {
    expect(resolveTargetSingularStatus("INACTIVE")).toBe("ACTIVE");
  });

  it("identifies deactivation only for ACTIVE singulares", () => {
    expect(isSingularDeactivation("ACTIVE")).toBe(true);
    expect(isSingularDeactivation("INACTIVE")).toBe(false);
  });
});
