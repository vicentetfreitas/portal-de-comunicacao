import { defineBoot } from "#q-app";

import { setupHttpClient } from "@/services/http";

export default defineBoot(() => {
  setupHttpClient();
});
