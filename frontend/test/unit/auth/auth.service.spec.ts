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

const readCsrfTokenMock = vi.fn((): string | undefined => "csrf-token");

vi.mock("@/services/http/csrf", () => ({
  readCsrfToken: () => readCsrfTokenMock()
}));

const fetchMock = vi.fn();

describe("AuthApiService", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    readCsrfTokenMock.mockReturnValue("csrf-token");
    vi.stubGlobal("location", { assign: assignMock });
    vi.stubGlobal("fetch", fetchMock);
  });

  it("builds login url with remember_me when enabled", () => {
    const url = authService.buildLoginUrl({ rememberMe: true });
    expect(url).toBe("/api/v1/auth/login?remember_me=true");
  });

  it("redirects browser to login endpoint", () => {
    authService.login({ rememberMe: false });
    expect(assignMock).toHaveBeenCalledWith("/api/v1/auth/login");
  });

  it("throws authentication error when login credentials are rejected", async () => {
    fetchMock.mockResolvedValue({
      status: 401,
      ok: false,
      json: async () => ({
        timestamp: "2026-07-24T00:00:00Z",
        status: 401,
        error: "UNAUTHORIZED",
        message: "Autenticação não realizada",
        path: "/api/v1/auth/login"
      })
    });

    await expect(
      authService.login({
        email: "user@unimedceara.com.br",
        password: "wrong"
      })
    ).rejects.toMatchObject({
      status: 401,
      code: "UNAUTHORIZED",
      category: "authentication"
    });
  });

  it("throws authorization error when colaborador is forbidden", async () => {
    fetchMock.mockResolvedValue({
      status: 403,
      ok: false,
      json: async () => ({
        timestamp: "2026-07-24T00:00:00Z",
        status: 403,
        error: "FORBIDDEN",
        message: "Colaborador sem autorização para acessar o Portal",
        path: "/api/v1/auth/login"
      })
    });

    await expect(
      authService.login({
        email: "inactive@unimedceara.com.br",
        password: "secret"
      })
    ).rejects.toMatchObject({
      status: 403,
      code: "FORBIDDEN",
      category: "authorization"
    });
  });

  it("throws server category when Zimbra is unavailable on login", async () => {
    fetchMock.mockResolvedValue({
      status: 503,
      ok: false,
      json: async () => ({
        timestamp: "2026-07-24T00:00:00Z",
        status: 503,
        error: "INTEGRATION_UNAVAILABLE",
        message: "Zimbra indisponível",
        path: "/api/v1/auth/login"
      })
    });

    await expect(
      authService.login({
        email: "user@unimedceara.com.br",
        password: "secret"
      })
    ).rejects.toMatchObject({
      status: 503,
      code: "INTEGRATION_UNAVAILABLE",
      category: "server"
    });
  });

  it("primes CSRF cookie before posting credentials", async () => {
    readCsrfTokenMock
      .mockReturnValueOnce(undefined)
      .mockReturnValue("primed-token");

    fetchMock
      .mockResolvedValueOnce({
        status: 401,
        ok: false,
        json: async () => ({
          timestamp: "2026-07-24T00:00:00Z",
          status: 401,
          error: "UNAUTHORIZED",
          message: "Autenticação não realizada",
          path: "/api/v1/auth/me"
        }),
        headers: {
          getSetCookie: () => ["XSRF-TOKEN=primed-token; Path=/"]
        }
      })
      .mockResolvedValueOnce({
        status: 302,
        ok: false,
        headers: {
          get: (name: string) => (name === "Location" ? "/app" : null)
        }
      });

    await authService.login({
      email: "user@unimedceara.com.br",
      password: "secret"
    });

    expect(fetchMock).toHaveBeenNthCalledWith(
      1,
      "/api/v1/auth/me",
      expect.objectContaining({
        method: "GET",
        credentials: "include"
      })
    );
    expect(fetchMock).toHaveBeenNthCalledWith(
      2,
      "/api/v1/auth/login",
      expect.objectContaining({
        method: "POST",
        credentials: "include"
      })
    );
  });

  it("posts credentials to login when email and password are provided", async () => {
    fetchMock.mockResolvedValue({
      status: 302,
      headers: {
        get: (name: string) => (name === "Location" ? "/app" : null)
      }
    });

    await authService.login({
      email: "user@unimedceara.com.br",
      password: "secret",
      rememberMe: true
    });

    expect(fetchMock).toHaveBeenCalledWith(
      "/api/v1/auth/login",
      expect.objectContaining({
        method: "POST",
        credentials: "include"
      })
    );
    expect(assignMock).toHaveBeenCalledWith("/app");
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
          sessionId: "session-1",
          organizationalLinks: {
            federationId: 1,
            singularId: null,
            areaId: null,
            teamId: null
          }
        }
      }
    });

    const user = await authService.fetchCurrentUser();
    expect(getMock).toHaveBeenCalledWith(AUTH_API_PATHS.me);
    expect(user.email).toBe("user@unimedceara.com.br");
    expect(user.organizationalLinks).toEqual({
      federationId: 1,
      singularId: null,
      areaId: null,
      teamId: null
    });
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
