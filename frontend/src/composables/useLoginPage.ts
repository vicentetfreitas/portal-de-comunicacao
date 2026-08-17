import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute } from "vue-router";

import { parseAuthErrorCode } from "@/auth";
import { useAuth } from "@/composables/useAuth";
import { normalizeApiError, type ApiError } from "@/types/api";

export interface LoginFieldErrors {
  usuario?: string;
  senha?: string;
}

export function useLoginPage() {
  const { t } = useI18n();
  const route = useRoute();
  const { login } = useAuth();

  const usuario = ref("");
  const senha = ref("");
  const rememberMe = ref(false);
  const isSubmitting = ref(false);
  const fieldErrors = ref<LoginFieldErrors>({});
  const submitErrorMessage = ref("");

  const authErrorCode = computed(() => parseAuthErrorCode(route.query.error));

  function resolveSubmitErrorMessage(error: ApiError): string {
    if (error.code === "FORBIDDEN") {
      return t("layout.auth.errors.portalAccessDenied");
    }

    switch (error.category) {
      case "authentication":
        return t("layout.auth.errors.invalidCredentials");
      case "authorization":
        return t("layout.auth.errors.forbidden");
      case "network":
      case "server":
        return t("layout.auth.errors.unavailable");
      default:
        return t("layout.auth.errors.unknown");
    }
  }

  const bannerMessage = computed(() => {
    if (submitErrorMessage.value) {
      return submitErrorMessage.value;
    }

    switch (authErrorCode.value) {
      case "unauthorized":
        return t("layout.auth.errors.unauthorized");
      case "forbidden":
        return t("layout.auth.errors.forbidden");
      case "unavailable":
        return t("layout.auth.errors.unavailable");
      case "unknown":
        return t("layout.auth.errors.unknown");
      default:
        return "";
    }
  });

  const isSubmitDisabled = computed(
    () =>
      isSubmitting.value ||
      usuario.value.trim().length === 0 ||
      senha.value.trim().length === 0
  );

  function validateFields(): boolean {
    const next: LoginFieldErrors = {};

    if (usuario.value.trim().length === 0) {
      next.usuario = t("layout.auth.figma.errors.userRequired");
    }
    if (senha.value.trim().length === 0) {
      next.senha = t("layout.auth.figma.errors.passwordHint");
    }

    fieldErrors.value = next;
    return Object.keys(next).length === 0;
  }

  async function handleSubmit(): Promise<void> {
    fieldErrors.value = {};

    if (!validateFields()) {
      return;
    }

    submitErrorMessage.value = "";
    isSubmitting.value = true;
    try {
      const credentials: {
        rememberMe: boolean;
        email: string;
        password: string;
        state?: string;
      } = {
        rememberMe: rememberMe.value,
        email: usuario.value.trim(),
        password: senha.value
      };

      if (typeof route.query.state === "string") {
        credentials.state = route.query.state;
      }

      await login(credentials);
    } catch (error) {
      submitErrorMessage.value = resolveSubmitErrorMessage(
        normalizeApiError(error)
      );
      isSubmitting.value = false;
    }
  }

  return {
    usuario,
    senha,
    rememberMe,
    isSubmitting,
    fieldErrors,
    bannerMessage,
    authErrorCode,
    isSubmitDisabled,
    handleSubmit
  };
}
