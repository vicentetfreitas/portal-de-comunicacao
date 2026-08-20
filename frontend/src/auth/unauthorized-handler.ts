import { ROUTE_PATHS } from "@/constants/routes";
import type { ApiError } from "@/types/api";

import {
  buildAuthRouteWithError,
  isAuthApiRequestUrl
} from "./session-redirect";

export interface UnauthorizedHandlerContext {
  requestUrl?: string;
}

export type UnauthorizedHandler = (
  error: ApiError,
  context?: UnauthorizedHandlerContext
) => Promise<boolean>;

export interface UnauthorizedHandlerDeps {
  refreshSession: () => Promise<boolean>;
  onSessionExpired: (redirectPath?: string) => void;
}

/**
 * FT-AUTH — 401 handler: refresh once, retry original request, else redirect login.
 */
export function createUnauthorizedHandler(
  deps: UnauthorizedHandlerDeps
): UnauthorizedHandler {
  let refreshPromise: Promise<boolean> | null = null;

  return async (_error, context) => {
    if (isAuthApiRequestUrl(context?.requestUrl)) {
      return false;
    }

    if (!refreshPromise) {
      refreshPromise = deps.refreshSession().finally(() => {
        refreshPromise = null;
      });
    }

    const refreshed = await refreshPromise;
    if (refreshed) {
      return true;
    }

    deps.onSessionExpired();
    return false;
  };
}

export function handleSessionExpired(redirectPath?: string): void {
  const target = new URL(
    buildAuthRouteWithError("unauthorized"),
    window.location.origin
  );
  if (redirectPath && redirectPath !== ROUTE_PATHS.AUTH) {
    target.searchParams.set("redirect", redirectPath);
  }
  window.location.assign(target.toString());
}
