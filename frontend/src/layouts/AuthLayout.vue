<template>
  <q-layout
    view="hHh lpR fFf"
    class="auth-layout"
    :class="{ 'auth-layout--full-bleed': fullBleed }"
  >
    <template v-if="!fullBleed">
      <q-header class="auth-layout__header" elevated>
        <q-toolbar class="auth-layout__toolbar">
          <router-link to="/" class="auth-layout__brand">{{
            $t("app.name")
          }}</router-link>
        </q-toolbar>
      </q-header>
    </template>

    <q-page-container>
      <q-page
        class="auth-layout__page"
        :class="{ 'auth-layout__page--full-bleed': fullBleed }"
      >
        <div
          class="auth-layout__content"
          :class="{ 'auth-layout__content--full-bleed': fullBleed }"
        >
          <slot />
        </div>
      </q-page>
    </q-page-container>

    <template v-if="!fullBleed">
      <q-footer class="auth-layout__footer" elevated>
        <q-toolbar class="auth-layout__footer-toolbar">
          <span class="auth-layout__footer-text">{{
            $t("layout.auth.footer")
          }}</span>
        </q-toolbar>
      </q-footer>
    </template>
  </q-layout>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

const fullBleed = computed(() => route.meta.authFullBleed === true);
</script>

<style scoped lang="scss">
.auth-layout {
  background-color: var(--color-background);

  &--full-bleed {
    height: 100dvh;
    max-height: 100dvh;
    overflow: hidden;
    background-color: var(--color-gray-100);
  }

  &--full-bleed :deep(.q-page-container) {
    height: 100dvh;
    max-height: 100dvh;
    overflow: hidden;
  }

  &__header,
  &__footer {
    background-color: var(--color-surface);
    color: var(--color-text-primary);
    border-color: var(--color-border);
  }

  &__toolbar,
  &__footer-toolbar {
    min-height: 56px;
    padding: 0 var(--spacing-md);
    justify-content: center;
  }

  &__brand {
    color: var(--color-primary);
    font-size: var(--font-size-xl);
    font-weight: var(--font-weight-bold);
    text-decoration: none;
  }

  &__page {
    padding: var(--spacing-lg);

    &:not(.auth-layout__page--full-bleed) {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }

    &--full-bleed {
      padding: 0;
      height: 100%;
      min-height: 0;
      max-height: 100dvh;
      overflow: hidden;
    }
  }

  &__content {
    width: 100%;
    max-width: 420px;
    margin: 0 auto;

    &--full-bleed {
      max-width: none;
      margin: 0;
      width: 100%;
      height: 100%;
    }
  }

  &__footer-text {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
  }
}
</style>
