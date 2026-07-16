import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, it, vi } from "vitest";
import type { RouteLocationNormalized } from "vue-router";

import { resetAuthContext, setAuthContext } from "@/auth";
import { ROUTE_PATHS } from "@/constants/routes";
import { createAuthGuard } from "@/router/guards/auth.guard";
import { useAuthStore } from "@/stores/auth-store";
import { createNextMock } from "../../vitest/helpers";

const { fetchCurrentUser } = vi.hoisted(() => ({
  fetchCurrentUser: vi.fn()
}));

vi.mock("@/config/router", () => ({
  routerGuardConfig: {
    enforceAuthentication: true,
    enforceAuthorization: false
  },
  APP_DOCUMENT_TITLE_SUFFIX: "Portal de Comunicação"
}));

vi.mock("@/services/auth/auth.service", () => ({
  authService: {
    fetchCurrentUser
  }
}));

function createRoute(
  meta: Record<string, unknown>,
  fullPath = "/app"
): RouteLocationNormalized {
  return {
    meta,
    fullPath,
    query: {}
  } as RouteLocationNormalized;
}

describe("createAuthGuard", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    resetAuthContext();
    vi.clearAllMocks();
  });

  it("allows public navigation without authentication", async () => {
    fetchCurrentUser.mockRejectedValue(new Error("unauthorized"));
    const next = createNextMock();
    const guard = createAuthGuard();

    await guard(
      createRoute({ public: true }),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith();
  });

  it("redirects unauthenticated users from protected routes", async () => {
    fetchCurrentUser.mockRejectedValue(new Error("unauthorized"));

    const next = createNextMock();
    const guard = createAuthGuard();

    await guard(
      createRoute({ requiresAuth: true }, "/app"),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith({
      path: ROUTE_PATHS.AUTH,
      query: { redirect: "/app" }
    });
  });

  it("redirects authenticated users away from guest-only routes", async () => {
    fetchCurrentUser.mockResolvedValue({
      id: 1,
      email: "a@b.com",
      name: "User",
      permissions: [],
      sessionId: "s"
    });

    const store = useAuthStore();
    await store.hydrateSession();
    setAuthContext({
      isAuthenticated: () => store.isAuthenticated,
      hasRole: () => false,
      hasAnyRole: () => false,
      hasCapability: () => false,
      hasAnyCapability: () => false
    });

    const next = createNextMock();
    const guard = createAuthGuard();

    await guard(
      createRoute({ guestOnly: true }),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith(ROUTE_PATHS.APP);
  });
});
