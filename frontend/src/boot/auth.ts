import { defineBoot } from "#q-app";

import {
  assertNoTokenStorage,
  bindAuthStoreToContext,
  createUnauthorizedHandler,
  handleSessionExpired,
  installTokenStorageGuard,
  validateCsrfInfrastructure
} from "@/auth";
import { routerGuardConfig } from "@/config/router";
import { setUnauthorizedHandler } from "@/services/http";
import { useAuthStore } from "@/stores/auth-store";

export default defineBoot(() => {
  installTokenStorageGuard();
  assertNoTokenStorage();

  const authStore = useAuthStore();
  bindAuthStoreToContext(authStore);

  authStore.$subscribe(() => {
    bindAuthStoreToContext(authStore);
  });

  setUnauthorizedHandler(
    createUnauthorizedHandler({
      refreshSession: () => authStore.refreshSession(),
      onSessionExpired: () => {
        authStore.markUnauthenticated();
        if (routerGuardConfig.enforceAuthentication) {
          handleSessionExpired(window.location.pathname);
        }
      }
    })
  );

  if (import.meta.env.DEV) {
    const csrfReport = validateCsrfInfrastructure();
    console.info("[auth] CSRF infrastructure", csrfReport);
  }

  void authStore.hydrateSession();
});
