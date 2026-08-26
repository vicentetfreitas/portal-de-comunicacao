import { flushPromises, mount } from "@vue/test-utils";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { createI18n } from "vue-i18n";

import AreaColaboradorArquivosPage from "@/pages/area-colaborador/AreaColaboradorArquivosPage.vue";
import ptBR from "@/i18n/pt-BR";

const { listMock, downloadMock, downloadBlobMock } = vi.hoisted(() => ({
  listMock: vi.fn(),
  downloadMock: vi.fn(),
  downloadBlobMock: vi.fn()
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

function mountPage() {
  const i18n = createI18n({
    legacy: false,
    locale: "pt-BR",
    messages: {
      "pt-BR": ptBR
    }
  });

  return mount(AreaColaboradorArquivosPage, {
    global: {
      plugins: [i18n]
    }
  });
}

describe("AreaColaboradorArquivosPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    listMock.mockImplementation(() => new Promise(() => {}));
  });

  it("exibe o skeleton de carregamento", () => {
    const wrapper = mountPage();

    expect(wrapper.find(".app-loading-skeleton").exists()).toBe(true);
  });

  it("lista pastas com seus documentos (AT-DOCUMENTO-001)", async () => {
    listMock.mockResolvedValue({
      content: [
        {
          id: 1,
          nome: "Logotipos",
          documentos: [
            { id: 10, nome: "logo.png", formato: "png", tamanhoBytes: 2048 }
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

    const wrapper = mountPage();
    await flushPromises();

    expect(wrapper.text()).toContain("Logotipos");
    expect(wrapper.text()).toContain("logo.png");
    expect(wrapper.text()).toContain("png");
  });

  it("exibe estado vazio quando não há pastas com permissão (AT-DOCUMENTO-001)", async () => {
    listMock.mockResolvedValue({
      content: [],
      page: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    });

    const wrapper = mountPage();
    await flushPromises();

    expect(wrapper.text()).toContain("Nenhum arquivo disponível");
  });

  it("aciona o download do documento ao clicar no botão (AT-DOCUMENTO-002)", async () => {
    listMock.mockResolvedValue({
      content: [
        {
          id: 1,
          nome: "Logotipos",
          documentos: [
            { id: 10, nome: "logo.png", formato: "png", tamanhoBytes: 2048 }
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
    const blob = new Blob(["conteudo"]);
    downloadMock.mockResolvedValue({ blob, filename: "logo.png" });

    const wrapper = mountPage();
    await flushPromises();

    await wrapper.find("button").trigger("click");
    await flushPromises();

    expect(downloadMock).toHaveBeenCalledWith(10);
    expect(downloadBlobMock).toHaveBeenCalledWith(blob, "logo.png");
  });
});
