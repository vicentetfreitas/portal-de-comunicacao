import { beforeEach, describe, expect, it, vi } from "vitest";

import { COLABORADOR_API_PATHS } from "@/config/organization";
import {
  ColaboradorApiService,
  colaboradorService
} from "@/services/organization/colaborador.service";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { ColaboradorResponse } from "@/types/organization/colaborador.types";

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

const sampleColaborador: ColaboradorResponse = {
  id: 1,
  federationId: 1,
  singularId: 1,
  areaId: 10,
  teamId: 20,
  managerId: null,
  name: "Maria Silva",
  email: "maria@unimedceara.com.br",
  zimbraId: "zimbra-maria",
  biography: null,
  status: "ACTIVE",
  birthDate: null,
  hireDate: null,
  lastAccessAt: null,
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

describe("ColaboradorApiService", () => {
  let service: ColaboradorApiService;

  beforeEach(() => {
    vi.clearAllMocks();
    service = new ColaboradorApiService();
  });

  it("extends BaseApiClient via exported singleton", () => {
    expect(colaboradorService).toBeInstanceOf(ColaboradorApiService);
  });

  it("creates colaborador via POST /colaboradores", async () => {
    postMock.mockResolvedValue({ data: successPayload(sampleColaborador) });

    const result = await service.create({
      federationId: 1,
      name: "Maria Silva",
      email: "maria@unimedceara.com.br"
    });

    expect(postMock).toHaveBeenCalledWith(
      COLABORADOR_API_PATHS.base,
      {
        federationId: 1,
        name: "Maria Silva",
        email: "maria@unimedceara.com.br"
      },
      undefined
    );
    expect(result).toEqual(sampleColaborador);
  });

  it("lists colaboradores with query params", async () => {
    const page: PageResponse<ColaboradorResponse> = {
      content: [sampleColaborador],
      page: 0,
      size: 20,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    };
    getMock.mockResolvedValue({ data: successPayload(page) });

    await service.list({
      status: "ACTIVE",
      teamId: 20,
      page: 0,
      size: 20
    });

    expect(getMock).toHaveBeenCalledWith(COLABORADOR_API_PATHS.base, {
      params: {
        status: "ACTIVE",
        teamId: 20,
        page: 0,
        size: 20
      }
    });
  });

  it("updates status via PATCH", async () => {
    patchMock.mockResolvedValue({
      data: successPayload({ ...sampleColaborador, status: "INACTIVE" })
    });

    await service.updateStatus(1, { status: "INACTIVE" });

    expect(patchMock).toHaveBeenCalledWith(
      `${COLABORADOR_API_PATHS.base}/1/status`,
      { status: "INACTIVE" },
      undefined
    );
  });
});
