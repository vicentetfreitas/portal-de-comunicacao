<template>
  <DsCard
    :title="$t('layout.auth.title')"
    :subtitle="$t('layout.auth.subtitle')"
  >
    <p class="auth-page__text">{{ $t("layout.auth.placeholder") }}</p>
    <template #actions>
      <DsButton variant="ghost" :to="ROUTE_PATHS.HOME">{{
        $t("layout.notFound.goHome")
      }}</DsButton>
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import { DsButton, DsCard } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";
</script>

<style scoped lang="scss">
.auth-page__text {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  margin: 0;
}
</style>
