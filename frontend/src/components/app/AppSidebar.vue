<template>
  <q-drawer
    v-model="drawerOpen"
    class="app-sidebar"
    :breakpoint="960"
    :width="LAYOUT_SIDEBAR_WIDTH"
    :mini="sidebarMini"
    :mini-width="LAYOUT_SIDEBAR_MINI_WIDTH"
    bordered
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
        :mini="sidebarMini"
        @edit="$emit('profileEdit')"
      />
    </div>

    <q-scroll-area class="app-sidebar__scroll">
      <SidebarMenu
        :items="primaryItems"
        :section-title="primarySectionTitle"
        :mini="sidebarMini"
      />

      <SidebarMenu
        v-if="adminItems.length > 0"
        :items="adminItems"
        :section-title="adminSectionTitle"
        :mini="sidebarMini"
      />
    </q-scroll-area>

    <div class="app-sidebar__footer">
      <DsButton
        variant="ghost"
        size="sm"
        class="full-width"
        :aria-label="$t('layout.sidebar.toggleCollapse')"
        @click="shell.toggleSidebarCollapse()"
      >
        <DsIcon
          :name="sidebarMini ? 'mdi-chevron-right' : 'mdi-chevron-left'"
          size="sm"
        />
        <span v-if="!sidebarMini" class="q-ml-sm">{{
          $t("layout.sidebar.collapse")
        }}</span>
      </DsButton>
    </div>
  </q-drawer>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsIcon } from "@/components/ds";
import {
  LAYOUT_SIDEBAR_MINI_WIDTH,
  LAYOUT_SIDEBAR_WIDTH
} from "@/constants/layout";
import { useAppShell } from "@/composables/useAppShell";

import { SidebarMenu, SidebarProfile } from "./sidebar";

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
  }>(),
  {
    showProfile: true,
    profileName: "Colaborador",
    profileGreeting: "Olá,",
    profileInitials: "CO",
    profileEditLabel: "Editar perfil",
    showEdit: true
  }
);

defineEmits<{
  profileEdit: [];
}>();

const { t } = useI18n();
const shell = useAppShell();

const drawerOpen = computed({
  get: () => shell.leftDrawerOpen.value,
  set: (value: boolean) => {
    shell.leftDrawerOpen.value = value;
  }
});

const sidebarMini = computed(
  () => shell.sidebarCollapsed.value && !shell.isMobile.value
);

const primaryItems = computed(() =>
  props.items.filter(item => item.section !== "admin")
);

const adminItems = computed(() =>
  props.items.filter(item => item.section === "admin")
);

const primarySectionTitle = computed(() => t("layout.sidebar.title"));
const adminSectionTitle = computed(() => t("layout.sidebar.adminSection"));
</script>

<style scoped lang="scss">
.app-sidebar {
  background-color: var(--color-surface);
  color: var(--color-text-primary);
  border-right: var(--border-width-thin) var(--border-style-default)
    var(--color-border);
  display: flex;
  flex-direction: column;

  &__profile {
    flex-shrink: 0;
    padding: var(--spacing-md);
    border-bottom: var(--border-width-thin) var(--border-style-default)
      var(--color-border);
  }

  &__scroll {
    flex: 1;
    min-height: 0;
  }

  &__footer {
    flex-shrink: 0;
    padding: var(--spacing-sm);
    border-top: var(--border-width-thin) var(--border-style-default)
      var(--color-border);
    background-color: var(--color-surface);
    min-height: var(--layout-footer-height);
    display: flex;
    align-items: center;
  }
}
</style>
