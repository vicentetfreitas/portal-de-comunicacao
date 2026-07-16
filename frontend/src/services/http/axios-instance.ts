import axios, { type AxiosInstance } from "axios";

import { env } from "@/config/env";
import {
  applyRequestInterceptors,
  onRequestInterceptorError
} from "@/services/http/interceptors/request.interceptor";
import { handleResponseError } from "@/services/http/interceptors/response.interceptor";

let httpClientInstance: AxiosInstance | null = null;

export function createHttpClient(): AxiosInstance {
  const client = axios.create({
    baseURL: env.apiBaseUrl,
    withCredentials: true,
    timeout: env.apiTimeoutMs,
    headers: {
      "Content-Type": "application/json"
    }
  });

  client.interceptors.request.use(
    applyRequestInterceptors,
    onRequestInterceptorError
  );

  client.interceptors.response.use(
    response => response,
    error => handleResponseError(error, client)
  );

  return client;
}

export function getHttpClient(): AxiosInstance {
  if (!httpClientInstance) {
    httpClientInstance = createHttpClient();
  }
  return httpClientInstance;
}

export function setupHttpClient(): AxiosInstance {
  httpClientInstance = createHttpClient();
  return httpClientInstance;
}
