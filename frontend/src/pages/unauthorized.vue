<template>
  <div class="unauthorized-page">
    <DsPageHeader
      :title="$t('layout.unauthorized.title')"
      :subtitle="$t('layout.unauthorized.subtitle')"
    />
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
}
</style>
