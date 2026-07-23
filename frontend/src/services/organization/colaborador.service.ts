import { COLABORADOR_API_PATHS } from "@/config/organization";
import { BaseApiClient, getHttpClient } from "@/services/http";
import type { PageResponse } from "@/types/api";
import type {
  ColaboradorListParams,
  ColaboradorResponse,
  CreateColaboradorRequest,
  UpdateColaboradorRequest,
  UpdateColaboradorStatusRequest
} from "@/types/organization/colaborador.types";

function buildListQueryParams(
  params?: ColaboradorListParams
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
  if (params.singularId !== undefined) {
    query.singularId = params.singularId;
  }
  if (params.areaId !== undefined) {
    query.areaId = params.areaId;
  }
  if (params.teamId !== undefined) {
    query.teamId = params.teamId;
  }
  if (params.name !== undefined && params.name.length > 0) {
    query.name = params.name;
  }
  if (params.email !== undefined && params.email.length > 0) {
    query.email = params.email;
  }

  return Object.keys(query).length > 0 ? query : undefined;
}

/**
 * FT-COLABORADOR HTTP service — consumes /api/v1/colaboradores via BaseApiClient.
 */
export class ColaboradorApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  create(request: CreateColaboradorRequest): Promise<ColaboradorResponse> {
    return this.post<ColaboradorResponse>(COLABORADOR_API_PATHS.base, request);
  }

  getById(id: number): Promise<ColaboradorResponse> {
    return this.get<ColaboradorResponse>(`${COLABORADOR_API_PATHS.base}/${id}`);
  }

  list(
    params?: ColaboradorListParams
  ): Promise<PageResponse<ColaboradorResponse>> {
    return this.getPage<ColaboradorResponse>(COLABORADOR_API_PATHS.base, {
      params: buildListQueryParams(params)
    });
  }

  update(
    id: number,
    request: UpdateColaboradorRequest
  ): Promise<ColaboradorResponse> {
    return this.put<ColaboradorResponse>(
      `${COLABORADOR_API_PATHS.base}/${id}`,
      request
    );
  }

  updateStatus(
    id: number,
    request: UpdateColaboradorStatusRequest
  ): Promise<ColaboradorResponse> {
    return this.patch<ColaboradorResponse>(
      `${COLABORADOR_API_PATHS.base}/${id}/status`,
      request
    );
  }
}

export const colaboradorService = new ColaboradorApiService();
