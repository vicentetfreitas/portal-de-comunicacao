<template>
  <div class="colaborador-stub-page">
    <DsPageHeader
      :title="$t('colaborador.list.title')"
      :subtitle="$t('colaborador.list.subtitle')"
    />
    <p class="colaborador-stub-page__hint">
      {{ $t("colaborador.stub.placeholder") }}
    </p>
  </div>
</template>

<script setup lang="ts">
import { DsPageHeader } from "@/components/ds";
</script>

<style scoped lang="scss">
.colaborador-stub-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md, 16px);

  &__hint {
    color: var(--color-text-secondary, #5f6368);
    margin: 0;
  }
}
</style>
