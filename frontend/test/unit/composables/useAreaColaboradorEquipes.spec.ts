import { beforeEach, describe, expect, it, vi } from "vitest";

import { useAreaColaboradorEquipes } from "@/composables/area-colaborador/useAreaColaboradorEquipes";

const { listMock, handleErrorMock, activeContext } = vi.hoisted(() => ({
  listMock: vi.fn(),
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
  equipeService: {
    list: listMock
  }
}));

describe("useAreaColaboradorEquipes", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    activeContext.value = { areaId: 7 };
  });

  it("loads the equipes vinculadas to the active context's area", async () => {
    listMock.mockResolvedValue({
      content: [
        { id: 1, areaId: 7, name: "TI", description: "Tecnologia" },
        { id: 2, areaId: 7, name: "RH", description: null }
      ],
      page: 0,
      size: 10,
      totalElements: 2,
      totalPages: 1,
      first: true,
      last: true
    });

    const page = useAreaColaboradorEquipes();
    await page.loadEquipes();

    expect(listMock).toHaveBeenCalledWith({ areaId: 7 });
    expect(page.equipes.value).toHaveLength(2);
    expect(page.isEmpty.value).toBe(false);
    expect(page.loading.value).toBe(false);
  });

  it("marks isEmpty when the area has no equipes (AT-AREA-COLAB-006)", async () => {
    listMock.mockResolvedValue({
      content: [],
      page: 0,
      size: 10,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    });

    const page = useAreaColaboradorEquipes();
    await page.loadEquipes();

    expect(page.equipes.value).toEqual([]);
    expect(page.isEmpty.value).toBe(true);
  });

  it("shows a generic error (toast) on communication failure, without throwing (AT-AREA-COLAB-007)", async () => {
    listMock.mockRejectedValue(new Error("network error"));

    const page = useAreaColaboradorEquipes();
    await expect(page.loadEquipes()).resolves.toBeUndefined();

    expect(handleErrorMock).toHaveBeenCalled();
    expect(page.equipes.value).toEqual([]);
    expect(page.loading.value).toBe(false);
  });

  it("does not call the service when the active context has no area", async () => {
    activeContext.value = { areaId: null };

    const page = useAreaColaboradorEquipes();
    await page.loadEquipes();

    expect(listMock).not.toHaveBeenCalled();
    expect(page.isEmpty.value).toBe(true);
  });
});
