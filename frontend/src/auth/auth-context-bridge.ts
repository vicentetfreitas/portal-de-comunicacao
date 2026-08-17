import type { AuthContext } from "./auth-context";

import type { useAuthStore } from "@/stores/auth-store";
import type { useSessionStore } from "@/stores/session.store";

type AuthStore = ReturnType<typeof useAuthStore>;
type SessionStore = ReturnType<typeof useSessionStore>;

/**
 * Builds AuthContext from auth cycle + session context stores.
 */
export function createAuthContextFromStore(
  authStore: AuthStore,
  sessionStore: SessionStore
): AuthContext {
  return {
    isAuthenticated: () => authStore.isAuthenticated,
    hasRole: (role: string) => sessionStore.hasRole(role),
    hasAnyRole: (roles: readonly string[]) => sessionStore.hasAnyRole(roles),
    hasCapability: (capability: string) =>
      sessionStore.hasCapability(capability),
    hasAnyCapability: (capabilities: readonly string[]) =>
      sessionStore.hasAnyCapability(capabilities)
  };
}
