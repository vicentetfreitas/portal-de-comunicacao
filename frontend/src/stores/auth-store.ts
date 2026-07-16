import { defineStore } from "pinia";
import { computed, ref } from "vue";

import type { AuthenticatedUser, AuthSessionStatus } from "@/auth/types";

/**
 * Structural auth store — session state without login/logout API calls.
 * FT-AUTH completes hydration via /auth/me and refresh flows.
 */
export const useAuthStore = defineStore("auth", () => {
  const status = ref<AuthSessionStatus>("idle");
  const user = ref<AuthenticatedUser | null>(null);

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

  /**
   * FT-AUTH will call GET /auth/me and populate session from HttpOnly cookies.
   */
  async function hydrateSession(): Promise<void> {
    setLoading();
    status.value = "unauthenticated";
    user.value = null;
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
    hydrateSession
  };
});
