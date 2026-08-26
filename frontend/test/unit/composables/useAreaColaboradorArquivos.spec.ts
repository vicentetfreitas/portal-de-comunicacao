import { beforeEach, describe, expect, it, vi } from "vitest";

import { useAreaColaboradorArquivos } from "@/composables/documento/useAreaColaboradorArquivos";

const { listMock, downloadMock, handleErrorMock, downloadBlobMock } =
  vi.hoisted(() => ({
    listMock: vi.fn(),
    downloadMock: vi.fn(),
    handleErrorMock: vi.fn(),
    downloadBlobMock: vi.fn()
  }));

vi.mock("@/composables/useStandardErrorHandling", () => ({
  useStandardErrorHandling: () => ({
    handleError: handleErrorMock
  })
}));

vi.mock("@/services/documento", () => ({
  pastaService: {
    list: listMock
  },
  documentoService: {
    download: downloadMock
  }
}));

vi.mock("@/utils/downloadBlob", () => ({
  downloadBlob: downloadBlobMock
}));

describe("useAreaColaboradorArquivos", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("loads pastas visíveis ao Contexto Ativo (AT-DOCUMENTO-001)", async () => {
    listMock.mockResolvedValue({
      content: [
        {
          id: 1,
          nome: "Pasta A",
          documentos: [
            { id: 10, nome: "arquivo.pdf", formato: "pdf", tamanhoBytes: 100 }
          ]
        }
      ],
      page: 0,
      size: 20,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    });

    const page = useAreaColaboradorArquivos();
    await page.loadPastas();

    expect(listMock).toHaveBeenCalledWith();
    expect(page.pastas.value).toHaveLength(1);
    expect(page.isEmpty.value).toBe(false);
    expect(page.loading.value).toBe(false);
  });

  it("marca isEmpty quando não há pastas com permissão (AT-DOCUMENTO-001)", async () => {
    listMock.mockResolvedValue({
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    });

    const page = useAreaColaboradorArquivos();
    await page.loadPastas();

    expect(page.pastas.value).toEqual([]);
    expect(page.isEmpty.value).toBe(true);
  });

  it("mostra erro (toast) na falha de comunicação, sem lançar exceção", async () => {
    listMock.mockRejectedValue(new Error("network error"));

    const page = useAreaColaboradorArquivos();
    await expect(page.loadPastas()).resolves.toBeUndefined();

    expect(handleErrorMock).toHaveBeenCalled();
    expect(page.pastas.value).toEqual([]);
  });

  it("baixa um documento acionando o utilitário de download (AT-DOCUMENTO-002)", async () => {
    const blob = new Blob(["conteudo"]);
    downloadMock.mockResolvedValue({ blob, filename: "relatorio.pdf" });

    const page = useAreaColaboradorArquivos();
    await page.baixarDocumento({
      id: 10,
      nome: "arquivo.pdf",
      formato: "pdf",
      tamanhoBytes: 100
    });

    expect(downloadMock).toHaveBeenCalledWith(10);
    expect(downloadBlobMock).toHaveBeenCalledWith(blob, "relatorio.pdf");
    expect(page.downloadingId.value).toBeNull();
  });

  it("trata 403 (sem permissão) no download sem lançar exceção (AT-DOCUMENTO-003)", async () => {
    downloadMock.mockRejectedValue(new Error("forbidden"));

    const page = useAreaColaboradorArquivos();
    await expect(
      page.baixarDocumento({
        id: 10,
        nome: "arquivo.pdf",
        formato: "pdf",
        tamanhoBytes: 100
      })
    ).resolves.toBeUndefined();

    expect(handleErrorMock).toHaveBeenCalled();
    expect(downloadBlobMock).not.toHaveBeenCalled();
  });
});
