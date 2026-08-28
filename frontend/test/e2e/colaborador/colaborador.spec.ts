import { expect, test, type Page } from "@playwright/test";

import { mockAuthenticatedAdmin } from "../support/auth-mock";
import {
  createMockColaboradorStore,
  installColaboradorApiMock,
  installColaboradorOrgOptionsMock,
  type MockColaboradorStore
} from "../support/colaborador-api-mock";

function colaboradorFieldValue(page: Page, label: string) {
  return page
    .locator(".colaborador-info-card__item")
    .filter({ hasText: label })
    .locator("dd");
}

async function setupColaboradorFeature(
  page: Page,
  seed: MockColaboradorStore["colaboradores"] = []
): Promise<MockColaboradorStore> {
  const store = createMockColaboradorStore(seed);
  await mockAuthenticatedAdmin(page);
  await installColaboradorApiMock(page, store);
  await installColaboradorOrgOptionsMock(page);
  return store;
}

test.describe("AT-FE-COLABORADOR-001 — Cadastro", () => {
  test("happy path redireciona para detalhe", async ({ page }) => {
    await setupColaboradorFeature(page);

    await page.goto("/app/administrador/colaboradores/novo");
    await page.getByLabel("Nome").fill("Fulano de Tal");
    await page.getByLabel("E-mail").fill("fulano.tal@unimedceara.com.br");
    await page.getByLabel("Identificador Zimbra").fill("fulano.tal");
    await page.getByRole("button", { name: "Cadastrar colaborador" }).click();

    await expect(page).toHaveURL(/\/app\/administrador\/colaboradores\/\d+$/);
    await expect(
      page.getByRole("heading", { name: "Fulano de Tal" })
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Ativo" })).toBeVisible();
  });

  test("e-mail duplicado exibe erro", async ({ page }) => {
    await setupColaboradorFeature(page, [
      {
        id: 1,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: "Colaborador Existente",
        email: "existente@unimedceara.com.br",
        zimbraId: "existente",
        biography: null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/colaboradores/novo");
    await page.getByLabel("Nome").fill("Outro Colaborador");
    await page.getByLabel("E-mail").fill("existente@unimedceara.com.br");
    await page.getByLabel("Identificador Zimbra").fill("outro.colaborador");
    await page.getByRole("button", { name: "Cadastrar colaborador" }).click();

    await expect(
      page.getByText("Já existe colaborador com este e-mail")
    ).toBeVisible();
  });
});

test.describe("AT-FE-COLABORADOR-002 — Detalhe", () => {
  test("consulta detalhe do colaborador", async ({ page }) => {
    await setupColaboradorFeature(page, [
      {
        id: 42,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: "Beltrana Souza",
        email: "beltrana.souza@unimedceara.com.br",
        zimbraId: "beltrana.souza",
        biography: null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/colaboradores/42");
    await expect(
      page.getByRole("heading", { name: "Beltrana Souza" })
    ).toBeVisible();
    await expect(
      colaboradorFieldValue(page, "Identificador Zimbra")
    ).toHaveText("beltrana.souza");
    await expect(colaboradorFieldValue(page, "E-mail")).toHaveText(
      "beltrana.souza@unimedceara.com.br"
    );
  });

  test("404 exibe estado amigável", async ({ page }) => {
    await setupColaboradorFeature(page);

    await page.goto("/app/administrador/colaboradores/999999");
    await expect(page.getByText("Colaborador não encontrado")).toBeVisible();
  });
});

test.describe("AT-FE-COLABORADOR-003 — Listagem", () => {
  test("filtro por status e paginação", async ({ page }) => {
    await setupColaboradorFeature(page, [
      {
        id: 1,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: "Alpha Ativo",
        email: "alpha@unimedceara.com.br",
        zimbraId: "alpha",
        biography: null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      },
      {
        id: 2,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: "Beta Inativo",
        email: "beta@unimedceara.com.br",
        zimbraId: "beta",
        biography: null,
        status: "INACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      },
      ...Array.from({ length: 11 }, (_, index) => ({
        id: index + 3,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: `Ativo ${index + 1}`,
        email: `ativo${index + 1}@unimedceara.com.br`,
        zimbraId: `ativo${index + 1}`,
        biography: null,
        status: "ACTIVE" as const,
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }))
    ]);

    await page.goto("/app/administrador/colaboradores/lista");
    await page.waitForResponse(
      response =>
        response.url().includes("/api/v1/colaboradores") &&
        response.request().method() === "GET" &&
        response.ok()
    );
    await expect(page.locator(".ds-data-table")).toContainText("Alpha Ativo", {
      timeout: 15_000
    });

    await page.getByLabel("Status").click();
    await page.getByRole("option", { name: "Inativo", exact: true }).click();
    await page.getByRole("button", { name: "Aplicar filtros" }).click();

    await expect(page.getByText("Beta Inativo")).toBeVisible();
    await expect(page.getByText("Alpha Ativo")).not.toBeVisible();

    await page.getByLabel("Status").click();
    await page.getByRole("option", { name: "Ativo", exact: true }).click();
    await page.getByRole("button", { name: "Aplicar filtros" }).click();

    await expect(page.getByText("Alpha Ativo")).toBeVisible();
    await expect(page.locator(".ds-data-table")).toContainText("Ativo 11");

    const tableBottom = page.locator(".ds-data-table .q-table__bottom");
    const nextPage = tableBottom.getByRole("button", {
      name: "Próxima página"
    });
    await expect(nextPage).toBeEnabled();
    await nextPage.click();

    await expect(page.locator(".ds-data-table")).toContainText("Ativo 8");
    await expect(page.locator(".ds-data-table")).not.toContainText(
      "Alpha Ativo"
    );
  });
});

test.describe("AT-FE-COLABORADOR-004 — Edição", () => {
  test("carrega, pré-preenche (e-mail somente leitura) e mantém área/equipe reais", async ({
    page
  }) => {
    await setupColaboradorFeature(page, [
      {
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
    ]);

    // Overrides the default (empty) org-options mock — the composable's
    // singularId/areaId watchers used to fire async after `reset()` and
    // unconditionally null the next field down, silently wiping the
    // colaborador's real areaId/teamId right after loading them. This
    // guards that regression, not just that the fields render.
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
    await expect(
      page.getByRole("heading", { name: "Fulano da Silva" })
    ).toBeVisible();
    // Regression guard: useColaboradorOrganizationalOptions's singularId/
    // areaId watchers used to fire async after reset() and unconditionally
    // null the next field down, silently wiping areaId/teamId right after
    // loading them from the API response.
    await expect(colaboradorFieldValue(page, "Singular")).toHaveText("10");
    await expect(colaboradorFieldValue(page, "Área")).toHaveText("200");
    await expect(colaboradorFieldValue(page, "Equipe")).toHaveText("300");
  });
});

test.describe("AT-FE-COLABORADOR-005 — Status", () => {
  test("inativação com sucesso atualiza badge", async ({ page }) => {
    await setupColaboradorFeature(page, [
      {
        id: 5,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: "Colaborador Ativo",
        email: "ativo@unimedceara.com.br",
        zimbraId: "ativo",
        biography: null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/colaboradores/5");
    await page.getByRole("button", { name: "Inativar" }).click();
    await page.getByRole("button", { name: "Inativar colaborador" }).click();

    await expect(
      page.getByText("Colaborador inativado com sucesso.")
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Inativo" })).toBeVisible();
    await expect(
      page.getByRole("button", { name: "Ativar", exact: true })
    ).toBeVisible();
  });

  test("bloqueio 422 por subordinados ativos", async ({ page }) => {
    await setupColaboradorFeature(page, [
      {
        id: 9,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: null,
        name: "Gestor Bloqueado",
        email: "gestor@unimedceara.com.br",
        zimbraId: "gestor",
        biography: null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      },
      {
        id: 10,
        federationId: 1,
        singularId: null,
        areaId: null,
        teamId: null,
        managerId: 9,
        name: "Subordinado Ativo",
        email: "subordinado@unimedceara.com.br",
        zimbraId: "subordinado",
        biography: null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/colaboradores/9");
    await page.getByRole("button", { name: "Inativar" }).click();
    await page.getByRole("button", { name: "Inativar colaborador" }).click();

    await expect(
      page.getByText("Colaborador possui subordinados ativos")
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Ativo" })).toBeVisible();
  });
});

test.describe("Hub administrativo", () => {
  test("exibe ações rápidas e navega para listagem", async ({ page }) => {
    await setupColaboradorFeature(page);

    await page.goto("/app/administrador/colaboradores");
    await expect(page.getByText("Ações rápidas")).toBeVisible();
    await page.getByRole("button", { name: "Listar colaboradores" }).click();
    await expect(page).toHaveURL("/app/administrador/colaboradores/lista");
  });
});
