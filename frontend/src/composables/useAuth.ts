import { computed } from "vue";
import { storeToRefs } from "pinia";

import { useAuthStore } from "@/stores/auth-store";
import { useSessionStore } from "@/stores/session.store";

/**
 * Composable facade — auth cycle (auth-store) + session context (session.store).
 */
export function useAuth() {
  const authStore = useAuthStore();
  const sessionStore = useSessionStore();

  const { status, isAuthenticated } = storeToRefs(authStore);
  const { user, permissions, roles } = storeToRefs(sessionStore);

  return {
    status,
    user,
    isAuthenticated,
    permissions,
    roles,
    hasRole: sessionStore.hasRole,
    hasAnyRole: sessionStore.hasAnyRole,
    hasCapability: sessionStore.hasCapability,
    hasAnyCapability: sessionStore.hasAnyCapability,
    markUnauthenticated: () => {
      sessionStore.clear();
      authStore.markUnauthenticated();
    },
    hydrateSession: authStore.hydrateSession,
    login: authStore.login,
    logout: authStore.logout,
    refreshSession: authStore.refreshSession,
    isReady: computed(
      () => status.value !== "idle" && status.value !== "loading"
    )
  };
}
