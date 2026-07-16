<template>
  <header
    class="ds-page-header row items-center justify-between q-col-gutter-md"
  >
    <div class="col">
      <h1 class="ds-page-header__title">{{ title }}</h1>
      <p v-if="subtitle" class="ds-page-header__subtitle">{{ subtitle }}</p>
    </div>
    <div v-if="$slots.actions" class="col-auto ds-page-header__actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  subtitle?: string;
}>();
</script>
