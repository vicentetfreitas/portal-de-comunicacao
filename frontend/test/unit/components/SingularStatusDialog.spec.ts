import { mount } from "@vue/test-utils";
import { Quasar } from "quasar";
import iconSet from "quasar/icon-set/mdi-v7";
import lang from "quasar/lang/pt-BR";
import { describe, expect, it } from "vitest";
import { createI18n } from "vue-i18n";

import SingularStatusDialog from "@/components/organization/singular/SingularStatusDialog.vue";
import ptBR from "@/i18n/pt-BR";
import type { SingularResponse } from "@/types/organization/singular.types";

const activeSingular: SingularResponse = {
  id: 1,
  federationId: 1,
  name: "Unimed Ceará",
  acronym: "UNI-CE",
  unimedCode: "UC001",
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
      plugins: [[Quasar, { lang, iconSet }], i18n]
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

    const buttons = wrapper.findAll(".ds-button");
    const confirmButton = buttons.find(button =>
      button.text().includes("Inativar singular")
    );

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

    const buttons = wrapper.findAll(".ds-button");
    const confirmButton = buttons.find(button =>
      button.text().includes("Ativar singular")
    );

    expect(confirmButton).toBeTruthy();
    await confirmButton!.trigger("click");

    expect(wrapper.emitted("confirm")).toEqual([["ACTIVE"]]);
  });
});
