<template>
  <component
    :is="tag"
    :class="navItemClasses"
    :aria-current="active ? 'page' : undefined"
    :aria-label="mini ? label : undefined"
    v-bind="componentAttrs"
  >
    <span v-if="icon" class="ds-nav-item__icon">
      <DsIcon :name="icon" :size="iconSize" />
    </span>
    <span v-if="!mini" class="ds-nav-item__label">
      <slot>{{ label }}</slot>
    </span>
    <span v-if="badge && !mini" class="ds-nav-item__badge">
      <DsBadge :label="badge" variant="neutral" />
    </span>
  </component>
</template>

<script setup lang="ts">
import { computed } from "vue";

import DsBadge from "../atoms/DsBadge.vue";
import DsIcon from "../atoms/DsIcon.vue";

import type { DsSize } from "../types";

const props = withDefaults(
  defineProps<{
    label?: string;
    icon?: string;
    to?: string;
    href?: string;
    active?: boolean;
    mini?: boolean;
    badge?: string;
    disabled?: boolean;
  }>(),
  {
    active: false,
    mini: false,
    disabled: false
  }
);

const tag = computed(() => {
  if (props.disabled) {
    return "span";
  }
  if (props.to) {
    return "router-link";
  }
  if (props.href) {
    return "a";
  }
  return "button";
});

const componentAttrs = computed(() => {
  if (props.disabled) {
    return { "aria-disabled": true, type: "button" };
  }
  if (props.to) {
    return { to: props.to };
  }
  if (props.href) {
    return { href: props.href };
  }
  return { type: "button" };
});

const iconSize = computed<DsSize>(() => (props.mini ? "sm" : "md"));

const navItemClasses = computed(() => [
  "ds-nav-item",
  {
    "ds-nav-item--active": props.active,
    "ds-nav-item--mini": props.mini,
    "ds-nav-item--disabled": props.disabled
  }
]);
</script>
