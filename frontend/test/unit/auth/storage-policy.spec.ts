import { beforeEach, describe, expect, it } from "vitest";

import { detectTokenStorageViolations } from "@/auth/storage-policy";

describe("storage-policy", () => {
  beforeEach(() => {
    localStorage.clear();
    sessionStorage.clear();
  });

  it("returns no violations when storage is clean", () => {
    expect(detectTokenStorageViolations()).toEqual([]);
  });

  it("detects forbidden token keys in localStorage", () => {
    localStorage.setItem("access_token", "forbidden");

    expect(detectTokenStorageViolations()).toContain("access_token");
  });

  it("detects forbidden token keys in sessionStorage", () => {
    sessionStorage.setItem("jwt", "forbidden");

    expect(detectTokenStorageViolations()).toContain("jwt");
  });
});
