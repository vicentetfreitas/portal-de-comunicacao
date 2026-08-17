import { createAuthContextFromStore } from "./auth-context-bridge";
import { setAuthContext } from "./auth-context";

import type { useAuthStore } from "@/stores/auth-store";
import type { useSessionStore } from "@/stores/session.store";

type AuthStore = ReturnType<typeof useAuthStore>;
type SessionStore = ReturnType<typeof useSessionStore>;

/**
 * Synchronizes global auth context with auth + session Pinia stores.
 */
export function bindAuthStoreToContext(
  authStore: AuthStore,
  sessionStore: SessionStore
): void {
  setAuthContext(createAuthContextFromStore(authStore, sessionStore));
}
