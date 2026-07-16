import type { ProblemDetails } from "./problem-details";
import type {
  ErrorResponseBody,
  FieldValidationError,
  ValidationErrorResponseBody
} from "./api-response";
import {
  isErrorResponseBody,
  isValidationErrorResponseBody
} from "./api-response";
import { isProblemDetails } from "./problem-details";

export type ApiErrorCategory =
  | "validation"
  | "authentication"
  | "authorization"
  | "not_found"
  | "conflict"
  | "server"
  | "network"
  | "unknown";

/**
 * Normalized API error for centralized HTTP handling.
 */
export class ApiError extends Error {
  readonly status: number;
  readonly code: string;
  readonly path?: string;
  readonly timestamp?: string;
  readonly category: ApiErrorCategory;
  readonly fieldErrors?: FieldValidationError[];
  readonly problemDetails?: ProblemDetails;

  constructor(options: {
    status: number;
    code: string;
    message: string;
    path?: string;
    timestamp?: string;
    category?: ApiErrorCategory;
    fieldErrors?: FieldValidationError[];
    problemDetails?: ProblemDetails;
  }) {
    super(options.message);
    this.name = "ApiError";
    this.status = options.status;
    this.code = options.code;
    this.category = options.category ?? categorizeStatus(options.status);

    if (options.path !== undefined) {
      this.path = options.path;
    }
    if (options.timestamp !== undefined) {
      this.timestamp = options.timestamp;
    }
    if (options.fieldErrors !== undefined) {
      this.fieldErrors = options.fieldErrors;
    }
    if (options.problemDetails !== undefined) {
      this.problemDetails = options.problemDetails;
    }
  }

  static fromErrorResponse(body: ErrorResponseBody): ApiError {
    return new ApiError({
      status: body.status,
      code: body.error,
      message: body.message,
      path: body.path,
      timestamp: body.timestamp
    });
  }

  static fromValidationErrorResponse(
    body: ValidationErrorResponseBody
  ): ApiError {
    return new ApiError({
      status: body.status,
      code: body.error,
      message: body.message,
      path: body.path,
      timestamp: body.timestamp,
      category: "validation",
      fieldErrors: body.errors
    });
  }

  static fromProblemDetails(details: ProblemDetails): ApiError {
    const status = details.status ?? 0;
    return new ApiError({
      status,
      code: typeof details.type === "string" ? details.type : "PROBLEM_DETAILS",
      message:
        typeof details.detail === "string"
          ? details.detail
          : typeof details.title === "string"
            ? details.title
            : "Unexpected API error",
      category: categorizeStatus(status),
      problemDetails: details
    });
  }

  static fromUnknown(error: unknown): ApiError {
    if (error instanceof ApiError) {
      return error;
    }

    if (error instanceof Error) {
      return new ApiError({
        status: 0,
        code: "UNKNOWN_ERROR",
        message: error.message,
        category: "unknown"
      });
    }

    return new ApiError({
      status: 0,
      code: "UNKNOWN_ERROR",
      message: "Unexpected error",
      category: "unknown"
    });
  }
}

export function categorizeStatus(status: number): ApiErrorCategory {
  if (status === 400 || status === 422) {
    return "validation";
  }
  if (status === 401) {
    return "authentication";
  }
  if (status === 403) {
    return "authorization";
  }
  if (status === 404) {
    return "not_found";
  }
  if (status === 409) {
    return "conflict";
  }
  if (status >= 500) {
    return "server";
  }
  if (status === 0) {
    return "network";
  }
  return "unknown";
}

export function normalizeApiError(error: unknown): ApiError {
  if (error instanceof ApiError) {
    return error;
  }

  if (typeof error === "object" && error !== null && "response" in error) {
    const axiosError = error as {
      response?: { status?: number; data?: unknown };
      message?: string;
    };

    const data = axiosError.response?.data;
    if (isValidationErrorResponseBody(data)) {
      return ApiError.fromValidationErrorResponse(data);
    }
    if (isErrorResponseBody(data)) {
      return ApiError.fromErrorResponse(data);
    }
    if (isProblemDetails(data)) {
      return ApiError.fromProblemDetails(data);
    }

    return new ApiError({
      status: axiosError.response?.status ?? 0,
      code: "HTTP_ERROR",
      message: axiosError.message ?? "HTTP request failed",
      category: categorizeStatus(axiosError.response?.status ?? 0)
    });
  }

  return ApiError.fromUnknown(error);
}
