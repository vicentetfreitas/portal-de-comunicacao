import {
  computed,
  inject,
  provide,
  ref,
  type ComputedRef,
  type InjectionKey,
  type Ref
} from "vue";
import { useQuasar } from "quasar";

export interface AppShellState {
  leftDrawerOpen: Ref<boolean>;
  sidebarCollapsed: Ref<boolean>;
  isMobile: ComputedRef<boolean>;
  toggleDrawer: () => void;
  toggleSidebarCollapse: () => void;
}

const APP_SHELL_KEY: InjectionKey<AppShellState> = Symbol("app-shell");

export function createAppShellState(): AppShellState {
  const $q = useQuasar();
  const leftDrawerOpen = ref(false);
  const sidebarCollapsed = ref(false);

  const isMobile = computed(() => $q.screen.lt.md);

  function toggleDrawer(): void {
    leftDrawerOpen.value = !leftDrawerOpen.value;
  }

  function toggleSidebarCollapse(): void {
    sidebarCollapsed.value = !sidebarCollapsed.value;
  }

  return {
    leftDrawerOpen,
    sidebarCollapsed,
    isMobile,
    toggleDrawer,
    toggleSidebarCollapse
  };
}

export function provideAppShell(): AppShellState {
  const state = createAppShellState();
  provide(APP_SHELL_KEY, state);
  return state;
}

export function useAppShell(): AppShellState {
  const state = inject(APP_SHELL_KEY);
  if (!state) {
    throw new Error("useAppShell must be used within an AppShell layout.");
  }
  return state;
}
