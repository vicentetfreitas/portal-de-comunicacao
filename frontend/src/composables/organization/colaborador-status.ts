import type { ColaboradorStatus } from "@/types/organization/colaborador.types";

export function resolveTargetColaboradorStatus(
  currentStatus: ColaboradorStatus
): ColaboradorStatus {
  return currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
}

export function isColaboradorDeactivation(
  currentStatus: ColaboradorStatus
): boolean {
  return currentStatus === "ACTIVE";
}
