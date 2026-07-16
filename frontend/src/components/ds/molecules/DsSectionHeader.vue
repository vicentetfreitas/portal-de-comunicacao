<template>
  <header class="ds-section-header">
    <div class="ds-section-header__content">
      <h2 class="ds-section-header__title">
        <slot name="title">{{ title }}</slot>
      </h2>
      <p v-if="subtitle || $slots.subtitle" class="ds-section-header__subtitle">
        <slot name="subtitle">{{ subtitle }}</slot>
      </p>
    </div>
    <div v-if="$slots.actions" class="ds-section-header__actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<script setup lang="ts">
defineProps<{
  title?: string;
  subtitle?: string;
}>();
</script>
