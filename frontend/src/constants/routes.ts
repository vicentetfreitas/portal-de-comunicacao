/**
 * Route paths — structural constants for navigation and guards.
 * Features must reference these paths instead of hardcoded strings.
 */
export const ROUTE_PATHS = {
  HOME: "/",
  SHOWCASE: "/showcase",
  AUTH: "/auth",
  LOGIN: "/login",
  APP: "/app",
  ADMIN: "/admin",
  UNAUTHORIZED: "/unauthorized"
} as const;

export const ROUTE_NAMES = {
  HOME: "home",
  SHOWCASE: "showcase",
  AUTH: "auth",
  APP: "app",
  ADMIN: "admin",
  NOT_FOUND: "not-found",
  UNAUTHORIZED: "unauthorized"
} as const;

export type RoutePath = (typeof ROUTE_PATHS)[keyof typeof ROUTE_PATHS];
