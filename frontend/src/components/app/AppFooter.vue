<template>
  <q-footer class="app-footer" elevated>
    <q-toolbar class="app-footer__toolbar">
      <div class="app-footer__content">
        <span class="app-footer__text">{{
          $t("layout.footer.copyright", { year })
        }}</span>
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
  // Figma: footer band is the same flat "Gelo" fill as the header (#edede6).
  // No border between the white page background and this band in the Home
  // screenshot (docs/figma/home/home.png) — dropped the unevidenced
  // `border-top`, same call already made for AppSidebar's internal dividers.
  background-color: var(--color-gelo);
  // Figma's footer text ("Rectangle 8" copyright line, home.txt) uses the
  // same "cinza" ink as the rest of the Home frame (#585c65) — light theme
  // only, since Figma has no dark frame; dark theme keeps the app's own
  // secondary-text token.
  --app-footer-ink: #585c65;
  color: var(--app-footer-ink);

  &__toolbar {
    min-height: var(--layout-footer-height);
    padding: 0 var(--spacing-md);
  }

  &__content {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    gap: var(--spacing-xs);
    width: 100%;
    text-align: center;
  }

  &__text {
    font-size: var(--text-body-small-size);
    // Figma: italic, weight 600 (SemiBold) — not the DS default body-small
    // style (normal, 400).
    font-style: italic;
    font-weight: var(--font-weight-semibold);
    line-height: var(--text-body-small-line-height);
  }
}

[data-theme="dark"] .app-footer {
  --app-footer-ink: var(--color-text-secondary);
}
</style>
