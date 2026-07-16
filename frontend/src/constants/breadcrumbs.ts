/**
 * Default breadcrumb icons — structural constants for route meta.
 */
export const BREADCRUMB_ICONS = {
  HOME: "mdi-home",
  SHOWCASE: "mdi-palette",
  AUTH: "mdi-login",
  APP: "mdi-view-dashboard",
  ADMIN: "mdi-shield-account",
  SETTINGS: "mdi-cog",
  DOCUMENT: "mdi-file-document-outline",
  FOLDER: "mdi-folder-outline",
  USER: "mdi-account-outline"
} as const;

export type BreadcrumbIcon =
  (typeof BREADCRUMB_ICONS)[keyof typeof BREADCRUMB_ICONS];
