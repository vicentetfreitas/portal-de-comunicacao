<template>
  <q-footer class="app-footer" elevated>
    <q-toolbar class="app-footer__toolbar">
      <div class="app-footer__content">
        <span class="app-footer__text">{{
          $t("layout.footer.copyright", { year })
        }}</span>
        <span class="app-footer__separator" aria-hidden="true">·</span>
        <span class="app-footer__text">{{
          $t("layout.footer.foundation")
        }}</span>
      </div>
    </q-toolbar>
  </q-footer>
</template>

<script setup lang="ts">
const year = new Date().getFullYear();
</script>

<style scoped lang="scss">
.app-footer {
  background-color: var(--color-surface);
  color: var(--color-text-secondary);
  border-top: var(--border-width-thin) var(--border-style-default)
    var(--color-border);

  &__toolbar {
    min-height: var(--layout-footer-height);
    padding: 0 var(--spacing-md);
  }

  &__content {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: var(--spacing-xs);
    width: 100%;
  }

  &__text {
    font-size: var(--text-body-small-size);
    font-weight: var(--text-body-small-weight);
    line-height: var(--text-body-small-line-height);
  }

  &__separator {
    color: var(--color-text-disabled);
    font-size: var(--text-caption-size);
  }
}
</style>
