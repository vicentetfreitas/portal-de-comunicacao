import { describe, expect, it } from "vitest";

import { validateCsrfInfrastructure } from "@/auth/csrf";

describe("validateCsrfInfrastructure", () => {
  it("reports CSRF infrastructure as healthy", () => {
    const report = validateCsrfInfrastructure();

    expect(report.ok).toBe(true);
    expect(report.checks.every(check => check.passed)).toBe(true);
  });
});
