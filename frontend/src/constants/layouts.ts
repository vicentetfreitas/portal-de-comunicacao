import type { AppLayoutName } from "@/types/router-meta";

export const LAYOUT_NAMES = {
  AUTH: "auth",
  MAIN: "main",
  ADMIN: "admin",
  PUBLIC: "public"
} as const satisfies Record<string, AppLayoutName>;

export const DEFAULT_LAYOUT: AppLayoutName = LAYOUT_NAMES.PUBLIC;
