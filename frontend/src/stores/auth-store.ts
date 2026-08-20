import { defineStore } from "pinia";
import { computed, ref } from "vue";

import { redirectAfterLogout } from "@/auth/session-redirect";
import type { AuthSessionStatus } from "@/auth/types";
import { authService } from "@/services/auth/auth.service";
import { useSessionStore } from "@/stores/session.store";
import { normalizeApiError } from "@/types/api";

/**
 * Auth store — authentication and token cycle only.
 * User/context state lives in session.store (FT-SESSION).
 */
export const useAuthStore = defineStore("auth", () => {
  const status = ref<AuthSessionStatus>("idle");
  let hydrationPromise: Promise<void> | null = null;

  const isAuthenticated = computed(() => status.value === "authenticated");

  function markAuthenticated(): void {
    status.value = "authenticated";
  }

  function markUnauthenticated(): void {
    status.value = "unauthenticated";
  }

  function setLoading(): void {
    status.value = "loading";
  }

  /**
   * Bootstraps session via /auth/me (session.store) and syncs auth status.
   * Keeps a single hydration entry-point for boot + guards.
   */
  async function hydrateSession(options?: { force?: boolean }): Promise<void> {
    if (hydrationPromise) {
      return hydrationPromise;
    }

    const sessionStore = useSessionStore();
    const force = options?.force === true;

    if (
      !force &&
      (sessionStore.needsPrimeiroAcesso || sessionStore.isBlocked) &&
      status.value === "authenticated"
    ) {
      return;
    }

    if (!force && sessionStore.isReady && status.value === "authenticated") {
      return;
    }

    hydrationPromise = (async () => {
      setLoading();
      try {
        await sessionStore.hydrate(options);
        if (
          sessionStore.isReady ||
          sessionStore.needsPrimeiroAcesso ||
          sessionStore.isBlocked
        ) {
          markAuthenticated();
          return;
        }
        markUnauthenticated();
      } catch (error) {
        markUnauthenticated();
        throw error;
      }
    })();

    try {
      await hydrationPromise;
    } finally {
      hydrationPromise = null;
    }
  }

  function login(options?: {
    rememberMe?: boolean;
    email?: string;
    password?: string;
    state?: string;
  }): void | Promise<void> {
    return authService.login(options);
  }

  async function logout(): Promise<void> {
    const sessionStore = useSessionStore();
    try {
      await authService.logout();
    } catch (error) {
      const apiError = normalizeApiError(error);
      if (apiError.category !== "authentication") {
        throw error;
      }
    } finally {
      sessionStore.clear();
      markUnauthenticated();
      redirectAfterLogout();
    }
  }

  async function refreshSession(): Promise<boolean> {
    const sessionStore = useSessionStore();
    const refreshed = await authService.refresh();
    if (!refreshed) {
      sessionStore.clear();
      markUnauthenticated();
      return false;
    }

    try {
      await sessionStore.hydrate({ force: true });
      if (
        sessionStore.isReady ||
        sessionStore.needsPrimeiroAcesso ||
        sessionStore.isBlocked
      ) {
        markAuthenticated();
        return true;
      }
      markUnauthenticated();
      return false;
    } catch (error) {
      if (normalizeApiError(error).category === "authentication") {
        sessionStore.clear();
        markUnauthenticated();
        return false;
      }
      throw error;
    }
  }

  return {
    status,
    isAuthenticated,
    markAuthenticated,
    markUnauthenticated,
    setLoading,
    hydrateSession,
    login,
    logout,
    refreshSession
  };
});
