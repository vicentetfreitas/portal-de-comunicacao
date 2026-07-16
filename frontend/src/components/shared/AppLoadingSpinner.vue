<template>
  <div class="app-loading-spinner" role="status" :aria-label="label">
    <q-spinner :color="color" :size="quasarSize" />
    <p v-if="label" class="app-loading-spinner__label">{{ label }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

import type { DsSize } from "@/components/ds";

const props = withDefaults(
  defineProps<{
    label?: string;
    size?: DsSize;
    color?: string;
  }>(),
  {
    size: "md",
    color: "primary"
  }
);

const quasarSize = computed(() => {
  switch (props.size) {
    case "sm":
      return "1.5rem";
    case "lg":
      return "3rem";
    default:
      return "2rem";
  }
});
</script>

<style scoped lang="scss">
.app-loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-md);
}

.app-loading-spinner__label {
  margin: 0;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}
</style>
