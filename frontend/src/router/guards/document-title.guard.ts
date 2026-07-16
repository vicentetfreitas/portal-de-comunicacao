import type {
  RouteLocationNormalized,
  RouteLocationNormalizedLoaded
} from "vue-router";

import { APP_DOCUMENT_TITLE_SUFFIX } from "@/config/router";
import { i18n } from "@/i18n/instance";

function resolvePageTitle(route: RouteLocationNormalized): string | undefined {
  if (route.meta.documentTitle) {
    return route.meta.documentTitle;
  }

  if (route.meta.pageTitleKey) {
    return i18n.global.t(route.meta.pageTitleKey);
  }

  return undefined;
}

export function updateDocumentTitle(
  route: RouteLocationNormalizedLoaded
): void {
  const pageTitle = resolvePageTitle(route);
  document.title = pageTitle
    ? `${pageTitle} | ${APP_DOCUMENT_TITLE_SUFFIX}`
    : APP_DOCUMENT_TITLE_SUFFIX;
}
