import { config } from "@vue/test-utils";
import { Notify, Quasar } from "quasar";
import iconSet from "quasar/icon-set/mdi-v7";
import lang from "quasar/lang/pt-BR";
import { beforeEach, vi } from "vitest";

import "quasar/dist/quasar.css";

config.global.plugins = [[Quasar, { plugins: { Notify }, lang, iconSet }]];

beforeEach(() => {
  document.body.innerHTML = "";
  document.cookie = "";
  vi.clearAllMocks();
});
