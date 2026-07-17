import { onMounted, ref, type Ref } from "vue";

import type { DsSelectOption } from "@/components/ds";
import { areaService } from "@/services/organization";

export function useEquipeAreaOptions(): {
  areaOptions: Ref<DsSelectOption<string>[]>;
  loadingAreas: Ref<boolean>;
  loadAreas: () => Promise<void>;
} {
  const areaOptions = ref<DsSelectOption<string>[]>([]);
  const loadingAreas = ref(false);

  async function loadAreas(): Promise<void> {
    loadingAreas.value = true;

    try {
      const page = await areaService.list({
        status: "ACTIVE",
        page: 0,
        size: 100,
        sort: "name,asc"
      });

      areaOptions.value = page.content.map(area => ({
        label: area.name,
        value: String(area.id)
      }));
    } finally {
      loadingAreas.value = false;
    }
  }

  onMounted(() => {
    void loadAreas();
  });

  return { areaOptions, loadingAreas, loadAreas };
}
