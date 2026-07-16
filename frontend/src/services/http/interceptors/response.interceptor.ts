import type { AxiosError, AxiosInstance } from "axios";

import { dispatchHttpError } from "@/services/http/error-handler";
import { normalizeApiError } from "@/types/api";

import type { UnauthorizedHandler } from "@/auth/unauthorized-handler";

let unauthorizedHandler: UnauthorizedHandler | null = null;

/**
 * FT-AUTH extension point — register refresh/retry logic for HTTP 401 responses.
 */
export function setUnauthorizedHandler(
  handler: UnauthorizedHandler | null
): void {
  unauthorizedHandler = handler;
}

export async function handleResponseError(
  error: AxiosError,
  client: AxiosInstance
): Promise<unknown> {
  const apiError = normalizeApiError(error);

  if (
    apiError.status === 401 &&
    unauthorizedHandler &&
    error.config &&
    !(error.config as { _retry?: boolean })._retry
  ) {
    const requestUrl = error.config.url;
    const shouldRetry = await unauthorizedHandler(
      apiError,
      requestUrl !== undefined ? { requestUrl } : {}
    );
    if (shouldRetry) {
      (error.config as { _retry?: boolean })._retry = true;
      return client.request(error.config);
    }
  }

  return Promise.reject(dispatchHttpError(apiError));
}
