import type { Router } from "vue-router";

import { createAuthGuard } from "./auth.guard";
import { createAuthorizationGuard } from "./authorization.guard";
import { updateDocumentTitle } from "./document-title.guard";

export function registerRouterGuards(router: Router): void {
  router.beforeEach(createAuthGuard());
  router.beforeEach(createAuthorizationGuard());
  router.afterEach(to => {
    updateDocumentTitle(to);
  });
}

export { createAuthGuard } from "./auth.guard";
export { createAuthorizationGuard } from "./authorization.guard";
export { updateDocumentTitle } from "./document-title.guard";
