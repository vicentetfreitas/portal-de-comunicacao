<template>
  <div class="app-page">
    <DsPageHeader
      :title="$t('layout.app.title')"
      :subtitle="$t('layout.app.subtitle')"
    />

    <DsCard :title="$t('layout.app.cardTitle')">
      <p class="app-page__text">{{ $t("layout.app.placeholder") }}</p>
      <template #actions>
        <DsButton variant="primary" :to="ROUTE_PATHS.SHOWCASE">
          {{ $t("layout.nav.showcase") }}
        </DsButton>
      </template>
    </DsCard>
  </div>
</template>

<script setup lang="ts">
import { DsButton, DsCard, DsPageHeader } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";
</script>

<style scoped lang="scss">
.app-page {
  &__text {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    margin: 0;
  }
}
</style>
