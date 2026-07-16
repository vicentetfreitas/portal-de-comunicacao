/**
 * Runtime configuration from external Vite environment variables.
 * Validation runs at application boot (see src/boot/env.ts).
 */
export const env = {
  appEnv: import.meta.env.VITE_APP_ENV ?? "",
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL ?? "",
  apiTimeoutMs: Number(import.meta.env.VITE_API_TIMEOUT_MS ?? 30_000)
} as const;

export function assertEnvConfigured(): void {
  if (!env.appEnv) {
    throw new Error("Missing environment variable: VITE_APP_ENV");
  }
  if (!env.apiBaseUrl) {
    throw new Error("Missing environment variable: VITE_API_BASE_URL");
  }
}

export type AppEnvironment = string;
