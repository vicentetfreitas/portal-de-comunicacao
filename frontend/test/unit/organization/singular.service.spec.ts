import { beforeEach, describe, expect, it, vi } from "vitest";

import { SINGULAR_API_PATHS } from "@/config/organization";
import {
  SingularApiService,
  singularService
} from "@/services/organization/singular.service";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { SingularResponse } from "@/types/organization/singular.types";

const { getMock, postMock, putMock, patchMock } = vi.hoisted(() => ({
  getMock: vi.fn(),
  postMock: vi.fn(),
  putMock: vi.fn(),
  patchMock: vi.fn()
}));

vi.mock("@/services/http", async importOriginal => {
  const actual = await importOriginal<typeof import("@/services/http")>();
  return {
    ...actual,
    getHttpClient: () => ({
      get: getMock,
      post: postMock,
      put: putMock,
      patch: patchMock,
      delete: vi.fn()
    })
  };
});

const sampleSingular: SingularResponse = {
  id: 1,
  federationId: 1,
  name: "Unimed Ceará",
  acronym: "UNI-CE",
  unimedCode: "UC001",
  status: "ACTIVE",
  createdAt: "2026-07-16T12:00:00Z",
  updatedAt: null
};

function successPayload<T>(data: T): ApiResponse<T> {
  return {
    timestamp: "2026-07-16T12:00:00Z",
    success: true,
    data
  };
}

describe("SingularApiService", () => {
  let service: SingularApiService;

  beforeEach(() => {
    vi.clearAllMocks();
    service = new SingularApiService();
  });

  it("extends BaseApiClient via exported singleton", () => {
    expect(singularService).toBeInstanceOf(SingularApiService);
  });

  it("creates singular via POST /singulares", async () => {
    postMock.mockResolvedValue({ data: successPayload(sampleSingular) });

    const result = await service.create({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001"
    });

    expect(postMock).toHaveBeenCalledWith(
      SINGULAR_API_PATHS.base,
      {
        federationId: 1,
        name: "Unimed Ceará",
        acronym: "UNI-CE",
        unimedCode: "UC001"
      },
      undefined
    );
    expect(result).toEqual(sampleSingular);
  });

  it("fetches singular by id", async () => {
    getMock.mockResolvedValue({ data: successPayload(sampleSingular) });

    const result = await service.getById(1);

    expect(getMock).toHaveBeenCalledWith(
      `${SINGULAR_API_PATHS.base}/1`,
      undefined
    );
    expect(result.id).toBe(1);
  });

  it("lists singulares with query params", async () => {
    const page: PageResponse<SingularResponse> = {
      content: [sampleSingular],
      page: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    };
    getMock.mockResolvedValue({ data: successPayload(page) });

    const result = await service.list({
      status: "ACTIVE",
      page: 0,
      size: 10,
      sort: "name,asc"
    });

    expect(getMock).toHaveBeenCalledWith(SINGULAR_API_PATHS.base, {
      params: {
        status: "ACTIVE",
        page: 0,
        size: 10,
        sort: "name,asc"
      }
    });
    expect(result.content).toHaveLength(1);
  });

  it("updates singular via PUT", async () => {
    putMock.mockResolvedValue({ data: successPayload(sampleSingular) });

    await service.update(1, {
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001"
    });

    expect(putMock).toHaveBeenCalledWith(
      `${SINGULAR_API_PATHS.base}/1`,
      {
        name: "Unimed Ceará",
        acronym: "UNI-CE",
        unimedCode: "UC001"
      },
      undefined
    );
  });

  it("updates status via PATCH", async () => {
    patchMock.mockResolvedValue({
      data: successPayload({ ...sampleSingular, status: "INACTIVE" })
    });

    const result = await service.updateStatus(1, { status: "INACTIVE" });

    expect(patchMock).toHaveBeenCalledWith(
      `${SINGULAR_API_PATHS.base}/1/status`,
      { status: "INACTIVE" },
      undefined
    );
    expect(result.status).toBe("INACTIVE");
  });
});
