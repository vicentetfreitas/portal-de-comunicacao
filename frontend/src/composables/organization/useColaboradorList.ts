import { reactive, ref, type UnwrapNestedRefs } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { colaboradorService } from "@/services/organization";
import type {
  ColaboradorListParams,
  ColaboradorResponse,
  ColaboradorStatus
} from "@/types/organization/colaborador.types";

export interface ColaboradorListFilters {
  status: ColaboradorStatus | null;
  singularId: number | null;
  areaId: number | null;
  teamId: number | null;
  name: string;
  email: string;
}

export interface ColaboradorTablePagination {
  sortBy: string;
  descending: boolean;
  page: number;
  rowsPerPage: number;
  rowsNumber: number;
}

export interface ColaboradorTableRequest {
  pagination: ColaboradorTablePagination;
}

export function createDefaultColaboradorListFilters(): ColaboradorListFilters {
  return {
    status: null,
    singularId: null,
    areaId: null,
    teamId: null,
    name: "",
    email: ""
  };
}

export function createDefaultColaboradorTablePagination(): ColaboradorTablePagination {
  return {
    sortBy: "name",
    descending: false,
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0
  };
}

export function buildColaboradorListParams(
  pagination: ColaboradorTablePagination,
  filters: ColaboradorListFilters
): ColaboradorListParams {
  const params: ColaboradorListParams = {
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
  if (filters.singularId !== null) {
    params.singularId = filters.singularId;
  }
  if (filters.areaId !== null) {
    params.areaId = filters.areaId;
  }
  if (filters.teamId !== null) {
    params.teamId = filters.teamId;
  }

  const name = filters.name.trim();
  if (name.length > 0) {
    params.name = name;
  }

  const email = filters.email.trim();
  if (email.length > 0) {
    params.email = email;
  }

  return params;
}

export function useColaboradorList() {
  const rows = ref<ColaboradorResponse[]>([]);
  const loading = ref(true);
  const filters: UnwrapNestedRefs<ColaboradorListFilters> = reactive(
    createDefaultColaboradorListFilters()
  );
  const pagination = ref<ColaboradorTablePagination>(
    createDefaultColaboradorTablePagination()
  );
  const { withErrorHandling } = useStandardErrorHandling();

  async function fetchPage(): Promise<void> {
    loading.value = true;

    const result = await withErrorHandling(() =>
      colaboradorService.list(
        buildColaboradorListParams(pagination.value, filters)
      )
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
    Object.assign(filters, createDefaultColaboradorListFilters());
    pagination.value = createDefaultColaboradorTablePagination();
    await fetchPage();
  }

  async function onTableRequest(
    request: ColaboradorTableRequest
  ): Promise<void> {
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
