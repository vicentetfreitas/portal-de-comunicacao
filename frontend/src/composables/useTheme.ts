import { Dark } from "quasar";
import { computed, readonly, ref } from "vue";

import {
  THEME_MODES,
  THEME_STORAGE_KEY,
  type ThemeMode
} from "@/constants/theme";

export type { ThemeMode };

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

function readStoredMode(): ThemeMode | null {
  if (typeof window === "undefined") {
    return null;
  }

  try {
    const stored = window.localStorage.getItem(THEME_STORAGE_KEY);
    return stored && (THEME_MODES as readonly string[]).includes(stored)
      ? (stored as ThemeMode)
      : null;
  } catch {
    // Storage unavailable (private mode, disabled, quota) — fall back to
    // system preference for this session.
    return null;
  }
}

function persistMode(newMode: ThemeMode): void {
  if (typeof window === "undefined") {
    return;
  }

  try {
    window.localStorage.setItem(THEME_STORAGE_KEY, newMode);
  } catch {
    // Same fallback as readStoredMode — theme still applies, just won't
    // survive a reload.
  }
}

/**
 * Initializes theme infrastructure — called from boot/theme.ts. Restores an
 * explicit user choice from storage (DEC-FA-005); falls back to following
 * the OS preference (`auto`) when nothing was saved yet.
 */
export function initTheme(): void {
  mode.value = readStoredMode() ?? "auto";
  applyTheme(resolveEffectiveTheme(mode.value));
  bindSystemThemeListener();
}

export function useTheme() {
  const resolvedMode = computed(() => resolveEffectiveTheme(mode.value));
  const isDark = computed(() => resolvedMode.value === "dark");

  function setMode(newMode: ThemeMode): void {
    mode.value = newMode;
    persistMode(newMode);
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
