import { quasar, transformAssetUrls } from "@quasar/vite-plugin";
import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "vitest/config";

export default defineConfig({
  plugins: [
    vue({
      template: { transformAssetUrls }
    }),
    quasar({
      sassVariables: fileURLToPath(
        new URL("./src/css/quasar.variables.scss", import.meta.url)
      )
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url))
    }
  },
  test: {
    environment: "happy-dom",
    globals: true,
    setupFiles: ["./test/vitest/setup.ts"],
    include: ["test/unit/**/*.spec.ts"],
    coverage: {
      provider: "v8",
      reporter: ["text", "html", "lcov", "json-summary"],
      reportsDirectory: "./coverage",
      include: ["src/**/*.{ts,vue}"],
      exclude: [
        "src/**/*.d.ts",
        "src/router/typed-router.d.ts",
        "src/boot/**",
        "src/pages/**"
      ]
    }
  }
});
