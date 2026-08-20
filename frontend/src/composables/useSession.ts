import { storeToRefs } from "pinia";

import { useSessionStore } from "@/stores/session.store";

/**
 * Composable for FT-SESSION context — no auth cycle (login/logout/refresh).
 */
export function useSession() {
  const store = useSessionStore();
  const {
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
    organizationalLinks
  } = storeToRefs(store);

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
    hasRole: store.hasRole,
    hasAnyRole: store.hasAnyRole,
    hasCapability: store.hasCapability,
    hasAnyCapability: store.hasAnyCapability,
    hydrate: store.hydrate,
    clear: store.clear
  };
}
