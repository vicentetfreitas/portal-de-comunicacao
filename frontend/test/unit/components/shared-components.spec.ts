import { mount } from "@vue/test-utils";
import { describe, expect, it } from "vitest";

import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import AppLoadingSpinner from "@/components/shared/AppLoadingSpinner.vue";
import DsButton from "@/components/ds/atoms/DsButton.vue";

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
