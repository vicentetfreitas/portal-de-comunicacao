import { env } from "@/config/env";

/**
 * Global application configuration resolved at bootstrap.
 */
export const appConfig = {
  name: "Portal de Comunicação",
  env: env.appEnv,
  apiBaseUrl: env.apiBaseUrl
} as const;
