import type { PageRequestParams } from "@/types/api";

export type ColaboradorStatus = "ACTIVE" | "INACTIVE";

export interface ColaboradorResponse {
  id: number;
  federationId: number;
  singularId: number | null;
  areaId: number | null;
  teamId: number | null;
  managerId: number | null;
  name: string;
  email: string;
  zimbraId: string;
  biography: string | null;
  status: ColaboradorStatus;
  birthDate: string | null;
  hireDate: string | null;
  lastAccessAt: string | null;
  createdAt: string;
  updatedAt: string | null;
}

export interface CreateColaboradorRequest {
  federationId: number;
  singularId?: number;
  areaId?: number;
  teamId?: number;
  managerId?: number;
  name: string;
  email: string;
  zimbraId: string;
  biography?: string;
  birthDate?: string;
  hireDate?: string;
}

export interface UpdateColaboradorRequest {
  name: string;
  singularId?: number;
  areaId?: number;
  teamId?: number;
  managerId?: number;
  zimbraId: string;
  biography?: string;
  birthDate?: string;
  hireDate?: string;
}

export interface UpdateColaboradorStatusRequest {
  status: ColaboradorStatus;
}

export interface ColaboradorListParams extends PageRequestParams {
  status?: ColaboradorStatus;
  singularId?: number;
  areaId?: number;
  teamId?: number;
  name?: string;
  email?: string;
}
