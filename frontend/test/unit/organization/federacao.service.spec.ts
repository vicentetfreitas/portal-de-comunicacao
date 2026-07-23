import { beforeEach, describe, expect, it, vi } from "vitest";

import { FEDERACAO_API_PATHS } from "@/config/organization";
import {
  FederacaoApiService,
  federacaoService
} from "@/services/organization/federacao.service";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { FederacaoResponse } from "@/types/organization/federacao.types";

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

const sampleFederacao: FederacaoResponse = {
  id: 1,
  name: "Unimed Federação",
  acronym: "UNMFED",
  unimedCode: 979,
  ansRegistration: "32195-8",
  websiteUrl: null,
  description: "Federação administradora.",
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

describe("FederacaoApiService", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("lists federacoes", async () => {
    const page: PageResponse<FederacaoResponse> = {
      content: [sampleFederacao],
      page: 0,
      size: 20,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    };
    getMock.mockResolvedValue({ data: successPayload(page) });

    const service = new FederacaoApiService();
    const result = await service.list({ status: "ACTIVE" });

    expect(getMock).toHaveBeenCalledWith(FEDERACAO_API_PATHS.base, {
      params: { status: "ACTIVE" }
    });
    expect(result.content[0].unimedCode).toBe(979);
  });

  it("creates federacao", async () => {
    postMock.mockResolvedValue({ data: successPayload(sampleFederacao) });

    await federacaoService.create({
      name: "Unimed Federação",
      acronym: "UNMFED",
      unimedCode: 979,
      ansRegistration: "32195-8"
    });

    expect(postMock).toHaveBeenCalledWith(FEDERACAO_API_PATHS.base, {
      name: "Unimed Federação",
      acronym: "UNMFED",
      unimedCode: 979,
      ansRegistration: "32195-8"
    });
  });
});
