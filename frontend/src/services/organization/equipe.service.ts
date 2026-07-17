import { EQUIPE_API_PATHS } from "@/config/organization";
import { BaseApiClient, getHttpClient } from "@/services/http";
import type { PageResponse } from "@/types/api";
import type {
  CreateEquipeRequest,
  EquipeListParams,
  EquipeResponse,
  UpdateEquipeRequest,
  UpdateEquipeStatusRequest
} from "@/types/organization/equipe.types";

function buildListQueryParams(
  params?: EquipeListParams
): Record<string, string | number> | undefined {
  if (!params) {
    return undefined;
  }

  const query: Record<string, string | number> = {};

  if (params.page !== undefined) {
    query.page = params.page;
  }
  if (params.size !== undefined) {
    query.size = params.size;
  }
  if (params.sort !== undefined) {
    query.sort = params.sort;
  }
  if (params.status !== undefined) {
    query.status = params.status;
  }
  if (params.areaId !== undefined) {
    query.areaId = params.areaId;
  }
  if (params.name !== undefined && params.name.length > 0) {
    query.name = params.name;
  }

  return Object.keys(query).length > 0 ? query : undefined;
}

/**
 * FT-EQUIPE HTTP service — consumes /api/v1/equipes via BaseApiClient.
 */
export class EquipeApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  create(request: CreateEquipeRequest): Promise<EquipeResponse> {
    return this.post<EquipeResponse>(EQUIPE_API_PATHS.base, request);
  }

  getById(id: number): Promise<EquipeResponse> {
    return this.get<EquipeResponse>(`${EQUIPE_API_PATHS.base}/${id}`);
  }

  list(params?: EquipeListParams): Promise<PageResponse<EquipeResponse>> {
    return this.getPage<EquipeResponse>(EQUIPE_API_PATHS.base, {
      params: buildListQueryParams(params)
    });
  }

  update(id: number, request: UpdateEquipeRequest): Promise<EquipeResponse> {
    return this.put<EquipeResponse>(`${EQUIPE_API_PATHS.base}/${id}`, request);
  }

  updateStatus(
    id: number,
    request: UpdateEquipeStatusRequest
  ): Promise<EquipeResponse> {
    return this.patch<EquipeResponse>(
      `${EQUIPE_API_PATHS.base}/${id}/status`,
      request
    );
  }
}

export const equipeService = new EquipeApiService();
