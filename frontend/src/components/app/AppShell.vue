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
      <template #actions>
        <DsButton
          variant="ghost"
          size="sm"
          :aria-label="
            isDark
              ? $t('layout.header.switchToLightTheme')
              : $t('layout.header.switchToDarkTheme')
          "
          @click="theme.toggle()"
        >
          <DsIcon
            :name="isDark ? 'mdi-weather-sunny' : 'mdi-weather-night'"
            size="md"
          />
        </DsButton>
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
      :show-organization-directory="variant === 'main'"
      @profile-edit="router.push(ROUTE_PATHS.PERFIL)"
    />

    <q-page-container>
      <q-page
        class="app-shell__page"
        :class="{ 'app-shell__page--drawer-closed': !leftDrawerOpen }"
      >
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
import { useRouter } from "vue-router";

import AppFooter from "./AppFooter.vue";
import AppHeader from "./AppHeader.vue";
import AppSidebar from "./AppSidebar.vue";

import { DsBreadcrumbs, DsButton, DsIcon } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";
import { provideAppShell } from "@/composables/useAppShell";
import { useAuth } from "@/composables/useAuth";
import { useLayoutMeta } from "@/composables/useLayoutMeta";
import { useTheme } from "@/composables/useTheme";
import { resolveGreetingName } from "@/utils/user-display-name";

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

const { leftDrawerOpen } = provideAppShell();
const { breadcrumbs, showBreadcrumbs } = useLayoutMeta();
const { t } = useI18n();
const router = useRouter();
const { user, isAuthenticated, logout } = useAuth();
const theme = useTheme();
const { isDark } = theme;

const isLoggingOut = ref(false);

// Greeting shows the first name only (Figma: "Olá, Monalisa!"), falling
// back to the email local-part when the session has no `name` yet — never
// the full name, and never a hardcoded/fictitious one.
const profileName = computed(
  () => resolveGreetingName(user.value) || t("layout.sidebar.profileName")
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
    // Was `min-height` only, which bounds the page from below but not from
    // above — on shorter viewports (measured via Playwright at 1600×900,
    // 1440×900, 1366×768) content taller than this budget just grew the
    // page past it, and since Quasar's header/footer here are fixed
    // (`view="hHh LpR fFf"`, both rows uppercase), the extra height didn't
    // push the footer down — it rendered *behind* the fixed footer instead,
    // while the whole document grew past the viewport (a real,
    // document-level `scrollHeight > innerHeight`, not a false positive).
    // `height` + `overflow-y` turns this into the standard fixed
    // header/scrollable content/fixed footer shell: this exact box is
    // "the space between header and footer", and if content ever exceeds
    // it, it scrolls inside this region instead of hiding under the footer
    // or growing the document.
    // `- var(--border-width-thin)`: the header's own `border-bottom`
    // (AppHeader.vue) renders 1px beyond `--layout-header-height` on top of
    // its `min-height`, since it's a border, not padding — without
    // subtracting it too, the page came out 1px taller than the true
    // remaining space at every resolution tested (confirmed via Playwright:
    // `document.scrollHeight` was exactly `innerHeight + 1` at all four).
    height: calc(
      100vh - var(--layout-header-height) - var(--layout-footer-height) - var(
          --border-width-thin
        )
    );
    overflow-y: auto;

    // Figma (home.txt): main content starts 166px right of the sidebar
    // rail's own right edge (`--layout-content-gutter`), not flush against
    // it. Only applies once the sidebar is actually in-flow (Quasar's
    // drawer breakpoint, `AppSidebar.vue`'s `:breakpoint="960"`) — below
    // that the drawer overlays instead of reserving rail width, so this
    // extra gutter would just eat into the mobile viewport for no reason.
    //
    // Same reasoning for the top offset: the Home frame's heading ("Fique
    // por dentro", y217) and the sidebar panel's own top edge (also y217,
    // `AppSidebar.vue`'s own top margin) are level — but this page only
    // had the generic `--spacing-lg` (24px) top padding, so content started
    // noticeably higher than the sidebar instead of lining up with it.
    // `--layout-content-top-offset` is shared with `AppSidebar.vue` (not a
    // literal `87px` in both) so the two stay level as it scales fluidly.
    @media (min-width: 960px) {
      padding-top: var(--layout-content-top-offset);
      padding-left: var(--layout-content-gutter);
    }

    // Figma has no closed-sidebar frame — this is a real gap found by
    // actually toggling the drawer (`Alternar menu`) and measuring: the
    // 166px gutter above is sized to line up with the *open* sidebar rail,
    // but Quasar stops reserving that rail's own width once the drawer
    // closes while this fixed gutter stays, so the content stayed pinned
    // 166px from the left edge with the whole freed rail width (464px)
    // left empty on the right instead of the content re-centering into it.
    &--drawer-closed {
      @media (min-width: 960px) {
        padding-left: var(--spacing-lg);
      }
    }
  }

  &__breadcrumbs {
    margin-bottom: var(--spacing-md);
  }

  &__content {
    max-width: 1280px;
    // Figma's content column is flush-left after the sidebar gutter, not
    // centered — it leaves whatever space remains as trailing whitespace
    // on the right (confirmed across the audited frames, not just Home).
    // `margin: 0 auto` centered it instead, pulling content further right
    // than Figma shows on any viewport wider than 1280+gutter. Only true
    // while the sidebar is actually open, though — see
    // `&__page--drawer-closed` below.
    margin: 0;
  }

  // See `&__page--drawer-closed` comment above: no sidebar rail is reserving
  // space anymore, so center the content in the freed width instead of
  // leaving it pinned to the left with dead space on the right.
  &__page--drawer-closed &__content {
    @media (min-width: 960px) {
      margin: 0 auto;
    }
  }
}
</style>
