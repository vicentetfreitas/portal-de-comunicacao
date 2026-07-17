import { describe, expect, it } from "vitest";

import {
  buildEquipeListParams,
  createDefaultEquipeListFilters,
  createDefaultEquipeTablePagination
} from "@/composables/organization/useEquipeList";

describe("buildEquipeListParams", () => {
  it("maps pagination and filters to API query params", () => {
    const params = buildEquipeListParams(
      {
        sortBy: "name",
        descending: true,
        page: 2,
        rowsPerPage: 20,
        rowsNumber: 0
      },
      {
        status: "ACTIVE",
        areaId: 10,
        name: " Alpha "
      }
    );

    expect(params).toEqual({
      page: 1,
      size: 20,
      sort: "name,desc",
      status: "ACTIVE",
      areaId: 10,
      name: "Alpha"
    });
  });

  it("omits empty filters", () => {
    const params = buildEquipeListParams(
      createDefaultEquipeTablePagination(),
      createDefaultEquipeListFilters()
    );

    expect(params).toEqual({
      page: 0,
      size: 10,
      sort: "name,asc"
    });
  });
});
