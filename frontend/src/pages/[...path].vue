<template>
  <div class="not-found-page">
    <div class="not-found-page__content">
      <div class="not-found-page__code">404</div>
      <h1 class="not-found-page__title">{{ $t("common.notFound") }}</h1>
      <DsButton variant="primary" :to="ROUTE_PATHS.HOME">{{
        $t("layout.notFound.goHome")
      }}</DsButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DsButton } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";
</script>

<style scoped lang="scss">
.not-found-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;

  &__content {
    text-align: center;
  }

  &__code {
    color: var(--color-primary);
    font-size: var(--font-size-5xl);
    font-weight: var(--font-weight-bold);
    line-height: var(--line-height-tight);
  }

  &__title {
    color: var(--color-text-secondary);
    font-size: var(--font-size-2xl);
    font-weight: var(--font-weight-medium);
    margin: var(--spacing-md) 0 var(--spacing-lg);
  }
}
</style>
