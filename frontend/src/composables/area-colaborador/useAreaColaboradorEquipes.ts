import { computed, onMounted, ref } from "vue";

import { useSession } from "@/composables/useSession";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { equipeService } from "@/services/organization";
import type { EquipeResponse } from "@/types/organization/equipe.types";

/**
 * FT-AREA-COLABORADOR (TK-AREA-COLAB-003) — equipes vinculadas à Área do Contexto Ativo.
 * Reutiliza useSession (areaId) e equipeService.list já existentes (FT-EQUIPE).
 */
export function useAreaColaboradorEquipes() {
  const { activeContext } = useSession();
  const { handleError } = useStandardErrorHandling();

  const equipes = ref<EquipeResponse[]>([]);
  const loading = ref(true);

  const isEmpty = computed(() => !loading.value && equipes.value.length === 0);

  async function loadEquipes(): Promise<void> {
    loading.value = true;
    equipes.value = [];

    const areaId = activeContext.value?.areaId;
    if (areaId == null) {
      loading.value = false;
      return;
    }

    try {
      const page = await equipeService.list({ areaId });
      equipes.value = page.content;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value = false;
    }
  }

  onMounted(() => {
    void loadEquipes();
  });

  return {
    equipes,
    loading,
    isEmpty,
    loadEquipes
  };
}
