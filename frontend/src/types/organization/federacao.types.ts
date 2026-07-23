import type { PageRequestParams } from "@/types/api";

export type FederacaoStatus = "ACTIVE" | "INACTIVE";

export interface FederacaoResponse {
  id: number;
  name: string;
  acronym: string;
  unimedCode: number;
  ansRegistration: string;
  websiteUrl: string | null;
  description: string | null;
  status: FederacaoStatus;
  createdAt: string;
  updatedAt: string | null;
}

export interface CreateFederacaoRequest {
  name: string;
  acronym: string;
  unimedCode: number;
  ansRegistration: string;
  websiteUrl?: string;
  description?: string;
}

export interface UpdateFederacaoRequest {
  name: string;
  acronym: string;
  unimedCode: number;
  ansRegistration: string;
  websiteUrl?: string;
  description?: string;
}

export interface UpdateFederacaoStatusRequest {
  status: FederacaoStatus;
}

export interface FederacaoListParams extends PageRequestParams {
  status?: FederacaoStatus;
  name?: string;
  acronym?: string;
  unimedCode?: number;
}
