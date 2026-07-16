<template>
  <DsCard
    :title="$t('layout.auth.title')"
    :subtitle="$t('layout.auth.subtitle')"
    class="auth-page"
  >
    <p class="auth-page__description">{{ $t("layout.auth.description") }}</p>

    <q-banner
      v-if="errorMessage"
      class="auth-page__banner q-mb-md"
      dense
      rounded
      :class="errorBannerClass"
    >
      {{ errorMessage }}
    </q-banner>

    <q-checkbox
      v-model="rememberMe"
      class="auth-page__remember q-mb-lg"
      :label="$t('layout.auth.rememberMe')"
      dense
    />

    <DsButton
      variant="primary"
      class="full-width"
      size="lg"
      :loading="isSubmitting"
      @click="handleLogin"
    >
      {{ $t("layout.auth.loginAction") }}
    </DsButton>

    <template #actions>
      <DsButton variant="ghost" :to="ROUTE_PATHS.HOME">
        {{ $t("layout.notFound.goHome") }}
      </DsButton>
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute } from "vue-router";

import { parseAuthErrorCode } from "@/auth";
import { DsButton, DsCard } from "@/components/ds";
import { useAuth } from "@/composables/useAuth";
import { ROUTE_PATHS } from "@/constants/routes";

const { t } = useI18n();
const route = useRoute();
const { login } = useAuth();

const rememberMe = ref(false);
const isSubmitting = ref(false);

const authErrorCode = computed(() => parseAuthErrorCode(route.query.error));

const errorMessage = computed(() => {
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

const errorBannerClass = computed(() => {
  if (authErrorCode.value === "forbidden") {
    return "auth-page__banner--warning";
  }
  return "auth-page__banner--error";
});

function handleLogin(): void {
  isSubmitting.value = true;
  login({ rememberMe: rememberMe.value });
}
</script>

<style scoped lang="scss">
.auth-page {
  &__description {
    margin: 0 0 var(--spacing-md);
    color: var(--color-text-secondary);
    font-size: var(--text-body-size);
    line-height: var(--text-body-line-height);
  }

  &__remember {
    color: var(--color-text-primary);
    font-size: var(--text-body-small-size);
  }

  &__banner {
    font-size: var(--text-body-small-size);

    &--error {
      background-color: rgb(198 40 40 / 0.08);
      color: var(--color-error);
    }

    &--warning {
      background-color: rgb(239 108 0 / 0.08);
      color: var(--color-warning);
    }
  }
}
</style>
