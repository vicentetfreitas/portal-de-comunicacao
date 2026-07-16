import { Notify } from "quasar";

import type { DsNotifyType } from "../types";

export interface DsNotifyOptions {
  message: string;
  type?: DsNotifyType;
  caption?: string;
  timeout?: number;
  position?:
    | "top-left"
    | "top-right"
    | "bottom-left"
    | "bottom-right"
    | "top"
    | "bottom"
    | "left"
    | "right"
    | "center";
}

const defaultTimeout = 4000;

function notify(options: DsNotifyOptions): void {
  Notify.create({
    message: options.message,
    type: options.type ?? "info",
    position: options.position ?? "top-right",
    timeout: options.timeout ?? defaultTimeout,
    classes: "ds-notify",
    ...(options.caption !== undefined ? { caption: options.caption } : {})
  });
}

export function dsNotifySuccess(message: string, caption?: string): void {
  notify({
    message,
    ...(caption !== undefined ? { caption } : {}),
    type: "positive"
  });
}

export function dsNotifyError(message: string, caption?: string): void {
  notify({
    message,
    ...(caption !== undefined ? { caption } : {}),
    type: "negative"
  });
}

export function dsNotifyWarning(message: string, caption?: string): void {
  notify({
    message,
    ...(caption !== undefined ? { caption } : {}),
    type: "warning"
  });
}

export function dsNotifyInfo(message: string, caption?: string): void {
  notify({
    message,
    ...(caption !== undefined ? { caption } : {}),
    type: "info"
  });
}

export { notify as dsNotify };
