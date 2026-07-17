import { reactive, ref, type UnwrapNestedRefs } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { equipeService } from "@/services/organization";
import type {
  EquipeListParams,
  EquipeResponse,
  EquipeStatus
} from "@/types/organization/equipe.types";

export interface EquipeListFilters {
  status: EquipeStatus | null;
  areaId: number | null;
  name: string;
}

export interface EquipeTablePagination {
  sortBy: string;
  descending: boolean;
  page: number;
  rowsPerPage: number;
  rowsNumber: number;
}

export interface EquipeTableRequest {
  pagination: EquipeTablePagination;
}

export function createDefaultEquipeListFilters(): EquipeListFilters {
  return {
    status: null,
    areaId: null,
    name: ""
  };
}

export function createDefaultEquipeTablePagination(): EquipeTablePagination {
  return {
    sortBy: "name",
    descending: false,
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0
  };
}

export function buildEquipeListParams(
  pagination: EquipeTablePagination,
  filters: EquipeListFilters
): EquipeListParams {
  const params: EquipeListParams = {
    page: Math.max(pagination.page - 1, 0),
    size: pagination.rowsPerPage
  };

  if (pagination.sortBy) {
    const direction = pagination.descending ? "desc" : "asc";
    params.sort = `${pagination.sortBy},${direction}`;
  }

  if (filters.status) {
    params.status = filters.status;
  }
  if (filters.areaId !== null) {
    params.areaId = filters.areaId;
  }

  const name = filters.name.trim();
  if (name.length > 0) {
    params.name = name;
  }

  return params;
}

export function useEquipeList() {
  const rows = ref<EquipeResponse[]>([]);
  const loading = ref(true);
  const filters: UnwrapNestedRefs<EquipeListFilters> = reactive(
    createDefaultEquipeListFilters()
  );
  const pagination = ref<EquipeTablePagination>(
    createDefaultEquipeTablePagination()
  );
  const { withErrorHandling } = useStandardErrorHandling();

  async function fetchPage(): Promise<void> {
    loading.value = true;

    const result = await withErrorHandling(() =>
      equipeService.list(buildEquipeListParams(pagination.value, filters))
    );

    loading.value = false;

    if (!result) {
      rows.value = [];
      return;
    }

    rows.value = result.content;
    pagination.value = {
      ...pagination.value,
      rowsNumber: result.totalElements
    };
  }

  async function applyFilters(): Promise<void> {
    pagination.value = {
      ...pagination.value,
      page: 1
    };
    await fetchPage();
  }

  async function resetFilters(): Promise<void> {
    Object.assign(filters, createDefaultEquipeListFilters());
    pagination.value = createDefaultEquipeTablePagination();
    await fetchPage();
  }

  async function onTableRequest(request: EquipeTableRequest): Promise<void> {
    pagination.value = {
      ...pagination.value,
      ...request.pagination
    };
    await fetchPage();
  }

  return {
    rows,
    loading,
    filters,
    pagination,
    fetchPage,
    applyFilters,
    resetFilters,
    onTableRequest
  };
}
