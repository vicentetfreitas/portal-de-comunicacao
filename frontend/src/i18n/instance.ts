import { createI18n } from "vue-i18n";

import messages from "./index";

export const i18n = createI18n({
  locale: "pt-BR",
  fallbackLocale: "pt-BR",
  messages,
  legacy: false,
  globalInjection: true
});
