import { defineConfig } from "#q-app";

import { DEV_SERVER_DEFAULTS, loadDevServerEnv } from "./config/quasar-env.js";

export default defineConfig(ctx => {
  const { backendUrl } = loadDevServerEnv(ctx.appPaths.appDir, ctx.dev);

  return {
    boot: ["env", "i18n", "theme", "http", "auth", "feedback"],

    css: ["app.scss"],

    extras: ["mdi-v7"],

    build: {
      env: {
        clientPrefix: "VITE_"
      },
      typescript: {
        strict: true,
        vueShim: true
      },

      filenameBasedRouting: false,
      vueRouterMode: "history"
    },

    devServer: {
      port: DEV_SERVER_DEFAULTS.port,
      open: false,
      proxy: {
        "/api": {
          target: backendUrl,
          changeOrigin: true
        }
      }
    },

    framework: {
      config: {
        dark: "auto"
      },
      iconSet: "mdi-v7",
      lang: "pt-BR",
      plugins: ["Dark", "Notify"]
    },

    animations: []
  };
});
