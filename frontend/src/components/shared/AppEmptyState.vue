<template>
  <div class="app-empty-state" role="status">
    <DsIcon v-if="icon" :name="icon" size="lg" class="app-empty-state__icon" />
    <h3 v-if="title" class="app-empty-state__title">{{ title }}</h3>
    <p v-if="description" class="app-empty-state__description">{{
      description
    }}</p>
    <div v-if="$slots.default" class="app-empty-state__actions">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { DsIcon } from "@/components/ds";

withDefaults(
  defineProps<{
    title?: string;
    description?: string;
    icon?: string;
  }>(),
  {
    icon: "mdi-inbox-outline"
  }
);
</script>

<style scoped lang="scss">
.app-empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-xl);
  text-align: center;
  color: var(--color-text-secondary);
}

.app-empty-state__icon {
  color: var(--color-text-disabled);
}

.app-empty-state__title {
  margin: 0;
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
}

.app-empty-state__description {
  margin: 0;
  max-width: 28rem;
  font-size: var(--font-size-base);
}

.app-empty-state__actions {
  margin-top: var(--spacing-sm);
}
</style>
