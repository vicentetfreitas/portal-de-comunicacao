import { beforeEach, describe, expect, it, vi } from "vitest";

import { EQUIPE_API_PATHS } from "@/config/organization";
import {
  EquipeApiService,
  equipeService
} from "@/services/organization/equipe.service";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { EquipeResponse } from "@/types/organization/equipe.types";

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

const sampleEquipe: EquipeResponse = {
  id: 1,
  areaId: 10,
  name: "Equipe Alpha",
  description: null,
  leaderId: null,
  status: "ACTIVE",
  createdAt: "2026-07-17T12:00:00Z",
  updatedAt: null
};

function successPayload<T>(data: T): ApiResponse<T> {
  return {
    timestamp: "2026-07-17T12:00:00Z",
    success: true,
    data
  };
}

describe("EquipeApiService", () => {
  let service: EquipeApiService;

  beforeEach(() => {
    vi.clearAllMocks();
    service = new EquipeApiService();
  });

  it("extends BaseApiClient via exported singleton", () => {
    expect(equipeService).toBeInstanceOf(EquipeApiService);
  });

  it("creates equipe via POST /equipes", async () => {
    postMock.mockResolvedValue({ data: successPayload(sampleEquipe) });

    const result = await service.create({
      areaId: 10,
      name: "Equipe Alpha"
    });

    expect(postMock).toHaveBeenCalledWith(
      EQUIPE_API_PATHS.base,
      { areaId: 10, name: "Equipe Alpha" },
      undefined
    );
    expect(result).toEqual(sampleEquipe);
  });

  it("lists equipes with query params", async () => {
    const page: PageResponse<EquipeResponse> = {
      content: [sampleEquipe],
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
      areaId: 10,
      page: 0,
      size: 10
    });

    expect(getMock).toHaveBeenCalledWith(EQUIPE_API_PATHS.base, {
      params: {
        status: "ACTIVE",
        areaId: 10,
        page: 0,
        size: 10
      }
    });
    expect(result.content).toHaveLength(1);
  });

  it("updates status via PATCH", async () => {
    patchMock.mockResolvedValue({
      data: successPayload({ ...sampleEquipe, status: "INACTIVE" })
    });

    const result = await service.updateStatus(1, { status: "INACTIVE" });

    expect(patchMock).toHaveBeenCalledWith(
      `${EQUIPE_API_PATHS.base}/1/status`,
      { status: "INACTIVE" },
      undefined
    );
    expect(result.status).toBe("INACTIVE");
  });
});
