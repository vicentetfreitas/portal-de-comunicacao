import { beforeEach, describe, expect, it, vi } from "vitest";

import { dsNotifyError } from "@/components/ds";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ApiError } from "@/types/api";

vi.mock("@/components/ds", async () => {
  const actual =
    await vi.importActual<typeof import("@/components/ds")>("@/components/ds");
  return {
    ...actual,
    dsNotifyError: vi.fn()
  };
});

describe("useStandardErrorHandling", () => {
  beforeEach(() => {
    vi.mocked(dsNotifyError).mockClear();
  });

  it("shows toast for server errors", () => {
    const { handleError } = useStandardErrorHandling();
    const error = new ApiError({
      status: 500,
      code: "INTERNAL_ERROR",
      message: "Server error",
      category: "server"
    });

    handleError(error);

    expect(dsNotifyError).toHaveBeenCalled();
  });

  it("does not show toast for authentication errors", () => {
    const { handleError } = useStandardErrorHandling();
    const error = new ApiError({
      status: 401,
      code: "UNAUTHORIZED",
      message: "Unauthorized",
      category: "authentication"
    });

    handleError(error);

    expect(dsNotifyError).not.toHaveBeenCalled();
  });

  it("returns undefined when wrapped operation fails", async () => {
    const { withErrorHandling } = useStandardErrorHandling();

    const result = await withErrorHandling(async () => {
      throw new ApiError({
        status: 404,
        code: "NOT_FOUND",
        message: "Not found",
        category: "not_found"
      });
    });

    expect(result).toBeUndefined();
    expect(dsNotifyError).toHaveBeenCalled();
  });
});
