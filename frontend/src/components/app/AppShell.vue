<template>
  <q-layout
    view="hHh LpR fFf"
    class="app-shell"
    :class="`app-shell--${variant}`"
  >
    <AppHeader
      :variant="variant"
      :show-menu-toggle="showMenuToggle"
      :show-search="showSearch"
    >
      <template v-if="$slots.headerActions" #actions>
        <slot name="headerActions" />
      </template>
    </AppHeader>

    <AppSidebar v-if="showSidebar" :items="navItems" />

    <q-page-container>
      <q-page class="app-shell__page">
        <div
          v-if="showBreadcrumbs && breadcrumbs.length > 0"
          class="app-shell__breadcrumbs"
        >
          <DsBreadcrumbs :items="breadcrumbs" />
        </div>
        <div class="app-shell__content">
          <slot />
        </div>
      </q-page>
    </q-page-container>

    <AppFooter v-if="showFooter" />
  </q-layout>
</template>

<script setup lang="ts">
import AppFooter from "./AppFooter.vue";
import AppHeader from "./AppHeader.vue";
import AppSidebar from "./AppSidebar.vue";

import { DsBreadcrumbs } from "@/components/ds";
import { provideAppShell } from "@/composables/useAppShell";
import { useLayoutMeta } from "@/composables/useLayoutMeta";

import type { AppNavItem, AppShellVariant } from "./types";

withDefaults(
  defineProps<{
    variant?: AppShellVariant;
    navItems: AppNavItem[];
    showSidebar?: boolean;
    showFooter?: boolean;
    showMenuToggle?: boolean;
    showSearch?: boolean;
  }>(),
  {
    variant: "main",
    showSidebar: true,
    showFooter: true,
    showMenuToggle: true,
    showSearch: true
  }
);

provideAppShell();
const { breadcrumbs, showBreadcrumbs } = useLayoutMeta();
</script>

<style scoped lang="scss">
.app-shell {
  background-color: var(--color-background);

  &__page {
    padding: var(--spacing-lg);
    min-height: calc(
      100vh - var(--layout-header-height) - var(--layout-footer-height)
    );
  }

  &__breadcrumbs {
    margin-bottom: var(--spacing-md);
  }

  &__content {
    max-width: 1280px;
    margin: 0 auto;
  }
}
</style>
