export type AppShellVariant = "main" | "admin";

export interface AppNavItem {
  labelKey: string;
  to: string;
  icon: string;
  section?: "primary" | "admin";
}
