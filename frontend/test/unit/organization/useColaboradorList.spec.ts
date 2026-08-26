import { describe, expect, it } from "vitest";

import {
  buildColaboradorListParams,
  createDefaultColaboradorListFilters,
  createDefaultColaboradorTablePagination
} from "@/composables/organization/useColaboradorList";

describe("buildColaboradorListParams", () => {
  it("maps pagination and filters to API query params", () => {
    const params = buildColaboradorListParams(
      {
        sortBy: "name",
        descending: true,
        page: 2,
        rowsPerPage: 20,
        rowsNumber: 0
      },
      {
        status: "ACTIVE",
        singularId: 5,
        areaId: 10,
        teamId: 20,
        name: " Maria ",
        email: " maria@unimedceara.com.br "
      }
    );

    expect(params).toEqual({
      page: 1,
      size: 20,
      sort: "name,desc",
      status: "ACTIVE",
      singularId: 5,
      areaId: 10,
      teamId: 20,
      name: "Maria",
      email: "maria@unimedceara.com.br"
    });
  });

  it("omits empty filters", () => {
    const params = buildColaboradorListParams(
      createDefaultColaboradorTablePagination(),
      createDefaultColaboradorListFilters()
    );

    expect(params).toEqual({
      page: 0,
      size: 10,
      sort: "name,asc"
    });
  });
});
