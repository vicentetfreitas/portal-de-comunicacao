<template>
  <q-icon
    :class="['ds-icon', `ds-icon--${size}`]"
    :name="name"
    :color="color"
    v-bind="$attrs"
  />
</template>

<script setup lang="ts">
import type { DsSize } from "../types";

withDefaults(
  defineProps<{
    name: string;
    size?: DsSize;
    color?: string;
  }>(),
  {
    size: "md"
  }
);
</script>

<style scoped lang="scss">
.ds-icon--xs {
  font-size: var(--font-size-sm);
}

.ds-icon--sm {
  font-size: var(--font-size-base);
}

.ds-icon--md {
  font-size: var(--font-size-xl);
}

.ds-icon--lg {
  font-size: var(--font-size-2xl);
}

.ds-icon--xl {
  font-size: var(--font-size-3xl);
}
</style>
