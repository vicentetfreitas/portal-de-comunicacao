<template>
  <q-badge
    :class="['ds-badge', `ds-badge--${variant}`]"
    :color="color"
    :label="label"
    :outline="outline"
    v-bind="$attrs"
  >
    <slot />
  </q-badge>
</template>

<script setup lang="ts">
import { computed } from "vue";

import type { DsNotifyType } from "../types";

const props = withDefaults(
  defineProps<{
    label?: string;
    variant?: DsNotifyType | "neutral";
    outline?: boolean;
  }>(),
  {
    variant: "neutral",
    outline: false
  }
);

const color = computed(() => {
  const map: Record<DsNotifyType | "neutral", string> = {
    positive: "positive",
    negative: "negative",
    warning: "warning",
    info: "info",
    neutral: "grey-6"
  };
  return map[props.variant];
});
</script>
