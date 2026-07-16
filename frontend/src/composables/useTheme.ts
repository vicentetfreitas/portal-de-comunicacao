import { Dark } from "quasar";
import { computed, readonly, ref } from "vue";

export type ThemeMode = "light" | "dark" | "auto";

export const THEME_ATTRIBUTE = "data-theme";

const mode = ref<ThemeMode>("auto");
let mediaQuery: MediaQueryList | null = null;

function resolveSystemTheme(): "light" | "dark" {
  if (typeof window === "undefined") {
    return "light";
  }

  return window.matchMedia("(prefers-color-scheme: dark)").matches
    ? "dark"
    : "light";
}

function resolveEffectiveTheme(currentMode: ThemeMode): "light" | "dark" {
  return currentMode === "auto" ? resolveSystemTheme() : currentMode;
}

export function applyTheme(resolved: "light" | "dark"): void {
  if (typeof document === "undefined") {
    return;
  }

  document.documentElement.setAttribute(THEME_ATTRIBUTE, resolved);
  Dark.set(resolved === "dark");
}

function onSystemThemeChange(event: MediaQueryListEvent): void {
  if (mode.value === "auto") {
    applyTheme(event.matches ? "dark" : "light");
  }
}

function bindSystemThemeListener(): void {
  if (typeof window === "undefined") {
    return;
  }

  mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
  mediaQuery.addEventListener("change", onSystemThemeChange);
}

/**
 * Initializes theme infrastructure — called from boot/theme.ts.
 */
export function initTheme(): void {
  applyTheme(resolveEffectiveTheme(mode.value));
  bindSystemThemeListener();
}

export function useTheme() {
  const resolvedMode = computed(() => resolveEffectiveTheme(mode.value));
  const isDark = computed(() => resolvedMode.value === "dark");

  function setMode(newMode: ThemeMode): void {
    mode.value = newMode;
    applyTheme(resolveEffectiveTheme(newMode));
  }

  function toggle(): void {
    setMode(isDark.value ? "light" : "dark");
  }

  return {
    mode: readonly(mode),
    resolvedMode,
    isDark,
    setMode,
    toggle,
    THEME_ATTRIBUTE
  };
}
