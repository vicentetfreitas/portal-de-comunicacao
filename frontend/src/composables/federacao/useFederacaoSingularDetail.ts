import { computed, onMounted, ref, watch, type Ref } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { areaService, singularService } from "@/services/organization";
import type { AreaResponse } from "@/types/organization/area.types";
import type { SingularResponse } from "@/types/organization/singular.types";

const AREAS_PAGE_SIZE = 200;

/**
 * Federação > Singular — leitura de uma Singular arbitrária (nome/sigla) e
 * suas Áreas ativas, consumindo `GET /api/v1/singulares/{id}` (FT-SINGULAR)
 * e `GET /api/v1/areas?singularId=…` (FT-AREA) — ambas já `APPROVED`, mesmo
 * par de endpoints que `useOrganizationDirectory.ts` já usa para a listagem
 * "Federação"/"Singular" da sidebar.
 */
export function useFederacaoSingularDetail(id: Ref<string>) {
  const { handleError } = useStandardErrorHandling();

  const singular = ref<SingularResponse | null>(null);
  const areas = ref<AreaResponse[]>([]);
  const loading = ref(true);
  const notFound = ref(false);

  const numericId = computed(() => Number(id.value));
  const isAreasEmpty = computed(
    () => !loading.value && areas.value.length === 0
  );

  async function load(): Promise<void> {
    loading.value = true;
    notFound.value = false;
    singular.value = null;
    areas.value = [];

    if (!Number.isFinite(numericId.value) || numericId.value <= 0) {
      notFound.value = true;
      loading.value = false;
      return;
    }

    try {
      singular.value = await singularService.getById(numericId.value);
      const page = await areaService.list({
        singularId: numericId.value,
        status: "ACTIVE",
        size: AREAS_PAGE_SIZE,
        sort: "name"
      });
      areas.value = page.content;
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
    void load();
  });

  watch(id, () => {
    void load();
  });

  return {
    singular,
    areas,
    loading,
    notFound,
    isAreasEmpty,
    load
  };
}
