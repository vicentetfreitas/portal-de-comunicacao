import type { PageRequestParams } from "@/types/api";

export type SingularStatus = "ACTIVE" | "INACTIVE";

export interface SingularResponse {
  id: number;
  federationId: number;
  name: string;
  acronym: string;
  unimedCode: string;
  status: SingularStatus;
  createdAt: string;
  updatedAt: string | null;
}

export interface CreateSingularRequest {
  federationId: number;
  name: string;
  acronym: string;
  unimedCode: string;
}

export interface UpdateSingularRequest {
  name: string;
  acronym: string;
  unimedCode: string;
}

export interface UpdateSingularStatusRequest {
  status: SingularStatus;
}

export interface SingularListParams extends PageRequestParams {
  status?: SingularStatus;
  federationId?: number;
  name?: string;
  acronym?: string;
  unimedCode?: string;
}
