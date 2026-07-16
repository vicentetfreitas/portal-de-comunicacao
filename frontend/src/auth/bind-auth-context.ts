import { createAuthContextFromStore } from "./auth-context-bridge";
import { setAuthContext } from "./auth-context";

import type { useAuthStore } from "@/stores/auth-store";

type AuthStore = ReturnType<typeof useAuthStore>;

/**
 * Synchronizes global auth context with the Pinia auth store.
 */
export function bindAuthStoreToContext(store: AuthStore): void {
  setAuthContext(createAuthContextFromStore(store));
}
