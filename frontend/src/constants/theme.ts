export const THEME_STORAGE_KEY = "portal-theme-mode";

export const THEME_MODES = ["light", "dark", "auto"] as const;

export type ThemeMode = (typeof THEME_MODES)[number];
