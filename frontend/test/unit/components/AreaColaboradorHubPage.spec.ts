import { flushPromises, mount } from "@vue/test-utils";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { createI18n } from "vue-i18n";

import AreaColaboradorHubPage from "@/pages/area-colaborador/AreaColaboradorHubPage.vue";
import ptBR from "@/i18n/pt-BR";

const { getByIdMock, activeContext } = vi.hoisted(() => ({
  getByIdMock: vi.fn(),
  activeContext: { value: { areaId: 7 } as { areaId: number | null } | null }
}));

vi.mock("@/composables/useSession", () => ({
  useSession: () => ({
    activeContext
  })
}));

vi.mock("@/services/organization", () => ({
  areaService: {
    getById: getByIdMock
  }
}));

function mountPage() {
  const i18n = createI18n({
    legacy: false,
    locale: "pt-BR",
    messages: {
      "pt-BR": ptBR
    }
  });

  return mount(AreaColaboradorHubPage, {
    global: {
      plugins: [i18n]
    }
  });
}

describe("AreaColaboradorHubPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    activeContext.value = { areaId: 7 };
    getByIdMock.mockImplementation(() => new Promise(() => {}));
  });

  it("exibe o hub com atalhos para Equipe e Arquivos e Documentos", () => {
    const wrapper = mountPage();

    expect(wrapper.text()).toContain("Área");
    expect(wrapper.text()).toContain("Equipe");
    expect(wrapper.text()).toContain("Arquivos e Documentos");
  });

  it("mantém os atalhos desabilitados enquanto as sub-seções não estão implementadas", () => {
    const wrapper = mountPage();

    const actionButtons = wrapper.findAll(".ds-action-card");
    expect(actionButtons).toHaveLength(2);
    for (const button of actionButtons) {
      expect(button.attributes("disabled")).toBeDefined();
    }
  });

  it("exibe o skeleton de carregamento enquanto a Área é lida", () => {
    const wrapper = mountPage();

    expect(wrapper.find(".app-loading-skeleton").exists()).toBe(true);
  });

  it("exibe nome e descrição da Área quando os dados chegam (AT-AREA-COLAB-003)", async () => {
    getByIdMock.mockResolvedValue({
      id: 7,
      singularId: 1,
      name: "Tecnologia da Informação",
      acronym: "TI",
      description: "Área responsável pela infraestrutura de TI",
      managerId: null,
      status: "ACTIVE",
      createdAt: "2026-01-01T00:00:00Z",
      updatedAt: null
    });

    const wrapper = mountPage();
    await flushPromises();

    expect(wrapper.text()).toContain("Tecnologia da Informação");
    expect(wrapper.text()).toContain(
      "Área responsável pela infraestrutura de TI"
    );
    expect(wrapper.find(".app-loading-skeleton").exists()).toBe(false);
  });

  it("exibe estado de não encontrado sem quebrar a navegação (AT-AREA-COLAB-004)", async () => {
    const { ApiError } = await import("@/types/api");
    getByIdMock.mockRejectedValue(
      new ApiError({
        status: 404,
        code: "not_found",
        message: "not found",
        category: "not_found"
      })
    );

    const wrapper = mountPage();
    await flushPromises();

    expect(wrapper.text()).toContain("Área não encontrada");
    expect(wrapper.findAll(".ds-action-card")).toHaveLength(2);
  });
});
