import { httpConfig } from "@/config/http";

export function createCorrelationId(): string {
  if (
    typeof crypto !== "undefined" &&
    typeof crypto.randomUUID === "function"
  ) {
    return crypto.randomUUID();
  }

  return `corr-${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

export function getCorrelationHeaderName(): string {
  return httpConfig.correlationHeaderName;
}
