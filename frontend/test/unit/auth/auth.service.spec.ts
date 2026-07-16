import { beforeEach, describe, expect, it, vi } from "vitest";

import { AUTH_API_PATHS } from "@/config/auth";
import { authService } from "@/services/auth/auth.service";
import { ApiError } from "@/types/api";

const getMock = vi.fn();
const postMock = vi.fn();

vi.mock("@/services/http", () => ({
  getHttpClient: () => ({
    get: getMock,
    post: postMock
  })
}));

const assignMock = vi.fn();

vi.mock("@/config/env", () => ({
  env: {
    apiBaseUrl: "/api/v1"
  }
}));

describe("AuthApiService", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.stubGlobal("location", { assign: assignMock });
  });

  it("builds login url with remember_me when enabled", () => {
    const url = authService.buildLoginUrl({ rememberMe: true });
    expect(url).toBe("/api/v1/auth/login?remember_me=true");
  });

  it("redirects browser to login endpoint", () => {
    authService.login({ rememberMe: false });
    expect(assignMock).toHaveBeenCalledWith("/api/v1/auth/login");
  });

  it("fetches current user from /auth/me", async () => {
    getMock.mockResolvedValue({
      data: {
        success: true,
        data: {
          id: 1,
          email: "user@unimedceara.com.br",
          name: "Maria",
          permissions: ["DOCUMENT_READ"],
          sessionId: "session-1"
        }
      }
    });

    const user = await authService.fetchCurrentUser();
    expect(getMock).toHaveBeenCalledWith(AUTH_API_PATHS.me);
    expect(user.email).toBe("user@unimedceara.com.br");
  });

  it("returns false when refresh fails with 401", async () => {
    postMock.mockRejectedValue(
      new ApiError({
        status: 401,
        code: "UNAUTHORIZED",
        message: "expired",
        category: "authentication"
      })
    );

    await expect(authService.refresh()).resolves.toBe(false);
  });

  it("posts to logout endpoint", async () => {
    postMock.mockResolvedValue({ status: 204 });
    await authService.logout();
    expect(postMock).toHaveBeenCalledWith(AUTH_API_PATHS.logout);
  });
});
