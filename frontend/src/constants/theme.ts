/**
 * Theme mode constants — infrastructure for PKG-FE-S0-02.
 * Runtime switching via useTheme composable is delivered in PKG-FE-S0-08.
 */
export const THEME_STORAGE_KEY = "portal-theme-mode";

export const THEME_MODES = ["light", "dark", "auto"] as const;

export type ThemeMode = (typeof THEME_MODES)[number];
