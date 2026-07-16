<template>
  <DsNavItem
    :label="label"
    :icon="icon"
    :to="to"
    :active="active"
    :mini="mini"
    v-bind="optionalAttrs"
  />
</template>

<script setup lang="ts">
import { computed } from "vue";

import DsNavItem from "@/components/ds/molecules/DsNavItem.vue";

const props = defineProps<{
  label: string;
  icon: string;
  to: string;
  active?: boolean;
  mini?: boolean;
  badge?: string | undefined;
  disabled?: boolean;
}>();

const optionalAttrs = computed(() => ({
  ...(props.badge !== undefined ? { badge: props.badge } : {}),
  ...(props.disabled !== undefined ? { disabled: props.disabled } : {})
}));
</script>
