<template>
  <q-avatar
    :class="['ds-avatar', `ds-avatar--${size}`]"
    :size="quasarSize"
    :color="color"
    :text-color="textColor"
    v-bind="$attrs"
  >
    <img v-if="src" :src="src" :alt="alt" />
    <slot v-else>{{ initials }}</slot>
  </q-avatar>
</template>

<script setup lang="ts">
import { computed } from "vue";

import type { DsSize } from "../types";

const props = withDefaults(
  defineProps<{
    src?: string;
    alt?: string;
    initials?: string;
    size?: DsSize;
    color?: string;
    textColor?: string;
  }>(),
  {
    size: "md",
    color: "primary",
    textColor: "white"
  }
);

const quasarSize = computed(() => {
  const map: Record<DsSize, string> = {
    xs: "24px",
    sm: "32px",
    md: "40px",
    lg: "56px",
    xl: "72px"
  };
  return map[props.size];
});
</script>

<style scoped lang="scss">
.ds-avatar {
  font-weight: var(--font-weight-semibold);

  &--xs {
    font-size: var(--font-size-xs);
  }

  &--sm {
    font-size: var(--font-size-sm);
  }

  &--md {
    font-size: var(--font-size-sm);
  }

  &--lg {
    font-size: var(--font-size-base);
  }

  &--xl {
    font-size: var(--font-size-xl);
  }
}
</style>
