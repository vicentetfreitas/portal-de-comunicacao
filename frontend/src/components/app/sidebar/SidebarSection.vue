<template>
  <div class="sidebar-section">
    <p v-if="title && !mini" class="sidebar-section__title">{{ title }}</p>
    <nav class="sidebar-section__nav" :aria-label="title">
      <slot />
    </nav>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title?: string;
  mini?: boolean;
}>();
</script>

<style scoped lang="scss">
.sidebar-section {
  &__title {
    margin: 0;
    padding: var(--spacing-sm) var(--spacing-md) var(--spacing-xs);
    color: var(--color-text-secondary);
    font-size: var(--text-label-size);
    font-weight: var(--text-label-weight);
    line-height: var(--text-label-line-height);
    text-transform: uppercase;
    letter-spacing: 0.04em;
  }

  &__nav {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-xs);
    padding: 0 var(--spacing-sm);
  }
}
</style>
