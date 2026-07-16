/**
 * HTTP client configuration — CSRF, correlation ID and mutable methods.
 */
export const httpConfig = {
  csrfCookieName: "XSRF-TOKEN",
  csrfHeaderName: "X-XSRF-TOKEN",
  correlationHeaderName: "X-Correlation-Id",
  mutableMethods: ["post", "put", "patch", "delete"] as const
} as const;

export type MutableHttpMethod = (typeof httpConfig.mutableMethods)[number];
