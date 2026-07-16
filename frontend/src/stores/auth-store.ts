import { defineStore } from "pinia";
import { computed, ref } from "vue";

import { redirectAfterLogout } from "@/auth/session-redirect";
import type { AuthenticatedUser, AuthSessionStatus } from "@/auth/types";
import { authService } from "@/services/auth/auth.service";
import { normalizeApiError } from "@/types/api";

/**
 * Auth store — session state derived from HttpOnly cookies via /auth/me.
 */
export const useAuthStore = defineStore("auth", () => {
  const status = ref<AuthSessionStatus>("idle");
  const user = ref<AuthenticatedUser | null>(null);
  let hydrationPromise: Promise<void> | null = null;

  const isAuthenticated = computed(
    () => status.value === "authenticated" && user.value !== null
  );

  const permissions = computed(() => user.value?.permissions ?? []);

  const roles = computed(() => user.value?.roles ?? []);

  function hasRole(role: string): boolean {
    return roles.value.includes(role);
  }

  function hasAnyRole(requiredRoles: readonly string[]): boolean {
    if (requiredRoles.length === 0) {
      return true;
    }
    return requiredRoles.some(role => hasRole(role));
  }

  function hasCapability(capability: string): boolean {
    return permissions.value.includes(capability);
  }

  function hasAnyCapability(requiredCapabilities: readonly string[]): boolean {
    if (requiredCapabilities.length === 0) {
      return true;
    }
    return requiredCapabilities.some(capability => hasCapability(capability));
  }

  function setSession(sessionUser: AuthenticatedUser): void {
    user.value = sessionUser;
    status.value = "authenticated";
  }

  function clearSession(): void {
    user.value = null;
    status.value = "unauthenticated";
  }

  function markUnauthenticated(): void {
    clearSession();
  }

  function setLoading(): void {
    status.value = "loading";
  }

  async function hydrateSession(): Promise<void> {
    if (hydrationPromise) {
      return hydrationPromise;
    }

    hydrationPromise = (async () => {
      setLoading();
      try {
        const sessionUser = await authService.fetchCurrentUser();
        setSession(sessionUser);
      } catch (error) {
        const apiError = normalizeApiError(error);
        if (apiError.category === "authentication") {
          markUnauthenticated();
          return;
        }
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

  function login(options?: { rememberMe?: boolean }): void {
    authService.login(options);
  }

  async function logout(): Promise<void> {
    try {
      await authService.logout();
    } catch (error) {
      const apiError = normalizeApiError(error);
      if (apiError.category !== "authentication") {
        throw error;
      }
    } finally {
      clearSession();
      redirectAfterLogout();
    }
  }

  async function refreshSession(): Promise<boolean> {
    const refreshed = await authService.refresh();
    if (!refreshed) {
      markUnauthenticated();
      return false;
    }

    try {
      const sessionUser = await authService.fetchCurrentUser();
      setSession(sessionUser);
      return true;
    } catch (error) {
      if (normalizeApiError(error).category === "authentication") {
        markUnauthenticated();
        return false;
      }
      throw error;
    }
  }

  return {
    status,
    user,
    isAuthenticated,
    permissions,
    roles,
    hasRole,
    hasAnyRole,
    hasCapability,
    hasAnyCapability,
    setSession,
    clearSession,
    markUnauthenticated,
    setLoading,
    hydrateSession,
    login,
    logout,
    refreshSession
  };
});
