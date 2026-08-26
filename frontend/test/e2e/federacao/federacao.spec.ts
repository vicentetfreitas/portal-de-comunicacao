import { expect, test, type Page } from "@playwright/test";

import { mockAuthenticatedAdmin } from "../support/auth-mock";

/**
 * "Federação" browsing flow — clicking an área or singular in the sidebar
 * directory (`AppSidebar.vue`) now navigates instead of being a read-only
 * list (explicit product decision, 2026-08-26 — see
 * `docs/architecture/decisions/AUDITORIA-DS-FIGMA-01.md`). Backed entirely
 * by already-`APPROVED` read APIs (`GET /api/v1/areas/{id}`, `GET /api/v1/
 * areas?singularId=`, `GET /api/v1/colaboradores?areaId=`) — no new backend
 * contract. "Arquivos e Documentos" stays disabled (`FT-DOCUMENTO` is
 * `DRAFT`); the roster shows only `name`/`email` (the only person-level
 * fields any approved contract returns — no cargo/telefone/"Contato
 * setorial", none of which exist in any current API).
 */
async function mockFederacaoFixtures(page: Page): Promise<void> {
  await mockAuthenticatedAdmin(page);

  await page.route("**/api/v1/singulares**", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify({
        success: true,
        data: {
          content: [{ id: 10, name: "Unimed Fortaleza", acronym: "UFOR" }],
          totalElements: 1
        }
      })
    })
  );
  await page.route("**/api/v1/singulares/10", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify({
        success: true,
        data: {
          id: 10,
          name: "Unimed Fortaleza",
          acronym: "UFOR",
          status: "ACTIVE"
        }
      })
    })
  );
  await page.route("**/api/v1/areas/200", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify({
        success: true,
        data: {
          id: 200,
          name: "Marketing",
          description: "O Marketing da Unimed Ceará conecta propósitos.",
          status: "ACTIVE"
        }
      })
    })
  );
  await page.route("**/api/v1/areas?**", route => {
    const url = route.request().url();
    const content = url.includes("singularId=10")
      ? [{ id: 200, name: "Marketing", description: "Área de Marketing" }]
      : [{ id: 200, name: "Marketing", description: null }];
    return route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify({
        success: true,
        data: { content, totalElements: content.length }
      })
    });
  });
  await page.route("**/api/v1/colaboradores**", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify({
        success: true,
        data: {
          content: [
            {
              id: 1,
              name: "Jessica Monteiro",
              email: "jessicamonteiro@unimedceara.com.br"
            },
            {
              id: 2,
              name: "Vicente Freitas",
              email: "vicentefreitas@unimedceara.com.br"
            }
          ],
          totalElements: 2
        }
      })
    })
  );
}

test.describe("Federação — área e singular navegáveis", () => {
  test("clicar numa área abre o hub, com Equipe navegável e Arquivos desabilitado", async ({
    page
  }) => {
    await mockFederacaoFixtures(page);
    await page.goto("/app");
    await expect(
      page.getByRole("heading", { name: "Fique por dentro" })
    ).toBeVisible();

    await page.getByRole("button", { name: "Federação" }).click();
    await page.getByText("Marketing").click();
    await expect(page).toHaveURL("/app/federacao/areas/200");
    await expect(
      page.getByRole("heading", { name: "Marketing" })
    ).toBeVisible();
    await expect(
      page.getByText("O Marketing da Unimed Ceará conecta propósitos.")
    ).toBeVisible();

    // FT-DOCUMENTO is DRAFT — no fabricated file browser, just a disabled
    // shortcut (same pattern as "Serviços").
    await expect(
      page.getByRole("button", { name: "Arquivos e Documentos" })
    ).toBeDisabled();

    await page.getByRole("button", { name: "Equipe" }).click();
    await expect(page).toHaveURL("/app/federacao/areas/200/equipe");
    await expect(page.getByText("Jessica Monteiro")).toBeVisible();
    await expect(
      page.getByText("jessicamonteiro@unimedceara.com.br")
    ).toBeVisible();
    await expect(page.getByText("Vicente Freitas")).toBeVisible();

    // Cargo/telefone/ramal/"Contato setorial" are explicitly mocked (no
    // approved API returns them) — still real UI, just not real data. See
    // useFederacaoAreaRoster.ts's module comment for why.
    await expect(page.getByText("Coordenador(a)")).toBeVisible();
    await expect(page.getByText("(85) 98001-1001")).toBeVisible();
    await expect(page.getByText("Contato setorial")).toBeVisible();

    // Back button now lives beside the page's own title (DsPageHeader,
    // `show-back`), inside the main content — not in the header near the
    // logo (removed from AppHeader.vue this round, see app-shell.spec.ts).
    await expect(page.getByRole("banner")).not.toContainText("Voltar");
    await page
      .getByRole("main")
      .getByRole("button", { name: "Voltar" })
      .click();
    await expect(page).toHaveURL("/app/federacao/areas/200");
  });

  test("clicar numa singular abre suas áreas, cada uma navegável ao hub da área", async ({
    page
  }) => {
    await mockFederacaoFixtures(page);
    await page.goto("/app");
    await expect(
      page.getByRole("heading", { name: "Fique por dentro" })
    ).toBeVisible();

    await page.getByRole("button", { name: "Singular" }).click();
    await page.getByText("Unimed Fortaleza").click();
    await expect(page).toHaveURL("/app/federacao/singulares/10");
    await expect(
      page.getByRole("heading", { name: "Unimed Fortaleza" })
    ).toBeVisible();

    await page.getByText("Marketing").first().click();
    await expect(page).toHaveURL("/app/federacao/areas/200");
  });
});
