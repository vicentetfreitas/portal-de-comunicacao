import type { AppNavItem } from "@/components/app/types";
import { ROUTE_PATHS } from "@/constants/routes";

export const MAIN_NAV_ITEMS: AppNavItem[] = [
  {
    labelKey: "layout.nav.home",
    to: ROUTE_PATHS.HOME,
    icon: "mdi-home"
  },
  {
    labelKey: "layout.nav.app",
    to: ROUTE_PATHS.APP,
    icon: "mdi-view-dashboard"
  },
  {
    labelKey: "layout.nav.showcase",
    to: ROUTE_PATHS.SHOWCASE,
    icon: "mdi-palette"
  }
];

export const ADMIN_NAV_ITEMS: AppNavItem[] = [
  ...MAIN_NAV_ITEMS,
  {
    labelKey: "layout.nav.admin",
    to: ROUTE_PATHS.ADMIN,
    icon: "mdi-shield-cog",
    section: "admin"
  }
];
