import { defineStore } from "pinia";
import { computed, ref } from "vue";

import type {
  AuthenticatedUser,
  ColaboradorOrganizationalLinks
} from "@/auth/types";
import { authService } from "@/services/auth/auth.service";
import { normalizeApiError } from "@/types/api";

/**
 * Session hydration lifecycle — distinct from auth token cycle (auth-store).
 */
export type SessionHydrationStatus =
  | "idle"
  | "loading"
  | "ready"
  | "unauthenticated"
  | "error";

/**
 * Organizational context snapshot from COLABORADOR links (FT-SESSION fase 1).
 * Multi-context selection is out of scope (OQ-027).
 */
export type OrganizationalContext = ColaboradorOrganizationalLinks;

/**
 * Session store — authenticated user + organizational context.
 * Does not own login/logout/refresh (auth-store).
 */
export const useSessionStore = defineStore("session", () => {
  const status = ref<SessionHydrationStatus>("idle");
  const user = ref<AuthenticatedUser | null>(null);
  const availableContext = ref<OrganizationalContext | null>(null);
  const activeContext = ref<OrganizationalContext | null>(null);
  let hydrationPromise: Promise<void> | null = null;

  const isHydrated = computed(
    () => status.value === "ready" || status.value === "unauthenticated"
  );

  const isReady = computed(
    () => status.value === "ready" && user.value !== null
  );

  const permissions = computed(() => user.value?.permissions ?? []);

  const roles = computed(() => user.value?.roles ?? []);

  const organizationalLinks = computed(
    () => user.value?.organizationalLinks ?? null
  );

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

  function applyAuthenticatedUser(sessionUser: AuthenticatedUser): void {
    user.value = sessionUser;
    // Contract: organizationalLinks is required on GET /auth/me.
    const links: OrganizationalContext = sessionUser.organizationalLinks;
    availableContext.value = links;

    // RN-SESSION-002: single COLABORADOR link set → auto-resolve as active.
    // TODO(OQ-027): multi-context selection before protected business routes.
    // TODO(OQ-001 / OQ-026): first-access / onboarding when links are incomplete.
    // TODO(OQ-028): landing/dashboard route after session ready — not in this increment.
    activeContext.value = links;
    status.value = "ready";
  }

  function clear(): void {
    user.value = null;
    availableContext.value = null;
    activeContext.value = null;
    status.value = "unauthenticated";
  }

  function reset(): void {
    user.value = null;
    availableContext.value = null;
    activeContext.value = null;
    status.value = "idle";
    hydrationPromise = null;
  }

  /**
   * Hydrates session from GET /auth/me.
   * Deduplicates concurrent calls; skips when already ready unless `force`.
   */
  async function hydrate(options?: { force?: boolean }): Promise<void> {
    const force = options?.force === true;

    if (!force && status.value === "ready" && user.value !== null) {
      return;
    }

    if (hydrationPromise) {
      return hydrationPromise;
    }

    hydrationPromise = (async () => {
      status.value = "loading";
      try {
        const sessionUser = await authService.fetchCurrentUser();
        applyAuthenticatedUser(sessionUser);
      } catch (error) {
        const apiError = normalizeApiError(error);
        if (apiError.category === "authentication") {
          clear();
          return;
        }
        user.value = null;
        availableContext.value = null;
        activeContext.value = null;
        status.value = "error";
        throw error;
      }
    })();

    try {
      await hydrationPromise;
    } finally {
      hydrationPromise = null;
    }
  }

  return {
    status,
    user,
    availableContext,
    activeContext,
    isHydrated,
    isReady,
    permissions,
    roles,
    organizationalLinks,
    hasRole,
    hasAnyRole,
    hasCapability,
    hasAnyCapability,
    applyAuthenticatedUser,
    clear,
    reset,
    hydrate
  };
});
