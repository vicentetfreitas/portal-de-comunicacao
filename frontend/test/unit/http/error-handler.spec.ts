import { beforeEach, describe, expect, it, vi } from "vitest";

import {
  dispatchHttpError,
  getDefaultHttpErrorMessage,
  setGlobalHttpErrorHandler
} from "@/services/http/error-handler";
import { ApiError } from "@/types/api";

describe("error-handler", () => {
  beforeEach(() => {
    setGlobalHttpErrorHandler(null);
  });

  it("maps authentication errors to default message", () => {
    const error = new ApiError({
      status: 401,
      code: "UNAUTHORIZED",
      message: "Unauthorized",
      category: "authentication"
    });

    expect(getDefaultHttpErrorMessage(error)).toContain("Sessão expirada");
  });

  it("dispatches normalized errors to the global handler", () => {
    const handler = vi.fn();
    setGlobalHttpErrorHandler(handler);

    const error = new ApiError({
      status: 500,
      code: "INTERNAL_ERROR",
      message: "Server error",
      category: "server"
    });

    const result = dispatchHttpError(error);

    expect(result).toBe(error);
    expect(handler).toHaveBeenCalledWith(error);
  });
});
