import { computed } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute } from "vue-router";

import type { DsBreadcrumbItem } from "@/components/ds";

export function useLayoutMeta() {
  const route = useRoute();
  const { t } = useI18n();

  const showBreadcrumbs = computed(() => route.meta.showBreadcrumbs === true);

  const breadcrumbs = computed<DsBreadcrumbItem[]>(() => {
    const items = route.meta.breadcrumbs ?? [];
    return items.map(item => ({
      label: t(item.labelKey),
      ...(item.to !== undefined ? { to: item.to } : {}),
      ...(item.icon !== undefined ? { icon: item.icon } : {})
    }));
  });

  const pageTitle = computed(() => {
    if (route.meta.pageTitleKey) {
      return t(route.meta.pageTitleKey);
    }
    return undefined;
  });

  return {
    breadcrumbs,
    pageTitle,
    showBreadcrumbs
  };
}
