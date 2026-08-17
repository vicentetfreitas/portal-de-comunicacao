import { AUTH_API_PATHS } from "@/config/auth";
import { env } from "@/config/env";
import { httpConfig } from "@/config/http";
import type { AuthenticatedUser } from "@/auth/types";
import { getHttpClient } from "@/services/http";
import { readCsrfToken } from "@/services/http/csrf";

import {
  ApiError,
  categorizeStatus,
  isErrorResponseBody,
  normalizeApiError
} from "@/types/api";

import type { AuthMeApiResponse } from "./auth-contracts";

async function ensureCsrfCookie(): Promise<void> {
  if (readCsrfToken()) {
    return;
  }

  await fetch(`${env.apiBaseUrl}${AUTH_API_PATHS.me}`, {
    method: "GET",
    credentials: "include"
  });
}

export interface AuthLoginOptions {
  rememberMe?: boolean;
  email?: string;
  password?: string;
  state?: string;
}

function unwrapMeResponse(payload: AuthMeApiResponse): AuthenticatedUser {
  if (payload.success !== true || payload.data === undefined) {
    throw new ApiError({
      status: 500,
      code: "API_RESPONSE_NOT_SUCCESSFUL",
      message: payload.message ?? "Resposta de autenticação inválida"
    });
  }

  return payload.data;
}

function isAuthenticationFailure(error: unknown): boolean {
  return normalizeApiError(error).category === "authentication";
}

/**
 * FT-AUTH HTTP service — cookie-based session (no token storage client-side).
 */
export class AuthApiService {
  buildLoginUrl(options?: AuthLoginOptions): string {
    const params = new URLSearchParams();
    if (options?.rememberMe === true) {
      params.set("remember_me", "true");
    }

    const query = params.toString();
    const path = query
      ? `${AUTH_API_PATHS.login}?${query}`
      : AUTH_API_PATHS.login;

    return `${env.apiBaseUrl}${path}`;
  }

  login(options?: AuthLoginOptions): void | Promise<void> {
    if (options?.email && options?.password) {
      return this.submitCredentials(options);
    }

    window.location.assign(this.buildLoginUrl(options));
  }

  private async submitCredentials(options: AuthLoginOptions): Promise<void> {
    await ensureCsrfCookie();

    const params = new URLSearchParams();
    params.set("email", options.email ?? "");
    params.set("password", options.password ?? "");
    if (options.rememberMe === true) {
      params.set("remember_me", "true");
    }
    if (options.state) {
      params.set("state", options.state);
    }

    const headers: Record<string, string> = {
      "Content-Type": "application/x-www-form-urlencoded"
    };
    const csrfToken = readCsrfToken();
    if (csrfToken) {
      headers[httpConfig.csrfHeaderName] = csrfToken;
    }

    const response = await fetch(`${env.apiBaseUrl}${AUTH_API_PATHS.login}`, {
      method: "POST",
      credentials: "include",
      headers,
      body: params.toString(),
      redirect: "manual"
    });

    if (response.status === 302) {
      const location = response.headers.get("Location");
      if (location) {
        window.location.assign(location);
        return;
      }
    }

    if (response.ok) {
      window.location.assign("/app");
      return;
    }

    const body: unknown = await response.json().catch(() => undefined);
    if (isErrorResponseBody(body)) {
      throw ApiError.fromErrorResponse(body);
    }

    throw new ApiError({
      status: response.status,
      code: "AUTH_LOGIN_FAILED",
      message: "Não foi possível concluir o login",
      category: categorizeStatus(response.status)
    });
  }

  async fetchCurrentUser(): Promise<AuthenticatedUser> {
    const client = getHttpClient();
    const response = await client.get<AuthMeApiResponse>(AUTH_API_PATHS.me);
    return unwrapMeResponse(response.data);
  }

  async logout(): Promise<void> {
    const client = getHttpClient();
    await client.post(AUTH_API_PATHS.logout);
  }

  async refresh(): Promise<boolean> {
    try {
      const client = getHttpClient();
      await client.post(AUTH_API_PATHS.refresh);
      return true;
    } catch (error) {
      if (isAuthenticationFailure(error)) {
        return false;
      }
      throw error;
    }
  }
}

export const authService = new AuthApiService();
