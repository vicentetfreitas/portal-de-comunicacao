<template>
  <q-drawer
    v-model="drawerOpen"
    class="app-sidebar"
    :breakpoint="SIDEBAR_BREAKPOINT"
    :width="drawerWidth"
    show-if-above
  >
    <div class="app-sidebar__profile">
      <SidebarProfile
        v-if="showProfile"
        :name="profileName"
        :greeting="profileGreeting"
        :avatar-initials="profileInitials"
        :edit-label="profileEditLabel"
        :show-edit="showEdit"
        @edit="$emit('profileEdit')"
      />
    </div>

    <q-scroll-area class="app-sidebar__scroll">
      <div class="app-sidebar__menu">
        <SidebarMenu :items="leadingItems" />

        <template v-if="showOrganizationDirectory">
          <SidebarDirectorySection
            :label="$t('layout.sidebar.federationLabel')"
            icon="mdi-file-tree"
            :items="federationAreas.items.value"
            :loading="federationAreas.loading.value"
            v-model:search="federationAreas.search.value"
            :search-placeholder="
              $t('layout.sidebar.federationSearchPlaceholder')
            "
            :empty-label="$t('layout.sidebar.federationEmpty')"
            @expand="federationAreas.load()"
          />

          <SidebarDirectorySection
            :label="$t('layout.sidebar.singularLabel')"
            icon="mdi-domain"
            :items="singulares.items.value"
            :loading="singulares.loading.value"
            v-model:search="singulares.search.value"
            :search-placeholder="$t('layout.sidebar.singularSearchPlaceholder')"
            :empty-label="$t('layout.sidebar.singularEmpty')"
            @expand="singulares.load()"
          />

          <SidebarMenu v-if="trailingItems.length > 0" :items="trailingItems" />

          <SidebarMenu :items="servicesItems" />
        </template>

        <!-- Admin shell has its own real CRUD nav instead of the
        Federação/Singular directories above (`showOrganizationDirectory`
        false there) — `trailingItems` ("Áreas") still needs to render for
        it, just without Federação/Singular/Serviços around it. -->
        <SidebarMenu
          v-else-if="trailingItems.length > 0"
          :items="trailingItems"
        />

        <SidebarMenu
          v-if="adminItems.length > 0"
          :items="adminItems"
          :section-title="adminSectionTitle"
        />
      </div>
    </q-scroll-area>
  </q-drawer>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";
import { useQuasar } from "quasar";

import {
  LAYOUT_SIDEBAR_WIDTH,
  LAYOUT_SIDEBAR_WIDTH_FLOOR,
  LAYOUT_SIDEBAR_WIDTH_FLOOR_VIEWPORT,
  LAYOUT_SIDEBAR_WIDTH_REFERENCE_VIEWPORT
} from "@/constants/layout";
import { federacaoAreaPath, federacaoSingularPath } from "@/constants/routes";
import { useAppShell } from "@/composables/useAppShell";
import {
  useFederationAreaDirectory,
  useSingularDirectory
} from "@/composables/organization/useOrganizationDirectory";

import {
  SidebarDirectorySection,
  SidebarMenu,
  SidebarProfile
} from "./sidebar";

import type { AppNavItem } from "./types";

const props = withDefaults(
  defineProps<{
    items: AppNavItem[];
    showProfile?: boolean;
    profileName?: string;
    profileGreeting?: string;
    profileInitials?: string;
    profileEditLabel?: string;
    showEdit?: boolean;
    /**
     * Production nav model's "Federação"/"Singular" read-only directories
     * (docs/discovery/frontend-production-discovery.md §"Menus" —
     * collaborator menu). Only meaningful for the main/collaborator shell:
     * the admin shell already has real CRUD navigation to Singulares/Áreas.
     */
    showOrganizationDirectory?: boolean;
  }>(),
  {
    showProfile: true,
    profileName: "Colaborador",
    profileGreeting: "Olá,",
    profileInitials: "CO",
    profileEditLabel: "Editar perfil",
    showEdit: true,
    showOrganizationDirectory: false
  }
);

defineEmits<{
  profileEdit: [];
}>();

const { t } = useI18n();
const $q = useQuasar();
const shell = useAppShell();

// Must match the `:breakpoint` prop above — Quasar's own `$q.screen.lt.md`
// (1024px, used elsewhere for generic mobile checks) is a different
// threshold than this drawer's own breakpoint (960px); reusing it here
// would desync the width from the drawer's actual above/below-breakpoint
// state in the 960–1024px band (persistent desktop rail there, not an
// overlay).
const SIDEBAR_BREAKPOINT = 960;

const drawerOpen = computed({
  get: () => shell.leftDrawerOpen.value,
  set: (value: boolean) => {
    shell.leftDrawerOpen.value = value;
  }
});

// `LAYOUT_SIDEBAR_WIDTH` (464px) is the in-flow desktop rail measured from
// the Figma frame — below the drawer's own breakpoint it becomes a
// full-screen mobile overlay instead, where that width doesn't fit small
// viewports (e.g. 390px) and pushes the floating panel off-screen. Figma has
// no mobile frame to source a value from, so this is a plain responsive
// overlay width, not a measured one.
const MOBILE_DRAWER_WIDTH = 344;
// Figma frame's own aspect ratio (1920×1080) — see `--layout-figma-viewport`
// in design-tokens.scss for why the *contain-fit* dimension (whichever of
// width/height is the tighter constraint), not plain screen width, drives
// the scale: a real screenshot measured a ~1920px-wide/~891px-tall window
// (width already at its max, so a width-only scale left this untouched)
// where the rail still visibly overpowered the shorter viewport.
const FIGMA_ASPECT_RATIO = LAYOUT_SIDEBAR_WIDTH_REFERENCE_VIEWPORT / 1080;
// Quasar's `<q-drawer :width>` takes a JS number, not a CSS value, so this
// reproduces the same contain-fit/`clamp()` derivation as the other layout
// tokens in `_layout.scss` (464px at 1920px, down to a 309px floor at
// 1280px) reactively against `$q.screen` instead of being pure CSS.
const drawerWidth = computed(() => {
  if ($q.screen.width < SIDEBAR_BREAKPOINT) {
    return Math.min(MOBILE_DRAWER_WIDTH, LAYOUT_SIDEBAR_WIDTH);
  }

  const containFitWidth = Math.min(
    $q.screen.width,
    $q.screen.height * FIGMA_ASPECT_RATIO
  );
  const clampedViewport = Math.min(
    Math.max(containFitWidth, LAYOUT_SIDEBAR_WIDTH_FLOOR_VIEWPORT),
    LAYOUT_SIDEBAR_WIDTH_REFERENCE_VIEWPORT
  );
  const scaled = Math.round(
    (LAYOUT_SIDEBAR_WIDTH * clampedViewport) /
      LAYOUT_SIDEBAR_WIDTH_REFERENCE_VIEWPORT
  );
  return Math.max(scaled, LAYOUT_SIDEBAR_WIDTH_FLOOR);
});

const leadingItems = computed(() =>
  props.items.filter(
    item => item.section !== "admin" && item.placement !== "trailing"
  )
);

const trailingItems = computed(() =>
  props.items.filter(
    item => item.section !== "admin" && item.placement === "trailing"
  )
);

const adminItems = computed(() =>
  props.items.filter(item => item.section === "admin")
);

const adminSectionTitle = computed(() => t("layout.sidebar.adminSection"));

const federationAreasDirectory = useFederationAreaDirectory();
const federationAreas = {
  ...federationAreasDirectory,
  // Only the área name — no singular-name subtitle. Explicit product
  // decision: the subtitle could literally read "Unimed Ceará" (the
  // federation's own pseudo-Singular row, see `useOrganizationDirectory.ts`
  // `EXCLUDED_SINGULAR_NAMES`) for areas under it, which read as noise/
  // confusing here even for areas under a real Singular.
  items: computed(() =>
    federationAreasDirectory.items.value.map(area => ({
      id: area.id,
      name: area.name,
      highlighted: area.isOwnArea,
      to: federacaoAreaPath(area.id)
    }))
  )
};

const singularsDirectory = useSingularDirectory();
const singulares = {
  ...singularsDirectory,
  items: computed(() =>
    singularsDirectory.items.value.map(singular => ({
      id: singular.id,
      name: singular.name,
      subtitle: singular.acronym,
      to: federacaoSingularPath(singular.id)
    }))
  )
};

// "Serviços" (Figma Home, node 7:3, "Botão Serviços" — literal label, not
// "Sistemas e Serviços") has no destination yet — `FT-SERVICOS` is `DRAFT`,
// blocked on a product decision (static vs. administrable list, external
// link vs. SSO). Shown disabled, same non-navigable pattern as the
// "Arquivos e Documentos" card in `AreaColaboradorHubPage.vue`, rather than
// left out of the nav or wired to a fabricated route.
const servicesItems = computed<AppNavItem[]>(() => [
  {
    labelKey: "layout.sidebar.servicesLabel",
    to: "",
    icon: "mdi-bookmark-outline",
    disabled: true
  }
]);
</script>

<style scoped lang="scss">
// Quasar's <q-drawer> forwards the `app-sidebar` class to its internal
// `.q-drawer__content` node, which sits outside this component's scoped
// attribute (only `.q-drawer-container`, an ancestor, carries it) — so the
// unscoped `.app-sidebar` selector below never matches it. `:deep()` targets
// that real node via a descendant match instead, restoring the flex column
// context `&__scroll` (flex: 1) depends on to avoid collapsing to 0 height.
// The floating panel no longer fills the whole drawer rail (see `height`
// below) — Quasar's own `.q-drawer` background (white, unthemed) would
// otherwise show through the newly-exposed gap above/below the panel,
// visible in dark theme. Match the page background there instead.
:deep(.q-drawer) {
  background-color: var(--color-background);
}

:deep(.app-sidebar) {
  display: flex;
  flex-direction: column;
  // Figma renders the sidebar as a fully-rounded panel floating with a
  // margin on every side (not flush against the header, viewport edge, or
  // content column) — confirmed via get_metadata across all 8 frames in the
  // file (Login, Home, Areas, Areas - Equipe, etc.), which each place the
  // same `Rectangle 12` sidebar fill inset from every edge of its 1920x1080
  // frame. #f6f6f6 is the exact fill from the Figma file, not the ~#fafafa
  // approximation `--color-surface-elevated` gave before.
  background-color: var(--color-surface-sidebar);
  color: var(--color-text-primary);
  // Figma measures this panel's own corner radius at 29px (node 64:849,
  // `rounded-[29px]`, all 8 frames) — noticeably heavier than `--radius-lg`
  // (12px), which under-rounded the panel.
  border-radius: 29px;
  // Quasar's drawer content has an explicit width/height (not an
  // inset-derived auto box), so `margin` alone only shifts the box — it
  // doesn't shrink it. Subtract the margin explicitly so the panel doesn't
  // overflow past the drawer's own edges.
  //
  // Left inset is `--layout-sidebar-inset` (116px), not `--spacing-lg`
  // (24px): the rail (`--layout-sidebar-width`, `constants/layout.ts`) was
  // widened to include this inset, so it isn't a page-specific fix — see
  // that file's comments for the Figma measurements. Right margin is ~0:
  // the panel sits flush against the rail's right edge; the visible gap
  // before the main content column is `AppShell.vue`'s own
  // `--layout-content-gutter`, not sidebar margin.
  //
  // Top inset is `--layout-content-top-offset` (87px at 1920px, fluid below
  // that — shared with `AppShell.vue` so the two stay level as it scales),
  // not `--spacing-lg` (24px): in the Home frame the panel's own top edge
  // (y217) lines up exactly with the main content's "Fique por dentro"
  // heading (also y217) — 87px below the header's visible bottom edge
  // (~y130) — not flush under the header like the other three sides are.
  margin: var(--layout-content-top-offset) 0 var(--spacing-lg)
    var(--layout-sidebar-inset);
  // Quasar's `.q-drawer__content` also carries its own `fit` utility class
  // (`width: 100% !important`), which silently won this width rule even
  // before this session's changes (a latent, previously tiny/unnoticed
  // 24px overflow past the rail edge — now that the inset is 116px, it
  // showed up as a visibly too-wide panel eating into the content gutter).
  width: calc(100% - var(--layout-sidebar-inset)) !important;
  // The Figma panel doesn't stretch to the footer either — its height (646px)
  // is bounded by its own content and ends exactly where the main content
  // column ends (y863 both), leaving a large gap above the footer. This app's
  // sidebar has more content than that one static frame did (the Federação/
  // Singular directories below can expand), so an intrinsic height capped by
  // `max-height` is the adaptation: it hugs short content like Figma shows,
  // but still scrolls internally (`&__scroll`) instead of overflowing the
  // drawer if a directory is expanded.
  height: auto;
  max-height: calc(100% - var(--layout-content-top-offset) - var(--spacing-lg));
}

.app-sidebar {
  &__profile {
    // Figma's sidebar panel (node 64:849) is a single flat card — the
    // profile block and nav rows have no divider lines between them, just
    // spacing. Dropped the `border-bottom`/`border-top` this had
    // (unevidenced against Figma).
    flex-shrink: 0;
    padding: var(--spacing-md);

    // home.txt "Editar perfil": italic, 10px, the same "cinza" ink as the
    // rest of the Home frame (#585C65) — `DsButton`'s `link` variant
    // defaults to `--color-primary` (brand green) with an underline, which
    // reads much louder here than Figma's subdued caption-style link.
    // Scoped to this instance (not `ds.scss`'s shared `.ds-profile-summary`
    // rule) since other consumers of the component may legitimately want
    // the DS default. Figma has no dark frame, so dark theme keeps the
    // app's own secondary-text token (same call as `/app`'s `--app-page-ink`).
    --app-sidebar-edit-ink: #585c65;

    :deep(.ds-profile-summary__edit) {
      // `!important` needed: `DsButton` passes `color="primary"` to Quasar's
      // `q-btn`, which applies its own `.text-primary` utility class with
      // `!important` — confirmed via screenshot that the rule above was
      // silently losing to it (rendered brand green, not the Figma ink)
      // despite the CSS looking correct on paper. Same class of bug as the
      // `.fit` width override already documented for the sidebar rail.
      color: var(--app-sidebar-edit-ink) !important;
      // Left fixed (not fluid like its siblings below) — Figma's own 10px
      // is already at the edge of legibility; shrinking it further below
      // 1920px would make "Editar perfil" harder to read, not "harmonized".
      font-size: 10px;
      font-style: italic;
      text-decoration: none;
      // Without this, the button (naturally inline-level) rides along on
      // the same line as `__name` once `__name` stops being a block-level
      // `<p>` below — confirmed by screenshot, "Editar perfil" merged into
      // the "Olá, Monalisa!" line instead of wrapping to its own row.
      display: block;
      margin-top: var(--spacing-xs, 4px);
    }

    // home.png: greeting and name read as a single line ("Olá, Monalisa!"),
    // same weight/tone throughout — not the DS default of two stacked
    // block-level <p> (light "Olá," over a bold, larger, separately-colored
    // name). No dedicated CSS exists yet for these two classes anywhere in
    // the DS, so this is a net-new rule, not an override of an established
    // default.
    :deep(.ds-profile-summary__greeting),
    :deep(.ds-profile-summary__name) {
      display: inline;
      margin: 0;
      // `--text-body-size` (16px) fluid below 1920px, off the same
      // contain-fit reference as the layout tokens — floored at 12px
      // (not the strict proportional 10.67px) for the same legibility
      // reason as the card description in `pages/app/index.vue`.
      font-size: clamp(
        12px,
        calc(0.0083333 * var(--layout-figma-viewport)),
        var(--text-body-size)
      );
      font-weight: var(--text-body-weight);
      color: var(--app-sidebar-edit-ink);
      // `.ds-profile-summary__content`'s own `min-width: 0` (ds.scss) only
      // lets the flex column shrink — it doesn't let an unbroken string
      // (no spaces, e.g. an email fallback name) wrap. Confirmed via
      // Playwright: an email-shaped name pushed `.app-sidebar`'s own
      // `scrollWidth` 36px past its `clientWidth`, a real horizontal
      // scrollbar, not just a cosmetic clip.
      overflow-wrap: anywhere;
    }

    :deep(.ds-profile-summary__name) {
      margin-left: 0.25em;
    }

    :deep(.ds-profile-summary__name)::after {
      content: "!";
    }
  }

  &__scroll {
    flex: 1;
    min-height: 0;
  }

  // `SidebarMenu` (`.sidebar-section`) and `SidebarDirectorySection`
  // (`.sidebar-directory`) are separate top-level block elements here, not
  // a single component's own repeated rows — so the `gap` each already
  // applies *within* its own item list (`.sidebar-section__nav`) never
  // applied *between* these blocks, which sat flush against each other
  // (measured via Playwright: 0px between "Página inicial" and
  // "Federação"). This is the one place that composes all of them, so it's
  // the right place to own the spacing between them rather than hardcoding
  // a bottom margin into each leaf component.
  &__menu {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-md);
  }
}

[data-theme="dark"] .app-sidebar__profile {
  --app-sidebar-edit-ink: var(--color-text-secondary);
}
</style>
