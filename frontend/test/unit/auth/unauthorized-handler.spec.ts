import { describe, expect, it, vi } from "vitest";

import { createUnauthorizedHandler } from "@/auth/unauthorized-handler";
import { ApiError } from "@/types/api";

describe("createUnauthorizedHandler", () => {
  it("retries request after successful refresh", async () => {
    const refreshSession = vi.fn(async () => true);
    const onSessionExpired = vi.fn();
    const handler = createUnauthorizedHandler({
      refreshSession,
      onSessionExpired
    });

    const error = new ApiError({
      status: 401,
      code: "UNAUTHORIZED",
      message: "expired"
    });

    await expect(
      handler(error, { requestUrl: "/api/v1/documents" })
    ).resolves.toBe(true);
    expect(refreshSession).toHaveBeenCalledOnce();
    expect(onSessionExpired).not.toHaveBeenCalled();
  });

  it("redirects to login when refresh fails", async () => {
    const refreshSession = vi.fn(async () => false);
    const onSessionExpired = vi.fn();
    const handler = createUnauthorizedHandler({
      refreshSession,
      onSessionExpired
    });

    const error = new ApiError({
      status: 401,
      code: "UNAUTHORIZED",
      message: "expired"
    });

    await expect(
      handler(error, { requestUrl: "/api/v1/documents" })
    ).resolves.toBe(false);
    expect(onSessionExpired).toHaveBeenCalledOnce();
  });

  it("does not refresh for auth endpoints", async () => {
    const refreshSession = vi.fn(async () => true);
    const handler = createUnauthorizedHandler({
      refreshSession,
      onSessionExpired: vi.fn()
    });

    const error = new ApiError({
      status: 401,
      code: "UNAUTHORIZED",
      message: "expired"
    });

    await expect(
      handler(error, { requestUrl: "/api/v1/auth/refresh" })
    ).resolves.toBe(false);
    expect(refreshSession).not.toHaveBeenCalled();
  });
});
