import { defineBoot } from "#q-app";

import {
  assertNoTokenStorage,
  bindAuthStoreToContext,
  installTokenStorageGuard,
  validateCsrfInfrastructure
} from "@/auth";
import { useAuthStore } from "@/stores/auth-store";
import { setUnauthorizedHandler } from "@/services/http";

export default defineBoot(() => {
  installTokenStorageGuard();
  assertNoTokenStorage();

  const authStore = useAuthStore();
  bindAuthStoreToContext(authStore);

  authStore.$subscribe(() => {
    bindAuthStoreToContext(authStore);
  });

  setUnauthorizedHandler(async () => {
    authStore.markUnauthenticated();
    return false;
  });

  if (import.meta.env.DEV) {
    const csrfReport = validateCsrfInfrastructure();
    console.info("[auth] CSRF infrastructure", csrfReport);
  }

  void authStore.hydrateSession();
});
