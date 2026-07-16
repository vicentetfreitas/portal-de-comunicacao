<template>
  <button
    type="button"
    class="ds-action-card"
    :class="[
      `ds-action-card--${variant}`,
      { 'ds-action-card--disabled': disabled }
    ]"
    :disabled="disabled"
    @click="$emit('click')"
  >
    <span class="ds-action-card__icon">
      <DsIcon :name="icon" size="lg" />
    </span>
    <span class="ds-action-card__label">
      <slot>{{ label }}</slot>
    </span>
    <span v-if="description" class="ds-action-card__description">{{
      description
    }}</span>
  </button>
</template>

<script setup lang="ts">
import DsIcon from "../atoms/DsIcon.vue";

withDefaults(
  defineProps<{
    label: string;
    icon: string;
    description?: string;
    variant?: "primary" | "secondary" | "outline";
    disabled?: boolean;
  }>(),
  {
    variant: "primary",
    disabled: false
  }
);

defineEmits<{
  click: [];
}>();
</script>
