import { computed } from "vue";
import { storeToRefs } from "pinia";

import { useAuthStore } from "@/stores/auth-store";

/**
 * Composable facade for auth store — UI and guards infrastructure.
 */
export function useAuth() {
  const store = useAuthStore();
  const { status, user, isAuthenticated, permissions, roles } =
    storeToRefs(store);

  return {
    status,
    user,
    isAuthenticated,
    permissions,
    roles,
    hasRole: store.hasRole,
    hasAnyRole: store.hasAnyRole,
    hasCapability: store.hasCapability,
    hasAnyCapability: store.hasAnyCapability,
    setSession: store.setSession,
    clearSession: store.clearSession,
    markUnauthenticated: store.markUnauthenticated,
    hydrateSession: store.hydrateSession,
    isReady: computed(
      () => status.value !== "idle" && status.value !== "loading"
    )
  };
}
