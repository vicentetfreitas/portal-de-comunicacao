<template>
  <component :is="layoutComponent" class="layout-resolver">
    <router-view />
  </component>
</template>

<script setup lang="ts">
import "@/types/router-meta";

import { computed } from "vue";
import { useRoute } from "vue-router";

import { layoutMap, PublicLayout } from "@/layouts";

const route = useRoute();

const layoutComponent = computed(() => {
  const layoutName = route.meta.layout ?? "public";
  return layoutMap[layoutName] ?? PublicLayout;
});
</script>
