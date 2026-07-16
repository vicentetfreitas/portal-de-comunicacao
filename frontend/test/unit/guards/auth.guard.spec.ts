import { beforeEach, describe, expect, it, vi } from "vitest";
import type { RouteLocationNormalized } from "vue-router";

import { resetAuthContext, setAuthContext } from "@/auth";
import { ROUTE_PATHS } from "@/constants/routes";
import { createAuthGuard } from "@/router/guards/auth.guard";
import { createNextMock } from "../../vitest/helpers";

vi.mock("@/config/router", () => ({
  routerGuardConfig: {
    enforceAuthentication: true,
    enforceAuthorization: false
  },
  APP_DOCUMENT_TITLE_SUFFIX: "Portal de Comunicação"
}));

function createRoute(
  meta: Record<string, unknown>,
  fullPath = "/app"
): RouteLocationNormalized {
  return {
    meta,
    fullPath
  } as RouteLocationNormalized;
}

describe("createAuthGuard", () => {
  beforeEach(() => {
    resetAuthContext();
  });

  it("allows public navigation without authentication", () => {
    const next = createNextMock();
    const guard = createAuthGuard();

    guard(createRoute({ public: true }), {} as RouteLocationNormalized, next);

    expect(next).toHaveBeenCalledWith();
  });

  it("redirects unauthenticated users from protected routes", () => {
    const next = createNextMock();
    const guard = createAuthGuard();

    guard(
      createRoute({ requiresAuth: true }, "/app"),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith({
      path: ROUTE_PATHS.AUTH,
      query: { redirect: "/app" }
    });
  });

  it("redirects authenticated users away from guest-only routes", () => {
    setAuthContext({
      isAuthenticated: () => true,
      hasRole: () => false,
      hasAnyRole: () => false,
      hasCapability: () => false,
      hasAnyCapability: () => false
    });

    const next = createNextMock();
    const guard = createAuthGuard();

    guard(
      createRoute({ guestOnly: true }),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith(ROUTE_PATHS.APP);
  });
});
