import { defineRouter } from "#q-app";
import {
  createMemoryHistory,
  createRouter,
  createWebHashHistory,
  createWebHistory
} from "vue-router";

import "@/types/router-meta";

import { registerRouterGuards } from "./guards";
import { createModularRoutes } from "./routes";
import { updateDocumentTitle } from "./guards/document-title.guard";

export default defineRouter(() => {
  const createHistory = import.meta.env.QUASAR_SERVER
    ? createMemoryHistory
    : import.meta.env.QUASAR_VUE_ROUTER_MODE === "history"
      ? createWebHistory
      : createWebHashHistory;

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes: createModularRoutes(),
    history: createHistory(import.meta.env.QUASAR_VUE_ROUTER_BASE)
  });

  registerRouterGuards(Router);

  void Router.isReady().then(() => {
    updateDocumentTitle(Router.currentRoute.value);
  });

  return Router;
});
