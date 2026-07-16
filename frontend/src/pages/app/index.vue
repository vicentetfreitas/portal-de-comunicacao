<template>
  <div class="app-page">
    <DsPageHeader :title="$t('layout.app.title')" :subtitle="pageSubtitle" />

    <DsCard :title="$t('layout.app.cardTitle')">
      <p class="app-page__text">{{ $t("layout.app.placeholder") }}</p>
      <p v-if="user" class="app-page__welcome">
        {{ $t("layout.app.welcome", { name: user.name }) }}
      </p>
      <template #actions>
        <DsButton variant="primary" :to="ROUTE_PATHS.SHOWCASE">
          {{ $t("layout.nav.showcase") }}
        </DsButton>
      </template>
    </DsCard>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsCard, DsPageHeader } from "@/components/ds";
import { useAuth } from "@/composables/useAuth";
import { ROUTE_PATHS } from "@/constants/routes";

const { t } = useI18n();
const { user } = useAuth();

const pageSubtitle = computed(() =>
  user.value?.email
    ? `${t("layout.app.subtitle")} — ${user.value.email}`
    : t("layout.app.subtitle")
);
</script>

<style scoped lang="scss">
.app-page {
  &__text,
  &__welcome {
    color: var(--color-text-secondary);
    font-size: var(--text-body-small-size);
    margin: 0 0 var(--spacing-sm);
  }

  &__welcome {
    color: var(--color-text-primary);
    font-weight: var(--font-weight-medium);
  }
}
</style>
