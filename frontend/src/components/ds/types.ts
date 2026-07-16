export type DsButtonVariant =
  | "primary"
  | "secondary"
  | "ghost"
  | "outline"
  | "danger"
  | "link";

export type DsCardVariant = "elevated" | "outlined" | "flat";

export type DsInputVariant = "outlined" | "filled" | "standard";

export type DsSize = "xs" | "sm" | "md" | "lg" | "xl";

export type DsNotifyType = "positive" | "negative" | "warning" | "info";

export interface DsBreadcrumbItem {
  label: string;
  to?: string;
  icon?: string;
}

export interface DsSelectOption<T = string> {
  label: string;
  value: T;
  disable?: boolean;
}

export interface DsTableColumn {
  name: string;
  label: string;
  field: string | ((row: Record<string, unknown>) => unknown);
  align?: "left" | "center" | "right";
  sortable?: boolean;
}
