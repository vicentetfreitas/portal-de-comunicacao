import type { AuthContext } from "./auth-context";
import type { useAuthStore } from "@/stores/auth-store";

type AuthStore = ReturnType<typeof useAuthStore>;

export function createAuthContextFromStore(store: AuthStore): AuthContext {
  return {
    isAuthenticated: () => store.isAuthenticated,
    hasRole: (role: string) => store.hasRole(role),
    hasAnyRole: (roles: readonly string[]) => store.hasAnyRole(roles),
    hasCapability: (capability: string) => store.hasCapability(capability),
    hasAnyCapability: (capabilities: readonly string[]) =>
      store.hasAnyCapability(capabilities)
  };
}
