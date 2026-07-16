import type { Component } from "vue";

import AdminLayout from "./AdminLayout.vue";
import AuthLayout from "./AuthLayout.vue";
import MainLayout from "./MainLayout.vue";
import PublicLayout from "./PublicLayout.vue";

import type { AppLayoutName } from "@/types/router-meta";

export const layoutMap: Record<AppLayoutName, Component> = {
  auth: AuthLayout,
  main: MainLayout,
  admin: AdminLayout,
  public: PublicLayout
};

export { AdminLayout, AuthLayout, MainLayout, PublicLayout };
