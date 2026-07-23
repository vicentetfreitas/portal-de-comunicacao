import { mkdtempSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";

import { afterEach, describe, expect, it, vi } from "vitest";

import {
  DEV_SERVER_DEFAULTS,
  loadDevServerEnv
} from "../../../config/quasar-env";

describe("loadDevServerEnv", () => {
  let tempDir: string;

  afterEach(() => {
    if (tempDir) {
      rmSync(tempDir, { recursive: true, force: true });
    }
    vi.unstubAllEnvs();
  });

  it("uses default localhost:8080 when no env files or process env", () => {
    tempDir = mkdtempSync(join(tmpdir(), "quasar-env-"));
    const result = loadDevServerEnv(tempDir, true);
    expect(result.backendUrl).toBe(DEV_SERVER_DEFAULTS.backendUrl);
  });

  it("reads BACKEND_URL from .env in app directory", () => {
    tempDir = mkdtempSync(join(tmpdir(), "quasar-env-"));
    writeFileSync(
      join(tempDir, ".env"),
      "BACKEND_URL=http://localhost:9090\n",
      "utf8"
    );
    const result = loadDevServerEnv(tempDir, true);
    expect(result.backendUrl).toBe("http://localhost:9090");
  });

  it("prefers .env over process.env when both are set", () => {
    tempDir = mkdtempSync(join(tmpdir(), "quasar-env-"));
    vi.stubEnv("BACKEND_URL", "http://localhost:7070");
    writeFileSync(
      join(tempDir, ".env"),
      "BACKEND_URL=http://localhost:9090\n",
      "utf8"
    );
    const result = loadDevServerEnv(tempDir, true);
    expect(result.backendUrl).toBe("http://localhost:9090");
  });
});
