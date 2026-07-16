<template>
  <q-layout view="hHh lpR fFf" class="public-layout">
    <q-header class="public-layout__header" elevated>
      <q-toolbar class="public-layout__toolbar">
        <router-link :to="ROUTE_PATHS.HOME" class="public-layout__brand">{{
          $t("app.name")
        }}</router-link>
        <q-space />
        <nav class="public-layout__nav gt-sm">
          <router-link
            :to="ROUTE_PATHS.SHOWCASE"
            class="public-layout__nav-link"
          >
            {{ $t("layout.nav.showcase") }}
          </router-link>
          <router-link :to="ROUTE_PATHS.AUTH" class="public-layout__nav-link">
            {{ $t("layout.nav.auth") }}
          </router-link>
        </nav>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <q-page class="public-layout__page">
        <slot />
      </q-page>
    </q-page-container>

    <AppFooter />
  </q-layout>
</template>

<script setup lang="ts">
import { AppFooter } from "@/components/app";
import { ROUTE_PATHS } from "@/constants/routes";
</script>

<style scoped lang="scss">
.public-layout {
  background-color: var(--color-background);

  &__header {
    background-color: var(--color-surface);
    border-bottom: 1px solid var(--color-border);
  }

  &__toolbar {
    min-height: 56px;
    padding: 0 var(--spacing-md);
  }

  &__brand {
    color: var(--color-primary);
    font-size: var(--font-size-lg);
    font-weight: var(--font-weight-bold);
    text-decoration: none;
  }

  &__nav {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
  }

  &__nav-link {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    font-weight: var(--font-weight-medium);
    text-decoration: none;

    &:hover,
    &.router-link-active {
      color: var(--color-primary);
    }
  }

  &__page {
    padding: var(--spacing-lg);
    min-height: calc(100vh - 56px - 48px);
  }
}
</style>
