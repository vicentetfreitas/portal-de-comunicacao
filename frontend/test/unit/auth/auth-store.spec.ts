import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { useAuthStore } from "@/stores/auth-store";
import { useSessionStore } from "@/stores/session.store";
import { ApiError } from "@/types/api";

const { fetchCurrentUser, refresh, logout } = vi.hoisted(() => ({
  fetchCurrentUser: vi.fn(),
  refresh: vi.fn(),
  logout: vi.fn()
}));

vi.mock("@/services/auth/auth.service", () => ({
  authService: {
    fetchCurrentUser,
    refresh,
    logout,
    login: vi.fn()
  }
}));

vi.mock("@/auth/session-redirect", () => ({
  redirectAfterLogout: vi.fn()
}));

const sampleUser = {
  id: 10,
  email: "user@unimedceara.com.br",
  name: "João",
  permissions: [],
  sessionId: "abc",
  organizationalLinks: {
    federationId: 1,
    singularId: 2,
    areaId: 3,
    teamId: null
  }
};

describe("useAuthStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  it("hydrates authenticated session via session.store", async () => {
    fetchCurrentUser.mockResolvedValue(sampleUser);

    const authStore = useAuthStore();
    const sessionStore = useSessionStore();
    await authStore.hydrateSession();

    expect(authStore.isAuthenticated).toBe(true);
    expect(authStore.status).toBe("authenticated");
    expect(sessionStore.user?.name).toBe("João");
    expect(sessionStore.activeContext?.singularId).toBe(2);
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

    const authStore = useAuthStore();
    const sessionStore = useSessionStore();
    await authStore.hydrateSession();

    expect(authStore.isAuthenticated).toBe(false);
    expect(authStore.status).toBe("unauthenticated");
    expect(sessionStore.status).toBe("unauthenticated");
    expect(sessionStore.user).toBeNull();
  });

  it("refreshes token and rehydrates session", async () => {
    refresh.mockResolvedValue(true);
    fetchCurrentUser.mockResolvedValue(sampleUser);

    const authStore = useAuthStore();
    const sessionStore = useSessionStore();
    await expect(authStore.refreshSession()).resolves.toBe(true);

    expect(authStore.isAuthenticated).toBe(true);
    expect(sessionStore.isReady).toBe(true);
  });

  it("clears session on logout", async () => {
    fetchCurrentUser.mockResolvedValue(sampleUser);
    logout.mockResolvedValue(undefined);

    const authStore = useAuthStore();
    const sessionStore = useSessionStore();
    await authStore.hydrateSession();
    await authStore.logout();

    expect(authStore.isAuthenticated).toBe(false);
    expect(sessionStore.user).toBeNull();
    expect(sessionStore.status).toBe("unauthenticated");
  });

  it("does not store user data in auth-store", async () => {
    fetchCurrentUser.mockResolvedValue(sampleUser);

    const authStore = useAuthStore();
    await authStore.hydrateSession();

    expect(authStore).not.toHaveProperty("user");
  });
});
