import { describe, expect, it, vi } from "vitest";
import type { InternalAxiosRequestConfig } from "axios";
import { AxiosHeaders } from "axios";

import { httpConfig } from "@/config/http";
import { applyRequestInterceptors } from "@/services/http/interceptors/request.interceptor";

vi.mock("@/services/http/csrf", async () => {
  const actual = await vi.importActual<typeof import("@/services/http/csrf")>(
    "@/services/http/csrf"
  );
  return {
    ...actual,
    readCsrfToken: vi.fn(() => "mock-csrf-token")
  };
});

describe("applyRequestInterceptors", () => {
  it("adds correlation id, accept header and CSRF token for mutable requests", () => {
    const config: InternalAxiosRequestConfig = {
      headers: new AxiosHeaders(),
      method: "post",
      url: "/resource"
    };

    const result = applyRequestInterceptors(config);

    expect(result.headers.get("Accept")).toBe("application/json");
    expect(result.headers.get("X-Correlation-Id")).toBeTruthy();
    expect(result.headers.get(httpConfig.csrfHeaderName)).toBe(
      "mock-csrf-token"
    );
  });

  it("does not attach CSRF token for GET requests", () => {
    const config: InternalAxiosRequestConfig = {
      headers: new AxiosHeaders(),
      method: "get",
      url: "/resource"
    };

    const result = applyRequestInterceptors(config);

    expect(result.headers.get(httpConfig.csrfHeaderName)).toBeUndefined();
  });
});
