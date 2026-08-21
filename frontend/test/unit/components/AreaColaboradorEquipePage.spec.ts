import { flushPromises, mount } from "@vue/test-utils";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { createI18n } from "vue-i18n";

import AreaColaboradorEquipePage from "@/pages/area-colaborador/AreaColaboradorEquipePage.vue";
import ptBR from "@/i18n/pt-BR";

const { listMock, activeContext } = vi.hoisted(() => ({
  listMock: vi.fn(),
  activeContext: { value: { areaId: 7 } as { areaId: number | null } | null }
}));

vi.mock("@/composables/useSession", () => ({
  useSession: () => ({
    activeContext
  })
}));

vi.mock("@/services/organization", () => ({
  equipeService: {
    list: listMock
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

  return mount(AreaColaboradorEquipePage, {
    global: {
      plugins: [i18n]
    }
  });
}

describe("AreaColaboradorEquipePage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    activeContext.value = { areaId: 7 };
    listMock.mockImplementation(() => new Promise(() => {}));
  });

  it("exibe o skeleton de carregamento", () => {
    const wrapper = mountPage();

    expect(wrapper.find(".app-loading-skeleton").exists()).toBe(true);
  });

  it("lista nome e descrição das equipes da Área (AT-AREA-COLAB-005)", async () => {
    listMock.mockResolvedValue({
      content: [
        { id: 1, areaId: 7, name: "TI", description: "Tecnologia" },
        { id: 2, areaId: 7, name: "RH", description: "Recursos Humanos" }
      ],
      page: 0,
      size: 10,
      totalElements: 2,
      totalPages: 1,
      first: true,
      last: true
    });

    const wrapper = mountPage();
    await flushPromises();

    expect(wrapper.text()).toContain("TI");
    expect(wrapper.text()).toContain("Tecnologia");
    expect(wrapper.text()).toContain("RH");
    expect(wrapper.text()).toContain("Recursos Humanos");
  });

  it("exibe estado vazio quando a Área não tem equipes (AT-AREA-COLAB-006)", async () => {
    listMock.mockResolvedValue({
      content: [],
      page: 0,
      size: 10,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    });

    const wrapper = mountPage();
    await flushPromises();

    expect(wrapper.text()).toContain("Nenhuma equipe vinculada");
  });
});
