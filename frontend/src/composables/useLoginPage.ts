import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute } from "vue-router";

import { parseAuthErrorCode } from "@/auth";
import { useAuth } from "@/composables/useAuth";

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

  const authErrorCode = computed(() => parseAuthErrorCode(route.query.error));

  const bannerMessage = computed(() => {
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

    isSubmitting.value = true;
    try {
      await login({
        rememberMe: rememberMe.value,
        email: usuario.value.trim(),
        password: senha.value,
        state:
          typeof route.query.state === "string" ? route.query.state : undefined
      });
    } catch {
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
