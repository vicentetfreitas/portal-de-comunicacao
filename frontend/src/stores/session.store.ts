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
  | "primeiroAcesso"
  | "blocked"
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
    () =>
      status.value === "ready" ||
      status.value === "primeiroAcesso" ||
      status.value === "blocked" ||
      status.value === "unauthenticated"
  );

  const isReady = computed(
    () =>
      status.value === "ready" &&
      user.value !== null &&
      !user.value.primeiroAcesso
  );

  const needsPrimeiroAcesso = computed(() => status.value === "primeiroAcesso");

  const isBlocked = computed(() => status.value === "blocked");

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

    if (sessionUser.primeiroAcesso || sessionUser.id == null) {
      availableContext.value = null;
      activeContext.value = null;
      const resolved = sessionUser.resolvedOrganization;
      const blocked =
        sessionUser.primeiroAcessoBlockCode === "PA_DOMAIN_NO_SINGULAR" ||
        resolved == null;
      status.value = blocked ? "blocked" : "primeiroAcesso";
      return;
    }

    const links: OrganizationalContext | null = sessionUser.organizationalLinks;
    availableContext.value = links;
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

    if (
      !force &&
      user.value !== null &&
      (status.value === "ready" ||
        status.value === "primeiroAcesso" ||
        status.value === "blocked")
    ) {
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
    needsPrimeiroAcesso,
    isBlocked,
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
