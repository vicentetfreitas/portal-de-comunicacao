import type { AxiosInstance, AxiosRequestConfig } from "axios";

import type { ApiResponse, PageResponse } from "@/types/api";
import { ApiError } from "@/types/api";

/**
 * Base API client — unwraps ApiResponse envelopes for Feature services.
 */
export class BaseApiClient {
  constructor(protected readonly client: AxiosInstance) {}

  protected unwrapData<T>(payload: ApiResponse<T>): T {
    if (payload.success !== true) {
      throw new ApiError({
        status: 500,
        code: "API_RESPONSE_NOT_SUCCESSFUL",
        message: payload.message ?? "API response indicates failure"
      });
    }

    if (payload.data === undefined) {
      throw new ApiError({
        status: 500,
        code: "API_RESPONSE_MISSING_DATA",
        message: "API response does not contain data payload"
      });
    }

    return payload.data;
  }

  protected unwrapOptionalData<T>(payload: ApiResponse<T>): T | undefined {
    if (payload.success !== true) {
      throw new ApiError({
        status: 500,
        code: "API_RESPONSE_NOT_SUCCESSFUL",
        message: payload.message ?? "API response indicates failure"
      });
    }

    return payload.data;
  }

  protected async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await this.client.get<ApiResponse<T>>(url, config);
    return this.unwrapData(response.data);
  }

  protected async getPage<T>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<PageResponse<T>> {
    return this.get<PageResponse<T>>(url, config);
  }

  protected async post<T>(
    url: string,
    body?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response = await this.client.post<ApiResponse<T>>(url, body, config);
    return this.unwrapData(response.data);
  }

  protected async put<T>(
    url: string,
    body?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response = await this.client.put<ApiResponse<T>>(url, body, config);
    return this.unwrapData(response.data);
  }

  protected async patch<T>(
    url: string,
    body?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    const response = await this.client.patch<ApiResponse<T>>(url, body, config);
    return this.unwrapData(response.data);
  }

  protected async delete(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<void> {
    const response = await this.client.delete<ApiResponse<void>>(url, config);
    this.unwrapOptionalData(response.data);
  }

  /**
   * GET returning a raw binary body (not an ApiResponse envelope) — e.g. file downloads.
   * Reads the filename from Content-Disposition when present (FT-DOCUMENTO).
   */
  protected async getBlob(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<{ blob: Blob; filename: string | null }> {
    const response = await this.client.get<Blob>(url, {
      ...config,
      responseType: "blob"
    });

    const disposition = response.headers["content-disposition"] as
      | string
      | undefined;
    const filename = extractFilename(disposition);

    return { blob: response.data, filename };
  }
}

function extractFilename(disposition: string | undefined): string | null {
  if (!disposition) {
    return null;
  }
  const match = /filename="?([^";]+)"?/i.exec(disposition);
  return match?.[1] ?? null;
}
