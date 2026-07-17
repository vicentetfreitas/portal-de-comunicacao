import { AREA_API_PATHS } from "@/config/organization";
import { BaseApiClient, getHttpClient } from "@/services/http";
import type { PageResponse } from "@/types/api";
import type {
  AreaListParams,
  AreaResponse
} from "@/types/organization/area.types";

function buildListQueryParams(
  params?: AreaListParams
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
  if (params.name !== undefined && params.name.length > 0) {
    query.name = params.name;
  }
  if (params.acronym !== undefined && params.acronym.length > 0) {
    query.acronym = params.acronym;
  }

  return Object.keys(query).length > 0 ? query : undefined;
}

/**
 * Minimal FT-AREA client for equipe select/filters (DS-EQUIPE-FE-01).
 */
export class AreaApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  getById(id: number): Promise<AreaResponse> {
    return this.get<AreaResponse>(`${AREA_API_PATHS.base}/${id}`);
  }

  list(params?: AreaListParams): Promise<PageResponse<AreaResponse>> {
    return this.getPage<AreaResponse>(AREA_API_PATHS.base, {
      params: buildListQueryParams(params)
    });
  }
}

export const areaService = new AreaApiService();
