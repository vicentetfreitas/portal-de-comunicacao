import { beforeEach, describe, expect, it, vi } from "vitest";

import { ApiError } from "@/types/api";
import { useAreaColaboradorDetail } from "@/composables/area-colaborador/useAreaColaboradorDetail";

const { getByIdMock, handleErrorMock, activeContext } = vi.hoisted(() => ({
  getByIdMock: vi.fn(),
  handleErrorMock: vi.fn(),
  activeContext: { value: { areaId: 7 } as { areaId: number | null } | null }
}));

vi.mock("@/composables/useSession", () => ({
  useSession: () => ({
    activeContext
  })
}));

vi.mock("@/composables/useStandardErrorHandling", () => ({
  useStandardErrorHandling: () => ({
    handleError: handleErrorMock
  })
}));

vi.mock("@/services/organization", () => ({
  areaService: {
    getById: getByIdMock
  }
}));

describe("useAreaColaboradorDetail", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    activeContext.value = { areaId: 7 };
    handleErrorMock.mockImplementation((error: unknown) =>
      error instanceof ApiError
        ? error
        : new ApiError({
            status: 0,
            code: "unknown",
            message: "erro",
            category: "unknown"
          })
    );
  });

  it("loads the active context's area", async () => {
    getByIdMock.mockResolvedValue({
      id: 7,
      name: "TI",
      description: "Tecnologia da Informação"
    });

    const page = useAreaColaboradorDetail();
    await page.loadArea();

    expect(getByIdMock).toHaveBeenCalledWith(7);
    expect(page.area.value?.name).toBe("TI");
    expect(page.loading.value).toBe(false);
    expect(page.notFound.value).toBe(false);
  });

  it("sets notFound without a toast on HTTP 404", async () => {
    getByIdMock.mockRejectedValue(
      new ApiError({
        status: 404,
        code: "not_found",
        message: "not found",
        category: "not_found"
      })
    );

    const page = useAreaColaboradorDetail();
    await page.loadArea();

    expect(page.notFound.value).toBe(true);
    expect(page.area.value).toBeNull();
    expect(handleErrorMock).toHaveBeenCalledWith(expect.anything(), {
      silent: true
    });
  });

  it("shows a generic error (toast) on communication failure, without blocking navigation", async () => {
    getByIdMock.mockRejectedValue(
      new ApiError({
        status: 503,
        code: "unavailable",
        message: "unavailable",
        category: "unknown"
      })
    );

    const page = useAreaColaboradorDetail();
    await page.loadArea();

    expect(page.notFound.value).toBe(false);
    expect(page.area.value).toBeNull();
    expect(page.loading.value).toBe(false);
    expect(handleErrorMock).toHaveBeenCalledWith(expect.anything());
  });

  it("treats a missing areaId in the active context as not found", async () => {
    activeContext.value = { areaId: null };

    const page = useAreaColaboradorDetail();
    await page.loadArea();

    expect(getByIdMock).not.toHaveBeenCalled();
    expect(page.notFound.value).toBe(true);
  });
});
