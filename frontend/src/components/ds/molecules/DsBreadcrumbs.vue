<template>
  <q-breadcrumbs class="ds-breadcrumbs" active-color="primary">
    <q-breadcrumbs-el
      v-for="(item, index) in items"
      :key="`${item.label}-${index}`"
      :label="item.label"
      :icon="item.icon"
      :to="item.to"
    />
  </q-breadcrumbs>
</template>

<script setup lang="ts">
import type { DsBreadcrumbItem } from "../types";

defineProps<{
  items: DsBreadcrumbItem[];
}>();
</script>

<style scoped lang="scss">
.ds-breadcrumbs {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}
</style>
