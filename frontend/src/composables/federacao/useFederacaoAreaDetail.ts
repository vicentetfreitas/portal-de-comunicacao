import { computed, onMounted, ref, watch, type Ref } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { areaService } from "@/services/organization";
import type { AreaResponse } from "@/types/organization/area.types";

/**
 * Federação — leitura de uma Área arbitrária da federação (não apenas a do
 * Contexto Ativo do colaborador — ver `useAreaColaboradorDetail` para essa).
 * Mesma API já `APPROVED` (`GET /api/v1/areas/{id}`, FT-AREA), parametrizada
 * pelo `:id` da rota em vez de `activeContext.areaId`.
 */
export function useFederacaoAreaDetail(id: Ref<string>) {
  const { handleError } = useStandardErrorHandling();

  const area = ref<AreaResponse | null>(null);
  const loading = ref(true);
  const notFound = ref(false);

  const numericId = computed(() => Number(id.value));

  async function loadArea(): Promise<void> {
    loading.value = true;
    notFound.value = false;
    area.value = null;

    if (!Number.isFinite(numericId.value) || numericId.value <= 0) {
      notFound.value = true;
      loading.value = false;
      return;
    }

    try {
      area.value = await areaService.getById(numericId.value);
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

  watch(id, () => {
    void loadArea();
  });

  return {
    area,
    loading,
    notFound,
    loadArea
  };
}
