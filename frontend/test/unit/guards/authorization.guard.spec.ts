import { beforeEach, describe, expect, it, vi } from "vitest";
import type { RouteLocationNormalized } from "vue-router";

import { resetAuthContext, setAuthContext } from "@/auth";
import { ROUTE_PATHS } from "@/constants/routes";
import { createAuthorizationGuard } from "@/router/guards/authorization.guard";
import { createNextMock } from "../../vitest/helpers";

vi.mock("@/config/router", () => ({
  routerGuardConfig: {
    enforceAuthentication: false,
    enforceAuthorization: true
  },
  APP_DOCUMENT_TITLE_SUFFIX: "Portal de Comunicação"
}));

function createRoute(meta: Record<string, unknown>): RouteLocationNormalized {
  return {
    meta
  } as RouteLocationNormalized;
}

describe("createAuthorizationGuard", () => {
  beforeEach(() => {
    resetAuthContext();
  });

  it("allows navigation when no roles or capabilities are required", () => {
    const next = createNextMock();
    const guard = createAuthorizationGuard();

    guard(createRoute({}), {} as RouteLocationNormalized, next);

    expect(next).toHaveBeenCalledWith();
  });

  it("redirects to unauthorized when required role is missing", () => {
    setAuthContext({
      isAuthenticated: () => true,
      hasRole: () => false,
      hasAnyRole: () => false,
      hasCapability: () => false,
      hasAnyCapability: () => false
    });

    const next = createNextMock();
    const guard = createAuthorizationGuard();

    guard(
      createRoute({ roles: ["ADMIN"] }),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith(ROUTE_PATHS.UNAUTHORIZED);
  });

  it("allows navigation when required capability is present", () => {
    setAuthContext({
      isAuthenticated: () => true,
      hasRole: () => false,
      hasAnyRole: () => false,
      hasCapability: capability => capability === "DOCUMENT_READ",
      hasAnyCapability: capabilities => capabilities.includes("DOCUMENT_READ")
    });

    const next = createNextMock();
    const guard = createAuthorizationGuard();

    guard(
      createRoute({ capabilities: ["DOCUMENT_READ"] }),
      {} as RouteLocationNormalized,
      next
    );

    expect(next).toHaveBeenCalledWith();
  });
});
