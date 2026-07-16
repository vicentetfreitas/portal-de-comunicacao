// Configuration for your app
// https://v2.quasar.dev/quasar-cli-vite/quasar-config-file

import { defineConfig } from "#q-app";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export default defineConfig(() => {
  return {
    boot: ["env", "i18n", "theme", "http", "auth", "feedback"],

    css: ["app.scss"],

    extras: ["mdi-v7"],

    build: {
      // envPrefix: "VITE_",
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
      port: 9000,
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
