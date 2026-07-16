<template>
  <q-layout view="hHh lpR fFf" class="auth-layout">
    <q-header class="auth-layout__header" elevated>
      <q-toolbar class="auth-layout__toolbar">
        <router-link to="/" class="auth-layout__brand">{{
          $t("app.name")
        }}</router-link>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <q-page class="auth-layout__page flex flex-center">
        <div class="auth-layout__content">
          <slot />
        </div>
      </q-page>
    </q-page-container>

    <q-footer class="auth-layout__footer" elevated>
      <q-toolbar class="auth-layout__footer-toolbar">
        <span class="auth-layout__footer-text">{{
          $t("layout.auth.footer")
        }}</span>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>

<style scoped lang="scss">
.auth-layout {
  background-color: var(--color-background);

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
  }

  &__content {
    width: 100%;
    max-width: 420px;
  }

  &__footer-text {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
  }
}
</style>
