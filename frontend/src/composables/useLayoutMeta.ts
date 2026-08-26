import { computed } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute } from "vue-router";

import type { DsBreadcrumbItem } from "@/components/ds";

export function useLayoutMeta() {
  const route = useRoute();
  const { t } = useI18n();

  const showBreadcrumbs = computed(() => route.meta.showBreadcrumbs === true);

  // Lets a breadcrumb's static `to` (route meta is defined once at router
  // setup, not per-navigation) target a dynamic parent by reusing the same
  // `:param` placeholder syntax as `ROUTE_PATHS` itself (e.g. `to:
  // ROUTE_PATHS.FEDERACAO_AREA_DETAIL`, `"/app/federacao/areas/:id"`) —
  // resolved here against the *current* route's own params. A route with no
  // matching param (or a plain path with no placeholder) passes through
  // unchanged.
  function resolveBreadcrumbTo(to: string): string {
    const params = route.params as Record<string, string | string[]>;
    return to.replace(/:(\w+)/g, (match, key: string) => {
      const value = params[key];
      return typeof value === "string" ? value : match;
    });
  }

  const breadcrumbs = computed<DsBreadcrumbItem[]>(() => {
    const items = route.meta.breadcrumbs ?? [];
    return items.map(item => ({
      label: t(item.labelKey),
      ...(item.to !== undefined ? { to: resolveBreadcrumbTo(item.to) } : {}),
      ...(item.icon !== undefined ? { icon: item.icon } : {})
    }));
  });

  const pageTitle = computed(() => {
    if (route.meta.pageTitleKey) {
      return t(route.meta.pageTitleKey);
    }
    return undefined;
  });

  // Shown on every page that opts into breadcrumbs (`route.meta.
  // showBreadcrumbs`) — in practice every admin/organization page, which
  // always has at least "Home → this page" (length ≥ 2). The `length > 1`
  // floor just guards a route that someday defines a single-item
  // breadcrumb trail; it isn't excluding hub pages on purpose — a hub
  // reached from the sidebar still has real navigation history to return
  // to, same as any other page.
  const showBackButton = computed(
    () => showBreadcrumbs.value && breadcrumbs.value.length > 1
  );

  return {
    breadcrumbs,
    pageTitle,
    showBreadcrumbs,
    showBackButton
  };
}
