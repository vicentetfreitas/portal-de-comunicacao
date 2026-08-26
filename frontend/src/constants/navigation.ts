import type { AppNavItem } from "@/components/app/types";
import { ROUTE_PATHS } from "@/constants/routes";

export const MAIN_NAV_ITEMS: AppNavItem[] = [
  {
    // Figma (frame Home, node 7:3) pairs "Página inicial" with a house
    // icon (Code Connect: component "Home", node 64:856) — not a dashboard
    // glyph. `ROUTE_PATHS.HOME` ("/") is the public landing page and has
    // no entry in the production collaborator menu
    // (docs/discovery/frontend-production-discovery.md §"Menus") nor in
    // any audited Figma frame — it was a duplicate "Início" row above this
    // one, both pointing at effectively the same landing concept.
    labelKey: "layout.nav.app",
    to: ROUTE_PATHS.APP,
    icon: "mdi-home"
  }
  // "Áreas" (FT-AREA-COLABORADOR, `ROUTE_PATHS.AREA_COLABORADOR_HUB`)
  // removed from the sidebar — explicit product decision, 2026-08-26: the
  // "Federação" directory now covers the same (and more general) ground,
  // including the colaborador's own área highlighted at the top of that
  // list. The route/pages themselves are untouched — FT-AREA-COLABORADOR
  // stays DONE, just unlinked from this nav — see
  // docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md.
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
