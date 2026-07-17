import { beforeEach, describe, expect, it, vi } from "vitest";

import {
  buildSingularListParams,
  createDefaultSingularListFilters,
  createDefaultSingularTablePagination
} from "@/composables/organization/useSingularList";

const listMock = vi.fn();

vi.mock("@/services/organization", () => ({
  singularService: {
    list: (...args: unknown[]) => listMock(...args)
  }
}));

vi.mock("@/composables/useStandardErrorHandling", () => ({
  useStandardErrorHandling: () => ({
    withErrorHandling: async <T>(operation: () => Promise<T>) => operation()
  })
}));

describe("buildSingularListParams", () => {
  it("maps pagination and filters to API query params", () => {
    const params = buildSingularListParams(
      {
        sortBy: "name",
        descending: false,
        page: 2,
        rowsPerPage: 20,
        rowsNumber: 0
      },
      {
        status: "ACTIVE",
        federationId: 1,
        name: " Unimed ",
        acronym: " UNI ",
        unimedCode: " UC001 "
      }
    );

    expect(params).toEqual({
      page: 1,
      size: 20,
      sort: "name,asc",
      status: "ACTIVE",
      federationId: 1,
      name: "Unimed",
      acronym: "UNI",
      unimedCode: "UC001"
    });
  });

  it("omits empty filters", () => {
    const params = buildSingularListParams(
      createDefaultSingularTablePagination(),
      createDefaultSingularListFilters()
    );

    expect(params).toEqual({
      page: 0,
      size: 10,
      sort: "name,asc"
    });
  });
});

describe("useSingularList integration", () => {
  beforeEach(() => {
    listMock.mockReset();
    listMock.mockResolvedValue({
      content: [],
      page: 0,
      size: 10,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    });
  });

  it("loads page data from singularService.list", async () => {
    const { useSingularList } =
      await import("@/composables/organization/useSingularList");
    const { fetchPage, rows, pagination } = useSingularList();

    await fetchPage();

    expect(listMock).toHaveBeenCalledWith({
      page: 0,
      size: 10,
      sort: "name,asc"
    });
    expect(rows.value).toEqual([]);
    expect(pagination.value.rowsNumber).toBe(0);
    expect(listMock).toHaveBeenCalledOnce();
  });
});
