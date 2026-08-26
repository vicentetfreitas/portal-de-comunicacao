<template>
  <q-header class="app-header" elevated>
    <q-toolbar class="app-header__toolbar">
      <!--
        Figma (home.txt "Verde 1", node 7:3) has only the brand mark in the
        header, with no positioning cue beyond that — this project's actual
        toolbar carries controls Figma doesn't (menu/search/theme/logout), so
        the leftmost placement here is an explicit product decision, not a
        Figma measurement. The asset itself is still Figma-sourced: it's a
        raster fill embedded inside the already-downloaded
        `docs/figma/home/home.svg` export (image `image1_7_3`, used via
        `pattern3_7_3` on exactly the "Verde 1" rect), extracted to
        `public/images/unimed-ceara-logo.png` rather than fabricated.
      -->
      <router-link v-if="showLogo" to="/" class="app-header__brand">
        <slot name="logo">
          <img
            src="/images/unimed-ceara-logo.png"
            :alt="$t('app.name')"
            class="app-header__logo-image"
          />
        </slot>
      </router-link>

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

      <!--
        Kept mounted at every width — it's the only way to open the sidebar
        drawer below the 960px breakpoint (AppSidebar.vue's own
        `show-if-above`), and E2E coverage (app-shell.spec.ts) exercises it
        there. Only its desktop visibility is a styling choice (`__menu-toggle`
        below hides it ≥960px, where the drawer is already always shown) —
        an explicit product decision that the toolbar shouldn't carry a
        redundant control once the sidebar is permanently open.
      -->
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

      <DsBadge
        v-if="variant === 'admin'"
        class="q-ml-sm"
        :label="$t('layout.admin.badge')"
        variant="info"
      />

      <q-space />

      <div v-if="showSearch" class="app-header__search">
        <DsButton
          v-if="!searchOpen"
          variant="ghost"
          size="sm"
          class="app-header__search-toggle"
          :aria-label="$t('layout.header.search')"
          @click="searchOpen = true"
        >
          <DsIcon name="mdi-magnify" size="md" />
        </DsButton>
        <div v-else class="app-header__search-field">
          <DsSearchInput
            v-model="searchQuery"
            :placeholder="$t('layout.header.searchPlaceholder')"
            dense
            autofocus
            @keyup.esc="searchOpen = false"
          />
          <DsButton
            variant="ghost"
            size="sm"
            class="app-header__search-close"
            :aria-label="$t('layout.header.closeSearch')"
            @click="searchOpen = false"
          >
            <DsIcon name="mdi-close" size="sm" />
          </DsButton>
        </div>
      </div>

      <div class="app-header__actions">
        <slot name="actions" />
      </div>
    </q-toolbar>
  </q-header>
</template>

<script setup lang="ts">
import { ref } from "vue";

import { DsBadge, DsButton, DsIcon, DsSearchInput } from "@/components/ds";
import { useAppShell } from "@/composables/useAppShell";

import type { AppShellVariant } from "./types";

withDefaults(
  defineProps<{
    variant?: AppShellVariant;
    showMenuToggle?: boolean;
    showSearch?: boolean;
    showLogo?: boolean;
    showBackButton?: boolean;
    backLabel?: string;
  }>(),
  {
    variant: "main",
    showMenuToggle: true,
    showSearch: true,
    showLogo: true,
    showBackButton: false,
    backLabel: "Voltar"
  }
);

defineEmits<{
  back: [];
}>();

const shell = useAppShell();
const searchQuery = ref("");
const searchOpen = ref(false);
</script>

<style scoped lang="scss">
.app-header {
  // Figma: header band on authenticated screens is a flat "Gelo" fill
  // (#edede6, confirmed from the Figma file's own asset export), not white.
  background-color: var(--color-gelo);
  color: var(--color-text-primary);
  border-bottom: var(--border-width-thin) var(--border-style-default)
    var(--color-border);

  &__toolbar {
    min-height: var(--layout-header-height);
    // Left padding matches the sidebar panel's own left inset
    // (`--layout-sidebar-inset`, `_layout.scss`) instead of the generic
    // `--spacing-md` every other side keeps — without it the logo (flush
    // left in the toolbar) sat ~60-100px left of the sidebar panel's own
    // edge below it (measured via Playwright at both 1280 and 1920px),
    // reading as two unrelated columns instead of one aligned shell.
    padding: 0 var(--spacing-md) 0 var(--layout-sidebar-inset);
    gap: var(--spacing-sm);
    align-items: center;
  }

  &__brand {
    display: flex;
    align-items: center;
    color: var(--color-primary);
    text-decoration: none;
    line-height: var(--line-height-tight);
  }

  &__logo-image {
    // Figma measures the mark at 72px tall inside a 130px header band
    // (home.txt "Verde 1", node 7:3) — same ratio (~0.55) applied to this
    // header's own `--layout-header-height` (fluid, 85–128px — see
    // `_layout.scss`) instead of a second independent px/clamp value, so
    // the logo keeps scaling with the header instead of the two drifting
    // apart below 1920px. Width is intentionally unset: the source PNG's
    // own aspect ratio (890×229, ≈ the Figma rect's 280×72) keeps it
    // correct.
    height: calc(var(--layout-header-height) * 0.5546875);
    width: auto;
  }

  // Hidden ≥960px (AppSidebar.vue's own SIDEBAR_BREAKPOINT): the drawer is
  // `show-if-above` there, so a toggle would be a redundant control once the
  // sidebar is already permanently open. Kept mounted (not `v-if` on width)
  // so it stays available below the breakpoint, where it's the only way to
  // open the drawer.
  &__menu-toggle {
    @media (min-width: 960px) {
      display: none;
    }
  }

  &__search {
    display: flex;
    align-items: center;
  }

  &__search-field {
    display: flex;
    align-items: center;
    gap: var(--spacing-xs);
    width: min(320px, 40vw);
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
  }
}
</style>
