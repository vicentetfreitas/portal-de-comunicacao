import type { PageRequestParams } from "@/types/api";

export type AreaStatus = "ACTIVE" | "INACTIVE";

export interface AreaResponse {
  id: number;
  singularId: number;
  parentAreaId: number | null;
  name: string;
  acronym: string;
  description: string | null;
  managerId: number | null;
  status: AreaStatus;
  createdAt: string;
  updatedAt: string | null;
}

export interface AreaListParams extends PageRequestParams {
  status?: AreaStatus;
  singularId?: number;
  name?: string;
  acronym?: string;
}
