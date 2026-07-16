export { BaseApiClient } from "./base-api-client";
export {
  createCorrelationId,
  getCorrelationHeaderName
} from "./correlation-id";
export { getCookieValue, isMutableHttpMethod, readCsrfToken } from "./csrf";
export {
  dispatchHttpError,
  getDefaultHttpErrorMessage,
  setGlobalHttpErrorHandler,
  type HttpErrorHandler
} from "./error-handler";
export {
  createHttpClient,
  getHttpClient,
  setupHttpClient
} from "./axios-instance";
export { applyRequestInterceptors } from "./interceptors/request.interceptor";
export {
  handleResponseError,
  setUnauthorizedHandler
} from "./interceptors/response.interceptor";
export type {
  UnauthorizedHandler,
  UnauthorizedHandlerContext
} from "@/auth/unauthorized-handler";
