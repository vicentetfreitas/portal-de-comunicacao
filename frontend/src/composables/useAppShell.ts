import { inject, provide, ref, type InjectionKey, type Ref } from "vue";

export interface AppShellState {
  leftDrawerOpen: Ref<boolean>;
  toggleDrawer: () => void;
}

const APP_SHELL_KEY: InjectionKey<AppShellState> = Symbol("app-shell");

export function createAppShellState(): AppShellState {
  const leftDrawerOpen = ref(false);

  function toggleDrawer(): void {
    leftDrawerOpen.value = !leftDrawerOpen.value;
  }

  return {
    leftDrawerOpen,
    toggleDrawer
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
