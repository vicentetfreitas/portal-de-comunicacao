import { expect, test } from "@playwright/test";

import { mockAuthenticatedAdmin } from "../support/auth-mock";

/**
 * AT-FE-COLABORADOR-004 (Edição) — `specs/features/colaborador/specification-frontend.md`.
 * `ColaboradorEditPage.vue` was a placeholder stub until this round; this
 * guards both the page existing for real and the hydration bug that
 * initially shipped with it: `useColaboradorOrganizationalOptions`'s
 * `singularId`/`areaId` watchers fire asynchronously and unconditionally
 * null out the next field down, which — without `loadOptionsFor`'s
 * hydration guard — silently wiped the colaborador's real `areaId`/
 * `teamId` right after `reset()` populated them from the API response.
 */
test.describe("AT-FE-COLABORADOR-004 — Edição", () => {
  test("carrega, pré-preenche (e-mail somente leitura) e mantém área/equipe reais", async ({
    page
  }) => {
    await mockAuthenticatedAdmin(page);

    let updateBody: unknown;

    await page.route("**/api/v1/colaboradores/42", async route => {
      if (route.request().method() === "GET") {
        await route.fulfill({
          status: 200,
          contentType: "application/json",
          body: JSON.stringify({
            success: true,
            data: {
              id: 42,
              federationId: 1,
              singularId: 10,
              areaId: 200,
              teamId: 300,
              managerId: null,
              name: "Fulano de Tal",
              email: "fulano@unimedceara.com.br",
              zimbraId: "fulano.tal",
              biography: null,
              status: "ACTIVE",
              birthDate: null,
              hireDate: null,
              lastAccessAt: null,
              createdAt: "2026-01-01T00:00:00Z",
              updatedAt: null
            }
          })
        });
        return;
      }
      if (route.request().method() === "PUT") {
        updateBody = route.request().postDataJSON();
        await route.fulfill({
          status: 200,
          contentType: "application/json",
          body: JSON.stringify({
            success: true,
            data: {
              id: 42,
              federationId: 1,
              singularId: 10,
              areaId: 200,
              teamId: 300,
              managerId: null,
              name: "Fulano da Silva",
              email: "fulano@unimedceara.com.br",
              zimbraId: "fulano.tal",
              biography: null,
              status: "ACTIVE",
              birthDate: null,
              hireDate: null,
              lastAccessAt: null,
              createdAt: "2026-01-01T00:00:00Z",
              updatedAt: "2026-01-02T00:00:00Z"
            }
          })
        });
        return;
      }
      await route.fallback();
    });

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
    await page.route("**/api/v1/areas**", route =>
      route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify({
          success: true,
          data: {
            content: [{ id: 200, name: "Recursos Humanos", acronym: "RH" }],
            totalElements: 1
          }
        })
      })
    );
    await page.route("**/api/v1/equipes**", route =>
      route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify({
          success: true,
          data: {
            content: [{ id: 300, name: "Equipe Alpha" }],
            totalElements: 1
          }
        })
      })
    );

    await page.goto("/app/administrador/colaboradores/42/editar");
    await expect(
      page.getByRole("heading", { name: "Editar colaborador" })
    ).toBeVisible();

    await expect(page.getByLabel("Nome")).toHaveValue("Fulano de Tal");
    const emailInput = page.getByLabel("E-mail");
    await expect(emailInput).toHaveValue("fulano@unimedceara.com.br");
    await expect(emailInput).toBeDisabled();

    await expect(page.getByText("Recursos Humanos")).toBeVisible();
    await expect(page.getByText("Equipe Alpha")).toBeVisible();

    await page.getByLabel("Nome").fill("Fulano da Silva");
    await page.getByRole("button", { name: "Salvar alterações" }).click();

    await expect(page).toHaveURL("/app/administrador/colaboradores/42");
    expect(updateBody).toMatchObject({
      name: "Fulano da Silva",
      singularId: 10,
      areaId: 200,
      teamId: 300
    });
    expect(updateBody).not.toHaveProperty("email");
  });
});
