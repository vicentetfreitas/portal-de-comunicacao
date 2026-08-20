<template>
  <article class="ds-service-card">
    <div class="ds-service-card__icon">
      <DsIcon :name="icon" size="lg" />
    </div>
    <div class="ds-service-card__body">
      <h3 class="ds-service-card__title">
        <slot name="title">{{ title }}</slot>
      </h3>
      <p v-if="description" class="ds-service-card__description">{{
        description
      }}</p>
    </div>
    <div v-if="$slots.actions" class="ds-service-card__actions">
      <slot name="actions" />
    </div>
  </article>
</template>

<script setup lang="ts">
import DsIcon from "../atoms/DsIcon.vue";

defineProps<{
  title: string;
  description?: string;
  icon: string;
}>();
</script>
