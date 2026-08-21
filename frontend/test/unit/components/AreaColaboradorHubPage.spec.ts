import { mount } from "@vue/test-utils";
import { describe, expect, it } from "vitest";
import { createI18n } from "vue-i18n";

import AreaColaboradorHubPage from "@/pages/area-colaborador/AreaColaboradorHubPage.vue";
import ptBR from "@/i18n/pt-BR";

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
});
