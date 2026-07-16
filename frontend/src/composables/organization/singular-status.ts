import type { SingularStatus } from "@/types/organization/singular.types";

export function resolveTargetSingularStatus(
  currentStatus: SingularStatus
): SingularStatus {
  return currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
}

export function isSingularDeactivation(currentStatus: SingularStatus): boolean {
  return currentStatus === "ACTIVE";
}
