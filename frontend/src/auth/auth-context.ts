/**
 * Auth context — bridge for router guards and HTTP hooks.
 * Backed by the auth Pinia store (PKG-FE-S0-07).
 */
export interface AuthContext {
  isAuthenticated: () => boolean;
  hasRole: (role: string) => boolean;
  hasAnyRole: (roles: readonly string[]) => boolean;
  hasCapability: (capability: string) => boolean;
  hasAnyCapability: (capabilities: readonly string[]) => boolean;
}

const defaultContext: AuthContext = {
  isAuthenticated: () => false,
  hasRole: () => false,
  hasAnyRole: () => false,
  hasCapability: () => false,
  hasAnyCapability: () => false
};

export const authContext: AuthContext = { ...defaultContext };

/**
 * Binds router guards and authorization checks to a live auth context implementation.
 */
export function setAuthContext(implementation: AuthContext): void {
  Object.assign(authContext, implementation);
}

export function resetAuthContext(): void {
  Object.assign(authContext, defaultContext);
}
