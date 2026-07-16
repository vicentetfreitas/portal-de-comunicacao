<template>
  <q-btn
    :class="['ds-button', `ds-button--${variant}`]"
    :color="color"
    :flat="isFlat"
    :outline="variant === 'outline'"
    :unelevated="!isFlat && variant !== 'outline'"
    :loading="loading"
    :disable="disable"
    :size="quasarSize"
    no-caps
    v-bind="$attrs"
  >
    <slot />
  </q-btn>
</template>

<script setup lang="ts">
import { computed } from "vue";

import type { DsButtonVariant, DsSize } from "../types";

const props = withDefaults(
  defineProps<{
    variant?: DsButtonVariant;
    size?: DsSize;
    loading?: boolean;
    disable?: boolean;
  }>(),
  {
    variant: "primary",
    size: "md",
    loading: false,
    disable: false
  }
);

const isFlat = computed(
  () => props.variant === "ghost" || props.variant === "link"
);

const color = computed(() => {
  if (props.variant === "danger") {
    return "negative";
  }
  if (props.variant === "secondary") {
    return "grey-7";
  }
  if (props.variant === "link") {
    return "primary";
  }
  return "primary";
});

const quasarSize = computed(() => {
  const map: Record<DsSize, "xs" | "sm" | "md" | "lg" | "xl"> = {
    xs: "xs",
    sm: "sm",
    md: "md",
    lg: "lg",
    xl: "xl"
  };
  return map[props.size];
});
</script>
