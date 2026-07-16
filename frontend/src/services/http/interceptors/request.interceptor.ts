import type { InternalAxiosRequestConfig } from "axios";

import { httpConfig } from "@/config/http";
import {
  createCorrelationId,
  getCorrelationHeaderName
} from "@/services/http/correlation-id";
import { isMutableHttpMethod, readCsrfToken } from "@/services/http/csrf";

export function applyRequestInterceptors(
  config: InternalAxiosRequestConfig
): InternalAxiosRequestConfig {
  const headers = config.headers;

  headers.set(getCorrelationHeaderName(), createCorrelationId());
  headers.set("Accept", "application/json");

  if (isMutableHttpMethod(config.method ?? "get")) {
    const csrfToken = readCsrfToken();
    if (csrfToken) {
      headers.set(httpConfig.csrfHeaderName, csrfToken);
    }
  }

  return config;
}

export function onRequestInterceptorError(error: unknown): Promise<never> {
  return Promise.reject(error);
}
