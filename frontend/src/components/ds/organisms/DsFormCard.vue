<template>
  <DsCard class="ds-form-card">
    <h2 v-if="title" class="ds-form-card__title">{{ title }}</h2>
    <div class="ds-form-card__content">
      <slot />
    </div>
    <template v-if="$slots.actions" #actions>
      <slot name="actions" />
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import DsCard from "../molecules/DsCard.vue";

defineProps<{
  title?: string | undefined;
}>();
</script>
