<template>
  <div class="colaborador-stub-page">
    <DsPageHeader
      :title="$t('colaborador.edit.title')"
      :subtitle="$t('colaborador.edit.subtitle', { id })"
    />
    <p class="colaborador-stub-page__hint">
      {{ $t("colaborador.stub.placeholder") }}
    </p>
  </div>
</template>

<script setup lang="ts">
import { DsPageHeader } from "@/components/ds";

defineProps<{
  id: string;
}>();
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
