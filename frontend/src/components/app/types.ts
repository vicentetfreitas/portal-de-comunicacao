export type AppShellVariant = "main" | "admin";

export interface AppNavItem {
  labelKey: string;
  to: string;
  icon: string;
  section?: "primary" | "admin";
  /**
   * Renders as an inert `<span>` (DsNavItem) instead of a link — for nav
   * items with no destination yet (e.g. "Serviços", `FT-SERVICOS` is DRAFT).
   * Same non-navigable pattern already used by the "Arquivos e Documentos"
   * card in `AreaColaboradorHubPage.vue`.
   */
  disabled?: boolean;
  /**
   * This round's explicit nav-order decision places the colaborador's own
   * "Áreas" hub after the Federação/Singular/Serviços block, not right
   * below "Página inicial" — `"trailing"` items render last, after the
   * organization-directory block. Defaults to leading.
   */
  placement?: "leading" | "trailing";
}
