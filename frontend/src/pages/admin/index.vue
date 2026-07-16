<template>
  <div class="admin-page">
    <DsPageHeader
      :title="$t('layout.admin.title')"
      :subtitle="$t('layout.admin.subtitle')"
    />

    <DsCard :title="$t('layout.admin.cardTitle')">
      <p class="admin-page__text">{{ $t("layout.admin.placeholder") }}</p>
    </DsCard>
  </div>
</template>

<script setup lang="ts">
import { DsCard, DsPageHeader } from "@/components/ds";
</script>

<style scoped lang="scss">
.admin-page {
  &__text {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    margin: 0;
  }
}
</style>
