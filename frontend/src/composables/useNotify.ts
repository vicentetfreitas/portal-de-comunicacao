import {
  dsNotify,
  dsNotifyError,
  dsNotifyInfo,
  dsNotifySuccess,
  dsNotifyWarning
} from "@/components/ds";

import type { DsNotifyOptions } from "@/components/ds";

/**
 * Composable facade for DS notify wrapper (Quasar Notify).
 */
export function useNotify() {
  return {
    notify: (options: DsNotifyOptions) => dsNotify(options),
    success: dsNotifySuccess,
    error: dsNotifyError,
    warning: dsNotifyWarning,
    info: dsNotifyInfo
  };
}

export type { DsNotifyOptions };
