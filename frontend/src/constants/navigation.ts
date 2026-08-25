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
  },
  {
    labelKey: "areaColaborador.hub.title",
    to: ROUTE_PATHS.AREA_COLABORADOR_HUB,
    icon: "mdi-domain",
    placement: "trailing"
  }
];

export const ADMIN_NAV_ITEMS: AppNavItem[] = [
  ...MAIN_NAV_ITEMS,
  {
    labelKey: "layout.nav.admin",
    to: ROUTE_PATHS.ADMIN,
    icon: "mdi-shield-cog",
    section: "admin"
  },
  {
    labelKey: "singular.hub.title",
    to: ROUTE_PATHS.SINGULAR_HUB,
    icon: "mdi-domain",
    section: "admin"
  },
  {
    labelKey: "equipe.hub.title",
    to: ROUTE_PATHS.EQUIPE_HUB,
    icon: "mdi-account-group",
    section: "admin"
  },
  {
    labelKey: "colaborador.hub.title",
    to: ROUTE_PATHS.COLABORADOR_HUB,
    icon: "mdi-account-multiple",
    section: "admin"
  }
];
