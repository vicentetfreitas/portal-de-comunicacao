import { AUTH_API_PATHS, AUTH_COOKIE_NAMES } from "@/config/auth";
import type {
  AuthLoginHandler,
  AuthLogoutHandler,
  AuthRefreshHandler,
  AuthenticatedUser,
  AuthSessionHydrator
} from "@/auth/types";
import type { ApiResponse } from "@/types/api";

export { AUTH_API_PATHS, AUTH_COOKIE_NAMES };

export type AuthenticatedUserResponse = AuthenticatedUser;

export type AuthMeApiResponse = ApiResponse<AuthenticatedUserResponse>;

/**
 * FT-AUTH service contract — implementation deferred to Sprint 1.
 */
export interface AuthService
  extends
    AuthSessionHydrator,
    AuthLoginHandler,
    AuthLogoutHandler,
    AuthRefreshHandler {}
