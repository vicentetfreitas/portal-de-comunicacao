import { defineBoot } from "#q-app";

import { i18n } from "@/i18n/instance";

export default defineBoot(({ app }) => {
  app.use(i18n);
});
