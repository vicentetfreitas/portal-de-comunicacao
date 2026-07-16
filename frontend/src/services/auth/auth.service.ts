import { AUTH_API_PATHS } from "@/config/auth";
import { env } from "@/config/env";
import type { AuthenticatedUser } from "@/auth/types";
import { getHttpClient } from "@/services/http";
import { ApiError, normalizeApiError } from "@/types/api";

import type { AuthMeApiResponse } from "./auth-contracts";

export interface AuthLoginOptions {
  rememberMe?: boolean;
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

  login(options?: AuthLoginOptions): void {
    window.location.assign(this.buildLoginUrl(options));
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
