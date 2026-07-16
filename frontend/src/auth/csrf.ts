import { httpConfig } from "@/config/http";
import { isMutableHttpMethod, readCsrfToken } from "@/services/http/csrf";

export interface CsrfInfrastructureCheck {
  name: string;
  passed: boolean;
}

export interface CsrfInfrastructureReport {
  ok: boolean;
  checks: CsrfInfrastructureCheck[];
}

/**
 * Validates CSRF infrastructure delivered in PKG-FE-S0-06 (no network calls).
 */
export function validateCsrfInfrastructure(): CsrfInfrastructureReport {
  const checks: CsrfInfrastructureCheck[] = [
    {
      name: "csrf_cookie_name",
      passed: httpConfig.csrfCookieName === "XSRF-TOKEN"
    },
    {
      name: "csrf_header_name",
      passed: httpConfig.csrfHeaderName === "X-XSRF-TOKEN"
    },
    {
      name: "mutable_methods_include_post",
      passed: httpConfig.mutableMethods.includes("post")
    },
    {
      name: "mutable_methods_include_put",
      passed: httpConfig.mutableMethods.includes("put")
    },
    {
      name: "mutable_methods_include_delete",
      passed: httpConfig.mutableMethods.includes("delete")
    },
    {
      name: "csrf_reader_available",
      passed: typeof readCsrfToken === "function"
    },
    {
      name: "mutable_method_detection",
      passed: isMutableHttpMethod("POST") && !isMutableHttpMethod("GET")
    }
  ];

  return {
    ok: checks.every(check => check.passed),
    checks
  };
}
