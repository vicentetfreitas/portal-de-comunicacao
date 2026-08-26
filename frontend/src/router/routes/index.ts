import type { RouteRecordRaw } from "vue-router";

import { areaColaboradorRoutes } from "./area-colaborador.routes";
import { federacaoRoutes } from "./federacao.routes";
import { foundationRoutes, lazyPages } from "./foundation.routes";
import { colaboradorRoutes } from "./organization/colaborador.routes";
import { equipeRoutes } from "./organization/equipe.routes";
import { singularRoutes } from "./organization/singular.routes";
import { perfilRoutes } from "./perfil.routes";
import { structuralRoutes } from "./structural.routes";

export {
  areaColaboradorRoutes,
  colaboradorRoutes,
  equipeRoutes,
  federacaoRoutes,
  foundationRoutes,
  lazyPages,
  perfilRoutes,
  singularRoutes,
  structuralRoutes
};

/**
 * Modular route registry — structural redirects, foundation pages, feature routes, catch-all.
 */
export function createModularRoutes(): RouteRecordRaw[] {
  return [
    ...structuralRoutes,
    ...foundationRoutes,
    ...singularRoutes,
    ...equipeRoutes,
    ...colaboradorRoutes,
    ...areaColaboradorRoutes,
    ...federacaoRoutes,
    ...perfilRoutes,
    {
      path: "/:pathMatch(.*)*",
      name: "not-found",
      component: lazyPages.notFound,
      meta: {
        layout: "public",
        public: true,
        pageTitleKey: "common.notFound"
      }
    }
  ];
}
