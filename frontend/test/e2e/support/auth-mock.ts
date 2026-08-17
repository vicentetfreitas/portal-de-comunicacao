import type { Page } from "@playwright/test";

/**
 * Homologated GET /auth/me payload (AUTH-API-003).
 * Optional `roles` aligns with frontend AuthenticatedUser (authorization helpers).
 */
const ADMIN_USER = {
  id: 1,
  email: "admin@unimedceara.com.br",
  name: "Administrador",
  permissions: [] as string[],
  sessionId: "session-e2e",
  roles: ["ADMIN"] as string[],
  organizationalLinks: {
    federationId: 1 as number | null,
    singularId: null as number | null,
    areaId: null as number | null,
    teamId: null as number | null
  }
};

function successEnvelope<T>(data: T) {
  return {
    timestamp: new Date().toISOString(),
    success: true,
    data
  };
}

export async function mockAuthenticatedAdmin(page: Page): Promise<void> {
  await page.route("**/api/v1/auth/me", async route => {
    await route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify(successEnvelope(ADMIN_USER))
    });
  });

  await page.route("**/api/v1/auth/refresh", async route => {
    await route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify(successEnvelope({ refreshed: true }))
    });
  });
}

export async function mockUnauthenticated(page: Page): Promise<void> {
  await page.route("**/api/v1/auth/me", async route => {
    await route.fulfill({
      status: 401,
      contentType: "application/json",
      body: JSON.stringify({
        timestamp: new Date().toISOString(),
        status: 401,
        error: "UNAUTHORIZED",
        message: "Não autenticado",
        path: "/api/v1/auth/me"
      })
    });
  });
}
