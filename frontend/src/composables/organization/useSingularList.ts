import { reactive, ref, type Ref, type UnwrapNestedRefs } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { singularService } from "@/services/organization";
import type {
  SingularListParams,
  SingularResponse,
  SingularStatus
} from "@/types/organization/singular.types";

export interface SingularListFilters {
  status: SingularStatus | null;
  federationId: number | null;
  name: string;
  acronym: string;
  unimedCode: string;
}

export interface SingularTablePagination {
  sortBy: string;
  descending: boolean;
  page: number;
  rowsPerPage: number;
  rowsNumber: number;
}

export interface SingularTableRequest {
  pagination: SingularTablePagination;
}

export function createDefaultSingularListFilters(): SingularListFilters {
  return {
    status: null,
    federationId: null,
    name: "",
    acronym: "",
    unimedCode: ""
  };
}

export function createDefaultSingularTablePagination(): SingularTablePagination {
  return {
    sortBy: "name",
    descending: false,
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0
  };
}

export function buildSingularListParams(
  pagination: SingularTablePagination,
  filters: SingularListFilters
): SingularListParams {
  const params: SingularListParams = {
    page: Math.max(pagination.page - 1, 0),
    size: pagination.rowsPerPage
  };

  if (pagination.sortBy) {
    params.sort = `${pagination.sortBy},${pagination.descending ? "desc" : "asc"}`;
  }

  if (filters.status) {
    params.status = filters.status;
  }
  if (filters.federationId !== null) {
    params.federationId = filters.federationId;
  }

  const name = filters.name.trim();
  if (name.length > 0) {
    params.name = name;
  }

  const acronym = filters.acronym.trim();
  if (acronym.length > 0) {
    params.acronym = acronym;
  }

  const unimedCode = filters.unimedCode.trim();
  if (unimedCode.length > 0) {
    params.unimedCode = unimedCode;
  }

  return params;
}

export function useSingularList() {
  const rows = ref<SingularResponse[]>([]);
  const loading = ref(false);
  const filters: UnwrapNestedRefs<SingularListFilters> = reactive(
    createDefaultSingularListFilters()
  );
  const pagination = ref<SingularTablePagination>(
    createDefaultSingularTablePagination()
  );
  const { withErrorHandling } = useStandardErrorHandling();

  async function fetchPage(): Promise<void> {
    loading.value = true;

    const result = await withErrorHandling(() =>
      singularService.list(buildSingularListParams(pagination.value, filters))
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
    Object.assign(filters, createDefaultSingularListFilters());
    pagination.value = createDefaultSingularTablePagination();
    await fetchPage();
  }

  async function onTableRequest(request: SingularTableRequest): Promise<void> {
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

export type UseSingularListReturn = {
  rows: Ref<SingularResponse[]>;
  loading: Ref<boolean>;
  filters: UnwrapNestedRefs<SingularListFilters>;
  pagination: Ref<SingularTablePagination>;
  fetchPage: () => Promise<void>;
  applyFilters: () => Promise<void>;
  resetFilters: () => Promise<void>;
  onTableRequest: (request: SingularTableRequest) => Promise<void>;
};
