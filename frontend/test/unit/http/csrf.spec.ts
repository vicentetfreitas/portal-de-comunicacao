import { beforeEach, describe, expect, it } from "vitest";

import {
  getCookieValue,
  isMutableHttpMethod,
  readCsrfToken
} from "@/services/http/csrf";

describe("http csrf utilities", () => {
  beforeEach(() => {
    document.cookie = "";
  });

  it("reads cookie values from document.cookie", () => {
    document.cookie = "XSRF-TOKEN=csrf-value";

    expect(getCookieValue("XSRF-TOKEN")).toBe("csrf-value");
    expect(readCsrfToken()).toBe("csrf-value");
  });

  it("detects mutable HTTP methods", () => {
    expect(isMutableHttpMethod("POST")).toBe(true);
    expect(isMutableHttpMethod("put")).toBe(true);
    expect(isMutableHttpMethod("GET")).toBe(false);
  });
});
