import { mount } from "@vue/test-utils";
import { describe, expect, it } from "vitest";
import { createI18n } from "vue-i18n";

import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import AppLoadingSpinner from "@/components/shared/AppLoadingSpinner.vue";
import DsButton from "@/components/ds/atoms/DsButton.vue";
import DsDialog from "@/components/ds/molecules/DsDialog.vue";
import DsNavItem from "@/components/ds/molecules/DsNavItem.vue";
import ptBR from "@/i18n/pt-BR";

describe("shared components", () => {
  it("renders AppEmptyState title and description", () => {
    const wrapper = mount(AppEmptyState, {
      props: {
        title: "Nenhum item",
        description: "Adicione conteúdo para começar."
      }
    });

    expect(wrapper.text()).toContain("Nenhum item");
    expect(wrapper.text()).toContain("Adicione conteúdo para começar.");
  });

  it("renders AppLoadingSpinner label", () => {
    const wrapper = mount(AppLoadingSpinner, {
      props: {
        label: "Carregando dados"
      }
    });

    expect(wrapper.text()).toContain("Carregando dados");
    expect(wrapper.attributes("role")).toBe("status");
  });

  it("renders AppLoadingSkeleton rows", () => {
    const wrapper = mount(AppLoadingSkeleton, {
      props: {
        rows: 2
      }
    });

    expect(wrapper.findAll(".app-loading-skeleton__row")).toHaveLength(2);
  });
});

describe("DsButton", () => {
  it("renders slot content", () => {
    const wrapper = mount(DsButton, {
      slots: {
        default: "Salvar"
      }
    });

    expect(wrapper.text()).toContain("Salvar");
  });
});

describe("DsNavItem", () => {
  it("exposes label as aria-label when mini, since visible text is removed", () => {
    const wrapper = mount(DsNavItem, {
      props: {
        label: "Início",
        icon: "mdi-home",
        mini: true
      }
    });

    expect(wrapper.attributes("aria-label")).toBe("Início");
    expect(wrapper.find(".ds-nav-item__label").exists()).toBe(false);
  });

  it("does not set aria-label when not mini, since visible text is present", () => {
    const wrapper = mount(DsNavItem, {
      props: {
        label: "Início",
        icon: "mdi-home"
      }
    });

    expect(wrapper.attributes("aria-label")).toBeUndefined();
    expect(wrapper.text()).toContain("Início");
  });
});

describe("DsDialog", () => {
  it("exposes an accessible name on the close button", async () => {
    const i18n = createI18n({
      legacy: false,
      locale: "pt-BR",
      messages: {
        "pt-BR": ptBR
      }
    });

    const wrapper = mount(DsDialog, {
      attachTo: document.body,
      global: {
        plugins: [i18n]
      },
      props: {
        title: "Confirmar ação",
        modelValue: true
      }
    });

    await wrapper.vm.$nextTick();

    const closeButton = document.body.querySelector(
      'button[aria-label="Fechar"]'
    );
    expect(closeButton).not.toBeNull();
  });
});
