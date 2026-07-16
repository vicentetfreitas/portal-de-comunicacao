import { beforeEach, describe, expect, it, vi } from "vitest";
import type { AxiosError, AxiosInstance } from "axios";

import { ApiError } from "@/types/api";
import {
  handleResponseError,
  setUnauthorizedHandler
} from "@/services/http/interceptors/response.interceptor";
import { setGlobalHttpErrorHandler } from "@/services/http/error-handler";

describe("handleResponseError", () => {
  const requestMock = vi.fn();
  const client = {
    request: requestMock
  } as unknown as AxiosInstance;

  beforeEach(() => {
    setUnauthorizedHandler(null);
    setGlobalHttpErrorHandler(null);
    requestMock.mockReset();
  });

  it("retries request when unauthorized handler approves retry", async () => {
    const handler = vi.fn(async () => true);
    setUnauthorizedHandler(handler);

    const error = {
      response: {
        status: 401,
        data: {
          timestamp: "2026-01-01T00:00:00Z",
          status: 401,
          error: "UNAUTHORIZED",
          message: "Unauthorized",
          path: "/api/v1/resource"
        }
      },
      config: { headers: {} }
    } as AxiosError;

    requestMock.mockResolvedValue({ data: { success: true } });

    await handleResponseError(error, client);

    expect(handler).toHaveBeenCalled();
    expect(requestMock).toHaveBeenCalled();
  });

  it("rejects with ApiError when retry is not performed", async () => {
    setUnauthorizedHandler(async () => false);
    const globalHandler = vi.fn();
    setGlobalHttpErrorHandler(globalHandler);

    const error = {
      response: {
        status: 500,
        data: {
          timestamp: "2026-01-01T00:00:00Z",
          status: 500,
          error: "INTERNAL_ERROR",
          message: "Server error",
          path: "/api/v1/resource"
        }
      },
      config: { headers: {} }
    } as AxiosError;

    await expect(handleResponseError(error, client)).rejects.toBeInstanceOf(
      ApiError
    );
    expect(globalHandler).toHaveBeenCalled();
  });
});
