import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { useSessionStore } from "@/stores/session.store";
import { ApiError } from "@/types/api";

const { fetchCurrentUser } = vi.hoisted(() => ({
  fetchCurrentUser: vi.fn()
}));

vi.mock("@/services/auth/auth.service", () => ({
  authService: {
    fetchCurrentUser
  }
}));

const sampleUser = {
  id: 10,
  email: "user@unimedceara.com.br",
  name: "João",
  permissions: ["DOCUMENT_READ"],
  sessionId: "abc",
  primeiroAcesso: false,
  roles: ["EDITOR"],
  organizationalLinks: {
    federationId: 1,
    singularId: 2,
    areaId: 3,
    teamId: 4
  }
};

describe("useSessionStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  it("starts with session not loaded", () => {
    const store = useSessionStore();

    expect(store.status).toBe("idle");
    expect(store.isHydrated).toBe(false);
    expect(store.isReady).toBe(false);
    expect(store.user).toBeNull();
    expect(store.activeContext).toBeNull();
  });

  it("hydrates session from /auth/me and auto-resolves active context", async () => {
    fetchCurrentUser.mockResolvedValue(sampleUser);

    const store = useSessionStore();
    await store.hydrate();

    expect(store.status).toBe("ready");
    expect(store.isReady).toBe(true);
    expect(store.isHydrated).toBe(true);
    expect(store.user?.name).toBe("João");
    expect(store.availableContext).toEqual(sampleUser.organizationalLinks);
    expect(store.activeContext).toEqual(sampleUser.organizationalLinks);
    expect(store.permissions).toEqual(["DOCUMENT_READ"]);
    expect(store.hasRole("EDITOR")).toBe(true);
    expect(store.hasCapability("DOCUMENT_READ")).toBe(true);
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

    const store = useSessionStore();
    await store.hydrate();

    expect(store.status).toBe("unauthenticated");
    expect(store.isReady).toBe(false);
    expect(store.isHydrated).toBe(true);
    expect(store.user).toBeNull();
    expect(store.activeContext).toBeNull();
  });

  it("sets error status and rethrows non-auth failures", async () => {
    fetchCurrentUser.mockRejectedValue(
      new ApiError({
        status: 500,
        code: "INTERNAL",
        message: "boom",
        category: "server"
      })
    );

    const store = useSessionStore();
    await expect(store.hydrate()).rejects.toMatchObject({ status: 500 });

    expect(store.status).toBe("error");
    expect(store.isReady).toBe(false);
    expect(store.user).toBeNull();
  });

  it("avoids duplicate hydration when session is already ready", async () => {
    fetchCurrentUser.mockResolvedValue(sampleUser);

    const store = useSessionStore();
    await store.hydrate();
    await store.hydrate();

    expect(fetchCurrentUser).toHaveBeenCalledTimes(1);
  });

  it("deduplicates concurrent hydrate calls", async () => {
    let resolveFetch: (value: typeof sampleUser) => void = () => undefined;
    fetchCurrentUser.mockImplementation(
      () =>
        new Promise(resolve => {
          resolveFetch = resolve;
        })
    );

    const store = useSessionStore();
    const first = store.hydrate();
    const second = store.hydrate();

    resolveFetch(sampleUser);
    await Promise.all([first, second]);

    expect(fetchCurrentUser).toHaveBeenCalledTimes(1);
    expect(store.isReady).toBe(true);
  });

  it("force rehydrates after a prior successful load", async () => {
    fetchCurrentUser.mockResolvedValueOnce(sampleUser).mockResolvedValueOnce({
      ...sampleUser,
      name: "Maria"
    });

    const store = useSessionStore();
    await store.hydrate();
    await store.hydrate({ force: true });

    expect(fetchCurrentUser).toHaveBeenCalledTimes(2);
    expect(store.user?.name).toBe("Maria");
  });

  it("clears session state", async () => {
    fetchCurrentUser.mockResolvedValue(sampleUser);

    const store = useSessionStore();
    await store.hydrate();
    store.clear();

    expect(store.status).toBe("unauthenticated");
    expect(store.user).toBeNull();
    expect(store.availableContext).toBeNull();
    expect(store.activeContext).toBeNull();
    expect(store.isReady).toBe(false);
  });

  it("marks primeiro acesso when Singular is resolved", async () => {
    fetchCurrentUser.mockResolvedValue({
      id: null,
      email: "novo@unimedceara.com.br",
      name: "Novo",
      permissions: [],
      sessionId: null,
      primeiroAcesso: true,
      organizationalLinks: null,
      resolvedOrganization: { singularId: 10, federationId: 1 }
    });

    const store = useSessionStore();
    await store.hydrate();

    expect(store.status).toBe("primeiroAcesso");
    expect(store.needsPrimeiroAcesso).toBe(true);
    expect(store.isBlocked).toBe(false);
    expect(store.isReady).toBe(false);
    expect(store.isHydrated).toBe(true);
    expect(store.user?.email).toBe("novo@unimedceara.com.br");
    expect(store.activeContext).toBeNull();
  });

  it("marks blocked when domain has no Singular", async () => {
    fetchCurrentUser.mockResolvedValue({
      id: null,
      email: "novo@desconhecido.test",
      name: "Novo",
      permissions: [],
      sessionId: null,
      primeiroAcesso: true,
      organizationalLinks: null,
      primeiroAcessoBlockCode: "PA_DOMAIN_NO_SINGULAR"
    });

    const store = useSessionStore();
    await store.hydrate();

    expect(store.status).toBe("blocked");
    expect(store.isBlocked).toBe(true);
    expect(store.needsPrimeiroAcesso).toBe(false);
    expect(store.isReady).toBe(false);
    expect(store.isHydrated).toBe(true);
  });
});
