import { onMounted, ref } from "vue";

import { useSession } from "@/composables/useSession";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { areaService } from "@/services/organization";
import type { AreaResponse } from "@/types/organization/area.types";

/**
 * FT-AREA-COLABORADOR (TK-AREA-COLAB-002) — leitura da Área do Contexto Ativo.
 * Reutiliza useSession (areaId) e areaService.getById já existentes.
 */
export function useAreaColaboradorDetail() {
  const { activeContext } = useSession();
  const { handleError } = useStandardErrorHandling();

  const area = ref<AreaResponse | null>(null);
  const loading = ref(true);
  const notFound = ref(false);

  async function loadArea(): Promise<void> {
    loading.value = true;
    notFound.value = false;
    area.value = null;

    const areaId = activeContext.value?.areaId;
    if (areaId == null) {
      notFound.value = true;
      loading.value = false;
      return;
    }

    try {
      area.value = await areaService.getById(areaId);
    } catch (error) {
      const apiError = handleError(error, { silent: true });
      if (apiError.status === 404 || apiError.category === "not_found") {
        notFound.value = true;
        return;
      }
      handleError(error);
    } finally {
      loading.value = false;
    }
  }

  onMounted(() => {
    void loadArea();
  });

  return {
    area,
    loading,
    notFound,
    loadArea
  };
}
