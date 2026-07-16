import { dsNotifyError } from "@/components/ds";
import {
  getDefaultHttpErrorMessage,
  setGlobalHttpErrorHandler
} from "@/services/http";
import { ApiError, normalizeApiError } from "@/types/api";

export interface StandardErrorHandlingOptions {
  silent?: boolean;
  fallbackMessage?: string;
}

function shouldShowToast(
  error: ApiError,
  options?: StandardErrorHandlingOptions
): boolean {
  if (options?.silent) {
    return false;
  }

  return error.category !== "authentication";
}

/**
 * Registers the global HTTP error toast handler (PKG-FE-S0-08).
 */
export function registerGlobalHttpErrorHandler(): void {
  setGlobalHttpErrorHandler(error => {
    if (!shouldShowToast(error)) {
      return;
    }

    dsNotifyError(getDefaultHttpErrorMessage(error));
  });
}

export function useStandardErrorHandling() {
  function handleError(
    error: unknown,
    options?: StandardErrorHandlingOptions
  ): ApiError {
    const apiError = normalizeApiError(error);

    if (shouldShowToast(apiError, options)) {
      const message =
        options?.fallbackMessage ?? getDefaultHttpErrorMessage(apiError);
      dsNotifyError(message);
    }

    return apiError;
  }

  async function withErrorHandling<T>(
    operation: () => Promise<T>,
    options?: StandardErrorHandlingOptions
  ): Promise<T | undefined> {
    try {
      return await operation();
    } catch (error) {
      handleError(error, options);
      return undefined;
    }
  }

  return {
    handleError,
    withErrorHandling
  };
}
