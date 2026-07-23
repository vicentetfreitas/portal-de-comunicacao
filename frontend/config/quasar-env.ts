import { existsSync, readFileSync } from "node:fs";
import { resolve } from "node:path";

import { parse } from "dotenv";

/**
 * Defaults for Quasar devServer (Node-only — not exposed to the client bundle).
 */
export const DEV_SERVER_DEFAULTS = {
  backendUrl: "http://localhost:8080",
  port: 9000
} as const;

export interface DevServerEnv {
  backendUrl: string;
}

function readBackendUrlFromEnvFiles(
  appDir: string,
  mode: string
): string | undefined {
  const candidates = [
    resolve(appDir, ".env"),
    resolve(appDir, ".env.local"),
    resolve(appDir, `.env.${mode}`),
    resolve(appDir, `.env.${mode}.local`)
  ];

  let value: string | undefined;

  for (const file of candidates) {
    if (!existsSync(file)) {
      continue;
    }

    const parsed = parse(readFileSync(file, "utf-8"));
    if (parsed.BACKEND_URL) {
      value = parsed.BACKEND_URL;
    }
  }

  return value;
}

/**
 * Loads dev-server variables from .env files in the app directory (Vite file order),
 * then process.env, then defaults.
 * Must be called from inside defineConfig((ctx) => ...) so paths match the CLI.
 */
export function loadDevServerEnv(appDir: string, dev: boolean): DevServerEnv {
  const mode = dev ? "development" : "production";

  const backendUrl =
    readBackendUrlFromEnvFiles(appDir, mode) ??
    process.env.BACKEND_URL ??
    DEV_SERVER_DEFAULTS.backendUrl;

  return { backendUrl };
}
