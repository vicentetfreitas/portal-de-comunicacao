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
      :user-initials="profileInitials"
      :show-user-area="isAuthenticated"
    >
      <template #actions>
        <DsButton
          v-if="isAuthenticated"
          variant="ghost"
          size="sm"
          :loading="isLoggingOut"
          @click="handleLogout"
        >
          {{ $t("layout.auth.logout") }}
        </DsButton>
        <slot name="headerActions" />
      </template>
    </AppHeader>

    <AppSidebar
      v-if="showSidebar"
      :items="navItems"
      :show-profile="isAuthenticated"
      :profile-name="profileName"
      :profile-greeting="profileGreeting"
      :profile-initials="profileInitials"
      :profile-edit-label="$t('layout.auth.editProfile')"
      :show-edit="false"
    />

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
import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";

import AppFooter from "./AppFooter.vue";
import AppHeader from "./AppHeader.vue";
import AppSidebar from "./AppSidebar.vue";

import { DsBreadcrumbs, DsButton } from "@/components/ds";
import { provideAppShell } from "@/composables/useAppShell";
import { useAuth } from "@/composables/useAuth";
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
const { t } = useI18n();
const { user, isAuthenticated, logout } = useAuth();

const isLoggingOut = ref(false);

const profileName = computed(
  () => user.value?.name ?? t("layout.sidebar.profileName")
);
const profileGreeting = computed(() => t("layout.sidebar.profileGreeting"));
const profileInitials = computed(() => {
  const name = user.value?.name?.trim();
  if (!name) {
    return "CO";
  }
  const parts = name.split(/\s+/).filter(Boolean);
  if (parts.length === 1) {
    return parts[0]!.slice(0, 2).toUpperCase();
  }
  return `${parts[0]![0] ?? ""}${parts[parts.length - 1]![0] ?? ""}`.toUpperCase();
});

async function handleLogout(): Promise<void> {
  if (isLoggingOut.value) {
    return;
  }
  isLoggingOut.value = true;
  try {
    await logout();
  } finally {
    isLoggingOut.value = false;
  }
}
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
