/**
 * Success envelope — aligned with backend ApiResponse and FT-AUTH contract.
 */
export interface ApiResponse<T> {
  timestamp: string;
  success: boolean;
  message?: string;
  data?: T;
}

/**
 * Error envelope — aligned with backend ErrorResponse.
 */
export interface ErrorResponseBody {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

export interface FieldValidationError {
  field: string;
  message: string;
}

export interface ValidationErrorResponseBody extends ErrorResponseBody {
  errors: FieldValidationError[];
}

export function isErrorResponseBody(
  value: unknown
): value is ErrorResponseBody {
  if (typeof value !== "object" || value === null) {
    return false;
  }

  const candidate = value as Record<string, unknown>;
  return (
    typeof candidate.timestamp === "string" &&
    typeof candidate.status === "number" &&
    typeof candidate.error === "string" &&
    typeof candidate.message === "string" &&
    typeof candidate.path === "string"
  );
}

export function isValidationErrorResponseBody(
  value: unknown
): value is ValidationErrorResponseBody {
  return (
    isErrorResponseBody(value) &&
    Array.isArray((value as ValidationErrorResponseBody).errors)
  );
}
