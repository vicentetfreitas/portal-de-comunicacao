<template>
  <q-card
    class="ds-card"
    :class="[`ds-card--${variant}`]"
    :flat="variant === 'flat'"
    :bordered="variant === 'outlined'"
    v-bind="$attrs"
  >
    <q-card-section v-if="title || $slots.header">
      <slot name="header">
        <div class="ds-text-card-title">{{ title }}</div>
        <div v-if="subtitle" class="ds-card__subtitle">{{ subtitle }}</div>
      </slot>
    </q-card-section>

    <q-separator v-if="title || $slots.header" />

    <q-card-section>
      <slot />
    </q-card-section>

    <template v-if="$slots.actions">
      <q-separator />
      <q-card-actions align="right">
        <slot name="actions" />
      </q-card-actions>
    </template>
  </q-card>
</template>

<script setup lang="ts">
import type { DsCardVariant } from "../types";

withDefaults(
  defineProps<{
    title?: string;
    subtitle?: string;
    variant?: DsCardVariant;
  }>(),
  {
    variant: "elevated"
  }
);
</script>
