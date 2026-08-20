import { FEDERACAO_API_PATHS } from "@/config/organization";
import { BaseApiClient, getHttpClient } from "@/services/http";
import type { PageResponse } from "@/types/api";
import type {
  CreateFederacaoRequest,
  FederacaoListParams,
  FederacaoResponse,
  UpdateFederacaoRequest,
  UpdateFederacaoStatusRequest
} from "@/types/organization/federacao.types";

function buildListQueryParams(
  params?: FederacaoListParams
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
  if (params.name !== undefined && params.name.length > 0) {
    query.name = params.name;
  }
  if (params.acronym !== undefined && params.acronym.length > 0) {
    query.acronym = params.acronym;
  }
  if (params.unimedCode !== undefined) {
    query.unimedCode = params.unimedCode;
  }

  return Object.keys(query).length > 0 ? query : undefined;
}

/**
 * FT-FEDERACAO HTTP service — consumes /api/v1/federacoes via BaseApiClient.
 */
export class FederacaoApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  create(request: CreateFederacaoRequest): Promise<FederacaoResponse> {
    return this.post<FederacaoResponse>(FEDERACAO_API_PATHS.base, request);
  }

  getById(id: number): Promise<FederacaoResponse> {
    return this.get<FederacaoResponse>(`${FEDERACAO_API_PATHS.base}/${id}`);
  }

  list(params?: FederacaoListParams): Promise<PageResponse<FederacaoResponse>> {
    return this.getPage<FederacaoResponse>(FEDERACAO_API_PATHS.base, {
      params: buildListQueryParams(params)
    });
  }

  update(
    id: number,
    request: UpdateFederacaoRequest
  ): Promise<FederacaoResponse> {
    return this.put<FederacaoResponse>(
      `${FEDERACAO_API_PATHS.base}/${id}`,
      request
    );
  }

  updateStatus(
    id: number,
    request: UpdateFederacaoStatusRequest
  ): Promise<FederacaoResponse> {
    return this.patch<FederacaoResponse>(
      `${FEDERACAO_API_PATHS.base}/${id}/status`,
      request
    );
  }
}

export const federacaoService = new FederacaoApiService();
