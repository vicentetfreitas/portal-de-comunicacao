import { mount } from "@vue/test-utils";
import { defineComponent, h } from "vue";
import { describe, expect, it } from "vitest";
import { createI18n } from "vue-i18n";

import SingularStatusDialog from "@/components/organization/singular/SingularStatusDialog.vue";
import ptBR from "@/i18n/pt-BR";
import type { SingularResponse } from "@/types/organization/singular.types";

const DsDialogStub = defineComponent({
  name: "DsDialog",
  props: {
    title: { type: String, required: true },
    subtitle: { type: String, default: undefined },
    modelValue: { type: Boolean, default: false },
    persistent: { type: Boolean, default: false },
    minWidth: { type: String, default: undefined }
  },
  emits: ["update:modelValue"],
  setup(props, { slots }) {
    return () =>
      h("div", { class: "ds-dialog-stub" }, [
        h("h2", props.title),
        props.subtitle ? h("p", props.subtitle) : null,
        slots.default?.(),
        h("div", { class: "ds-dialog-actions" }, slots.actions?.())
      ]);
  }
});

const activeSingular: SingularResponse = {
  id: 1,
  federationId: 1,
  name: "Unimed Ceará",
  acronym: "UNI-CE",
  unimedCode: 42,
  registroAns: "123456",
  status: "ACTIVE",
  createdAt: "2026-07-16T12:00:00Z",
  updatedAt: null
};

function mountDialog(singular: SingularResponse) {
  const i18n = createI18n({
    legacy: false,
    locale: "pt-BR",
    messages: {
      "pt-BR": ptBR
    }
  });

  return mount(SingularStatusDialog, {
    props: {
      modelValue: true,
      singular,
      loading: false
    },
    global: {
      plugins: [i18n],
      stubs: {
        DsDialog: DsDialogStub
      }
    }
  });
}

describe("SingularStatusDialog", () => {
  it("shows deactivation copy for active singulares", () => {
    const wrapper = mountDialog(activeSingular);

    expect(wrapper.text()).toContain("Inativar singular");
    expect(wrapper.text()).toContain("Unimed Ceará");
  });

  it("emits INACTIVE when confirming deactivation", async () => {
    const wrapper = mountDialog(activeSingular);

    const confirmButton = wrapper
      .findAll(".ds-button")
      .find(button => button.text().includes("Inativar singular"));

    expect(confirmButton).toBeTruthy();
    await confirmButton!.trigger("click");

    expect(wrapper.emitted("confirm")).toEqual([["INACTIVE"]]);
  });

  it("emits ACTIVE when confirming reactivation", async () => {
    const wrapper = mountDialog({
      ...activeSingular,
      status: "INACTIVE"
    });

    expect(wrapper.text()).toContain("Ativar singular");

    const confirmButton = wrapper
      .findAll(".ds-button")
      .find(button => button.text().includes("Ativar singular"));

    expect(confirmButton).toBeTruthy();
    await confirmButton!.trigger("click");

    expect(wrapper.emitted("confirm")).toEqual([["ACTIVE"]]);
  });
});
