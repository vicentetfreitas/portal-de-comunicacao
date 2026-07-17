import type { PageRequestParams } from "@/types/api";

export type EquipeStatus = "ACTIVE" | "INACTIVE";

export interface EquipeResponse {
  id: number;
  areaId: number;
  name: string;
  description: string | null;
  leaderId: number | null;
  status: EquipeStatus;
  createdAt: string;
  updatedAt: string | null;
}

export interface CreateEquipeRequest {
  areaId: number;
  name: string;
  description?: string;
  leaderId?: number;
}

export interface UpdateEquipeRequest {
  name: string;
  description?: string;
  leaderId?: number;
}

export interface UpdateEquipeStatusRequest {
  status: EquipeStatus;
}

export interface EquipeListParams extends PageRequestParams {
  status?: EquipeStatus;
  areaId?: number;
  name?: string;
}
