import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { useAuthStore } from "@/stores/auth-store";
import { ApiError } from "@/types/api";

const { fetchCurrentUser, refresh } = vi.hoisted(() => ({
  fetchCurrentUser: vi.fn(),
  refresh: vi.fn()
}));

vi.mock("@/services/auth/auth.service", () => ({
  authService: {
    fetchCurrentUser,
    refresh,
    logout: vi.fn(),
    login: vi.fn()
  }
}));

vi.mock("@/auth/session-redirect", () => ({
  redirectAfterLogout: vi.fn()
}));

describe("useAuthStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  it("hydrates authenticated session from /auth/me", async () => {
    fetchCurrentUser.mockResolvedValue({
      id: 10,
      email: "user@unimedceara.com.br",
      name: "João",
      permissions: [],
      sessionId: "abc"
    });

    const store = useAuthStore();
    await store.hydrateSession();

    expect(store.isAuthenticated).toBe(true);
    expect(store.user?.name).toBe("João");
  });

  it("marks unauthenticated when /auth/me returns 401", async () => {
    fetchCurrentUser.mockRejectedValue(
      new ApiError({
        status: 401,
        code: "UNAUTHORIZED",
        message: "unauthorized",
        category: "authentication"
      })
    );

    const store = useAuthStore();
    await store.hydrateSession();

    expect(store.isAuthenticated).toBe(false);
    expect(store.status).toBe("unauthenticated");
  });

  it("refreshes session and reloads user", async () => {
    refresh.mockResolvedValue(true);
    fetchCurrentUser.mockResolvedValue({
      id: 1,
      email: "a@b.com",
      name: "Ana",
      permissions: [],
      sessionId: "s1"
    });

    const store = useAuthStore();
    await expect(store.refreshSession()).resolves.toBe(true);
    expect(store.isAuthenticated).toBe(true);
  });
});
