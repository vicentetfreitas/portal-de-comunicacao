<template>
  <div class="sidebar-directory">
    <button
      type="button"
      class="sidebar-directory__trigger"
      :class="{ 'sidebar-directory__trigger--mini': mini }"
      :aria-expanded="expanded"
      :aria-label="mini ? label : undefined"
      @click="toggle"
    >
      <DsIcon :name="icon" size="md" class="sidebar-directory__icon" />
      <span v-if="!mini" class="sidebar-directory__label">{{ label }}</span>
      <DsIcon
        v-if="!mini"
        :name="expanded ? 'mdi-chevron-up' : 'mdi-chevron-down'"
        size="sm"
        class="sidebar-directory__chevron"
      />
    </button>

    <div v-if="expanded && !mini" class="sidebar-directory__panel">
      <DsSearchInput
        v-model="searchModel"
        :placeholder="searchPlaceholder"
        dense
      />

      <p v-if="loading" class="sidebar-directory__status">
        {{ $t("common.loading") }}
      </p>
      <p v-else-if="items.length === 0" class="sidebar-directory__status">
        {{ emptyLabel }}
      </p>
      <ul v-else class="sidebar-directory__list">
        <li
          v-for="item in items"
          :key="item.id"
          class="sidebar-directory__item"
          :class="{
            'sidebar-directory__item--highlighted': item.highlighted
          }"
        >
          <component
            :is="item.to ? 'router-link' : 'div'"
            :to="item.to"
            class="sidebar-directory__item-link"
          >
            <span class="sidebar-directory__item-name">{{ item.name }}</span>
            <span
              v-if="item.subtitle"
              class="sidebar-directory__item-subtitle"
              >{{ item.subtitle }}</span
            >
          </component>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";

import { DsIcon, DsSearchInput } from "@/components/ds";

export interface SidebarDirectoryItem {
  id: number;
  name: string;
  subtitle?: string;
  /** Marks the colaborador's own vínculo entry — sorted first, rendered bold. */
  highlighted?: boolean;
  /** Route to navigate to on click — plain (non-interactive) row when absent. */
  to?: string;
}

const props = defineProps<{
  label: string;
  icon: string;
  items: SidebarDirectoryItem[];
  loading: boolean;
  search: string;
  searchPlaceholder: string;
  emptyLabel: string;
  mini?: boolean;
}>();

const emit = defineEmits<{
  "update:search": [string];
  expand: [];
}>();

const searchModel = computed<string>({
  get: () => props.search,
  set: value => emit("update:search", value ?? "")
});

const expanded = ref(false);

function toggle(): void {
  if (props.mini) {
    return;
  }
  expanded.value = !expanded.value;
  if (expanded.value) {
    emit("expand");
  }
}
</script>

<style scoped lang="scss">
.sidebar-directory {
  // `SidebarMenuItem` rows sit inside `SidebarSection.vue`'s own
  // `.sidebar-section__nav`, which adds `padding: 0 var(--spacing-sm)`
  // around every row on top of `DsNavItem`'s own `--spacing-md` — this
  // block is a top-level sibling in `AppSidebar.vue`, not nested inside a
  // `SidebarSection`, so it never got that extra inset: its icon rendered
  // 8px left of every other row's icon (measured via Playwright: 132px vs
  // 140px at a 1920px viewport). Matching that same inset here re-aligns
  // the whole block (trigger and its expanded panel) with the rest of the
  // menu instead of patching just the icon.
  padding: 0 var(--spacing-sm);

  &__trigger {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    width: 100%;
    padding: var(--spacing-sm) var(--spacing-md);
    border: none;
    border-radius: var(--radius-md);
    background: transparent;
    color: var(--color-text-primary);
    font-family: inherit;
    font-size: var(--text-body-size);
    font-weight: var(--text-card-title-weight);
    cursor: pointer;
    text-align: left;

    &:hover {
      background-color: var(--color-surface-hover);
    }

    &--mini {
      justify-content: center;
      padding: var(--spacing-sm);
    }
  }

  &__label {
    flex: 1;
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__chevron {
    flex-shrink: 0;
    color: var(--color-text-secondary);
  }

  &__panel {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-sm);
    padding: var(--spacing-xs) var(--spacing-md) var(--spacing-sm);
    // A flex column's cross-axis (width) items default to `min-width: auto`
    // — an unbroken long token (a name/acronym with no spaces) in a child
    // below can force this whole column wider than the sidebar rail
    // instead of shrinking to fit it, pushing the drawer's real horizontal
    // overflow (`min-width: 0` here plus the wrap rules on the item text
    // below close the loop — width containment alone doesn't help unless
    // the text itself can also break).
    min-width: 0;
  }

  &__status {
    margin: 0;
    padding: var(--spacing-xs) var(--spacing-sm);
    color: var(--color-text-secondary);
    font-size: var(--text-body-small-size);
  }

  &__list {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-xs);
    margin: 0;
    padding: 0;
    list-style: none;
    max-height: 220px;
    overflow-y: auto;
    min-width: 0;
  }

  &__item {
    border-radius: var(--radius-sm);
    min-width: 0;

    &:hover {
      background-color: var(--color-surface-hover);
    }

    // Colaborador's own vínculo área (sorted first by the composable) —
    // explicit product decision to make it visually stand out, not just
    // lead the list positionally. Bold name only (no badge/subtitle text —
    // those read as noise here, see `AppSidebar.vue`'s federation mapping).
    &--highlighted {
      background-color: var(--color-surface-active);

      &:hover {
        background-color: var(--color-surface-hover);
      }

      .sidebar-directory__item-name {
        font-weight: var(--font-weight-bold);
      }
    }
  }

  &__item-link {
    display: flex;
    flex-direction: column;
    padding: var(--spacing-xs) var(--spacing-sm);
    min-width: 0;
    color: inherit;
    text-decoration: none;
    cursor: pointer;
  }

  &__item-name {
    font-size: var(--text-body-small-size);
    color: var(--color-text-primary);
    // API-sourced names/acronyms aren't curated UI copy — an unbroken long
    // one (no spaces) must still break instead of forcing the sidebar
    // wider than its rail (reproduced with a synthetic long acronym: the
    // whole drawer stretched ~105px past its right edge without this).
    overflow-wrap: break-word;
    word-break: break-word;
  }

  &__item-subtitle {
    font-size: var(--text-caption-size);
    color: var(--color-text-secondary);
    overflow-wrap: break-word;
    word-break: break-word;
  }
}
</style>
