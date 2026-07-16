<template>
  <q-header class="app-header" elevated>
    <q-toolbar class="app-header__toolbar">
      <DsButton
        v-if="showBackButton"
        variant="ghost"
        size="sm"
        class="app-header__back"
        :aria-label="backLabel"
        @click="$emit('back')"
      >
        <DsIcon name="mdi-arrow-left" size="md" />
      </DsButton>

      <DsButton
        v-if="showMenuToggle"
        variant="ghost"
        size="sm"
        class="app-header__menu-toggle"
        :aria-label="$t('layout.header.toggleMenu')"
        @click="shell.toggleDrawer()"
      >
        <DsIcon name="mdi-menu" size="md" />
      </DsButton>

      <router-link v-if="showLogo" to="/" class="app-header__brand">
        <slot name="logo">
          <span class="app-header__logo-text">{{ $t("app.name") }}</span>
        </slot>
      </router-link>

      <DsBadge
        v-if="variant === 'admin'"
        class="q-ml-sm"
        :label="$t('layout.admin.badge')"
        variant="info"
      />

      <q-space />

      <div v-if="showSearch" class="app-header__search gt-sm">
        <DsSearchInput
          v-model="searchQuery"
          :placeholder="$t('layout.header.searchPlaceholder')"
          dense
        />
      </div>

      <div class="app-header__actions">
        <slot name="actions" />
      </div>

      <div v-if="showUserArea" class="app-header__user">
        <slot name="user">
          <DsAvatar :initials="userInitials" size="sm" />
        </slot>
      </div>
    </q-toolbar>
  </q-header>
</template>

<script setup lang="ts">
import { ref } from "vue";

import {
  DsAvatar,
  DsBadge,
  DsButton,
  DsIcon,
  DsSearchInput
} from "@/components/ds";
import { useAppShell } from "@/composables/useAppShell";

import type { AppShellVariant } from "./types";

withDefaults(
  defineProps<{
    variant?: AppShellVariant;
    showMenuToggle?: boolean;
    showSearch?: boolean;
    showLogo?: boolean;
    showBackButton?: boolean;
    showUserArea?: boolean;
    backLabel?: string;
    userInitials?: string;
  }>(),
  {
    variant: "main",
    showMenuToggle: true,
    showSearch: true,
    showLogo: true,
    showBackButton: false,
    showUserArea: true,
    backLabel: "Voltar",
    userInitials: "UN"
  }
);

defineEmits<{
  back: [];
}>();

const shell = useAppShell();
const searchQuery = ref("");
</script>

<style scoped lang="scss">
.app-header {
  background-color: var(--color-surface);
  color: var(--color-text-primary);
  border-bottom: var(--border-width-thin) var(--border-style-default)
    var(--color-border);

  &__toolbar {
    min-height: var(--layout-header-height);
    padding: 0 var(--spacing-md);
    gap: var(--spacing-sm);
  }

  &__brand {
    display: flex;
    align-items: center;
    color: var(--color-primary);
    text-decoration: none;
    line-height: var(--line-height-tight);
  }

  &__logo-text {
    font-size: var(--text-card-title-size);
    font-weight: var(--text-card-title-weight);
    line-height: var(--text-card-title-line-height);
  }

  &__search {
    width: min(320px, 40vw);
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
  }

  &__user {
    display: flex;
    align-items: center;
    margin-left: var(--spacing-sm);
  }
}
</style>
