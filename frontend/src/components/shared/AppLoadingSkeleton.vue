<template>
  <div class="app-loading-skeleton" :aria-busy="true" aria-live="polite">
    <q-skeleton
      v-for="index in rows"
      :key="index"
      :type="type"
      :height="height"
      :width="width"
      class="app-loading-skeleton__row"
    />
  </div>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{
    rows?: number;
    type?: "rect" | "text" | "QAvatar" | "QBtn";
    height?: string;
    width?: string;
  }>(),
  {
    rows: 3,
    type: "rect",
    height: "1rem"
  }
);
</script>

<style scoped lang="scss">
.app-loading-skeleton {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
  width: 100%;
}

.app-loading-skeleton__row {
  border-radius: var(--radius-sm);
}
</style>
