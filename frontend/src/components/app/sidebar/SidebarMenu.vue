<template>
  <SidebarSection v-bind="sectionAttrs">
    <SidebarMenuItem
      v-for="(item, index) in items"
      :key="`${item.to}-${index}`"
      :label="$t(item.labelKey)"
      :icon="item.icon"
      :to="item.to"
      :mini="mini"
      :active="isActive(item.to)"
      v-bind="itemAttrs(item)"
    />
  </SidebarSection>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";

import SidebarMenuItem from "./SidebarMenuItem.vue";
import SidebarSection from "./SidebarSection.vue";

import type { AppNavItem } from "../types";

const props = defineProps<{
  items: AppNavItem[];
  sectionTitle?: string | undefined;
  mini?: boolean;
}>();

const route = useRoute();

const sectionAttrs = computed(() => ({
  mini: props.mini ?? false,
  ...(props.sectionTitle !== undefined ? { title: props.sectionTitle } : {})
}));

function isActive(path: string): boolean {
  return route.path === path;
}

function itemAttrs(item: AppNavItem): Record<string, boolean> {
  return item.disabled !== undefined ? { disabled: item.disabled } : {};
}
</script>
