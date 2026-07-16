import { defineBoot } from "#q-app";

import { assertEnvConfigured, env } from "@/config/env";

export default defineBoot(() => {
  assertEnvConfigured();

  if (import.meta.env.DEV) {
    console.info(
      `[bootstrap] environment=${env.appEnv} apiBaseUrl=${env.apiBaseUrl} apiTimeoutMs=${env.apiTimeoutMs}`
    );
  }
});
