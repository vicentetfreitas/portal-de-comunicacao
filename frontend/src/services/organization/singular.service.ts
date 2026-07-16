import { SINGULAR_API_PATHS } from "@/config/organization";
import { BaseApiClient, getHttpClient } from "@/services/http";
import type { PageResponse } from "@/types/api";
import type {
  CreateSingularRequest,
  SingularListParams,
  SingularResponse,
  UpdateSingularRequest,
  UpdateSingularStatusRequest
} from "@/types/organization/singular.types";

function buildListQueryParams(
  params?: SingularListParams
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
  if (params.federationId !== undefined) {
    query.federationId = params.federationId;
  }
  if (params.name !== undefined && params.name.length > 0) {
    query.name = params.name;
  }
  if (params.acronym !== undefined && params.acronym.length > 0) {
    query.acronym = params.acronym;
  }
  if (params.unimedCode !== undefined && params.unimedCode.length > 0) {
    query.unimedCode = params.unimedCode;
  }

  return Object.keys(query).length > 0 ? query : undefined;
}

/**
 * FT-SINGULAR HTTP service — consumes /api/v1/singulares via BaseApiClient.
 */
export class SingularApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  create(request: CreateSingularRequest): Promise<SingularResponse> {
    return this.post<SingularResponse>(SINGULAR_API_PATHS.base, request);
  }

  getById(id: number): Promise<SingularResponse> {
    return this.get<SingularResponse>(`${SINGULAR_API_PATHS.base}/${id}`);
  }

  list(params?: SingularListParams): Promise<PageResponse<SingularResponse>> {
    return this.getPage<SingularResponse>(SINGULAR_API_PATHS.base, {
      params: buildListQueryParams(params)
    });
  }

  update(id: number, request: UpdateSingularRequest): Promise<SingularResponse> {
    return this.put<SingularResponse>(`${SINGULAR_API_PATHS.base}/${id}`, request);
  }

  updateStatus(
    id: number,
    request: UpdateSingularStatusRequest
  ): Promise<SingularResponse> {
    return this.patch<SingularResponse>(
      `${SINGULAR_API_PATHS.base}/${id}/status`,
      request
    );
  }
}

export const singularService = new SingularApiService();
