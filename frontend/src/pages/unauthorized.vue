<template>
  <div class="unauthorized-page">
    <DsPageHeader
      :title="$t('layout.unauthorized.title')"
      :subtitle="$t('layout.unauthorized.subtitle')"
    />
    <q-banner class="unauthorized-page__banner" dense rounded>
      {{ $t("layout.unauthorized.message") }}
    </q-banner>
    <DsButton variant="primary" :to="ROUTE_PATHS.HOME">
      {{ $t("layout.notFound.goHome") }}
    </DsButton>
  </div>
</template>

<script setup lang="ts">
import { DsButton, DsPageHeader } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";
</script>

<style scoped lang="scss">
.unauthorized-page {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: var(--spacing-md);

  &__banner {
    width: 100%;
    background-color: rgb(239 108 0 / 0.08);
    color: var(--color-warning);
    font-size: var(--text-body-small-size);
  }
}
</style>
