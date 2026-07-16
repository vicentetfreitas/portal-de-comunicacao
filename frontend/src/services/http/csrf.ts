import { httpConfig } from "@/config/http";

export function getCookieValue(name: string): string | undefined {
  if (typeof document === "undefined") {
    return undefined;
  }

  const encodedName = encodeURIComponent(name);
  const match = document.cookie.match(
    new RegExp(`(?:^|; )${encodedName}=([^;]*)`)
  );
  const value = match?.[1];
  return value !== undefined ? decodeURIComponent(value) : undefined;
}

export function readCsrfToken(): string | undefined {
  return getCookieValue(httpConfig.csrfCookieName);
}

export function isMutableHttpMethod(method: string): boolean {
  return httpConfig.mutableMethods.includes(
    method.toLowerCase() as (typeof httpConfig.mutableMethods)[number]
  );
}
