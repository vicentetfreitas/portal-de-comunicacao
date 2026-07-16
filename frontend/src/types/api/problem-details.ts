/**
 * RFC 7807 Problem Details — extension point for APIs that return application/problem+json.
 */
export interface ProblemDetails {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  [key: string]: unknown;
}

export function isProblemDetails(value: unknown): value is ProblemDetails {
  if (typeof value !== "object" || value === null) {
    return false;
  }

  const candidate = value as Record<string, unknown>;
  return (
    typeof candidate.title === "string" ||
    typeof candidate.detail === "string" ||
    typeof candidate.status === "number"
  );
}
