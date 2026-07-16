import { describe, expect, it } from "vitest";
import type { AxiosInstance } from "axios";

import { BaseApiClient } from "@/services/http/base-api-client";
import type { ApiResponse } from "@/types/api";
import { ApiError } from "@/types/api";

class TestApiClient extends BaseApiClient {
  unwrap<T>(payload: ApiResponse<T>): T {
    return this.unwrapData(payload);
  }
}

describe("BaseApiClient", () => {
  const client = new TestApiClient({} as AxiosInstance);

  it("unwraps successful ApiResponse payloads", () => {
    const payload: ApiResponse<{ id: number }> = {
      timestamp: "2026-01-01T00:00:00Z",
      success: true,
      data: { id: 42 }
    };

    expect(client.unwrap(payload)).toEqual({ id: 42 });
  });

  it("throws when ApiResponse is not successful", () => {
    const payload: ApiResponse<{ id: number }> = {
      timestamp: "2026-01-01T00:00:00Z",
      success: false,
      message: "Failure"
    };

    expect(() => client.unwrap(payload)).toThrow(ApiError);
  });
});
