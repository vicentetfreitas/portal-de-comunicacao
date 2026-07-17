import type { EquipeStatus } from "@/types/organization/equipe.types";

export function resolveTargetEquipeStatus(
  currentStatus: EquipeStatus
): EquipeStatus {
  return currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
}

export function isEquipeDeactivation(currentStatus: EquipeStatus): boolean {
  return currentStatus === "ACTIVE";
}
