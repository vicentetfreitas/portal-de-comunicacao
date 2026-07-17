import { expect, test, type Page } from "@playwright/test";

import { mockAuthenticatedAdmin } from "../support/auth-mock";
import {
  createMockEquipeStore,
  installEquipeApiMock,
  type MockEquipeStore
} from "../support/equipe-api-mock";

function equipeFieldValue(page: Page, label: string) {
  return page
    .locator(".equipe-info-card__item")
    .filter({ hasText: label })
    .locator("dd");
}

async function setupEquipeFeature(
  page: Page,
  seed: MockEquipeStore["equipes"] = []
): Promise<MockEquipeStore> {
  const store = createMockEquipeStore(seed);
  await mockAuthenticatedAdmin(page);
  await installEquipeApiMock(page, store);
  return store;
}

async function selectArea(page: Page, areaName: string): Promise<void> {
  const areaField = page.getByLabel("Área");
  await expect(areaField).toBeEnabled({ timeout: 15_000 });
  await areaField.click();
  await page.getByRole("option", { name: areaName }).click();
}

async function gotoEquipeCreate(page: Page): Promise<void> {
  const areasRequest = page.waitForResponse(
    response =>
      response.url().includes("/api/v1/areas") &&
      response.request().method() === "GET" &&
      response.ok()
  );
  await page.goto("/app/administrador/equipes/novo");
  await areasRequest;
}

async function gotoEquipeEdit(page: Page, id: number): Promise<void> {
  const detailRequest = page.waitForResponse(
    response =>
      response.url().includes(`/api/v1/equipes/${id}`) &&
      response.request().method() === "GET" &&
      response.ok()
  );
  await page.goto(`/app/administrador/equipes/${id}/editar`);
  await detailRequest;
  await expect(page.getByLabel("Nome")).toBeVisible({ timeout: 15_000 });
}

test.describe("AT-FE-EQUIPE-001 — Cadastro", () => {
  test("happy path redireciona para detalhe", async ({ page }) => {
    await setupEquipeFeature(page);

    await gotoEquipeCreate(page);
    await selectArea(page, "Área Comunicação");
    await page.getByLabel("Nome").fill("Equipe Alpha");
    await page.getByRole("button", { name: "Cadastrar equipe" }).click();

    await expect(page).toHaveURL(/\/app\/administrador\/equipes\/\d+$/);
    await expect(
      page.getByRole("heading", { name: "Equipe Alpha" })
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Ativa" })).toBeVisible();
  });

  test("nome duplicado exibe erro de validação", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 1,
        areaId: 10,
        name: "Equipe Alpha",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      }
    ]);

    await gotoEquipeCreate(page);
    await selectArea(page, "Área Comunicação");
    await page.getByLabel("Nome").fill("Equipe Alpha");
    await page.getByRole("button", { name: "Cadastrar equipe" }).click();

    await expect(
      page.getByRole("alert", {
        name: "Já existe equipe ativa com este nome na área"
      })
    ).toBeVisible();
  });
});

test.describe("AT-FE-EQUIPE-002 — Detalhe", () => {
  test("consulta detalhe da equipe", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 42,
        areaId: 10,
        name: "Equipe Sul",
        description: "Descrição teste",
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/equipes/42");
    await expect(
      page.getByRole("heading", { name: "Equipe Sul" })
    ).toBeVisible();
    await expect(equipeFieldValue(page, "Descrição")).toHaveText(
      "Descrição teste"
    );
    await expect(equipeFieldValue(page, "Área")).toHaveText("10");
  });

  test("404 exibe estado amigável", async ({ page }) => {
    await setupEquipeFeature(page);

    await page.goto("/app/administrador/equipes/999999");
    await expect(page.getByText("Equipe não encontrada")).toBeVisible();
  });
});

test.describe("AT-FE-EQUIPE-003 — Listagem", () => {
  test("filtro por status e paginação", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 1,
        areaId: 10,
        name: "Alpha Ativa",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      },
      {
        id: 2,
        areaId: 10,
        name: "Beta Inativa",
        description: null,
        leaderId: null,
        status: "INACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      },
      ...Array.from({ length: 11 }, (_, index) => ({
        id: index + 3,
        areaId: 10,
        name: `Ativa ${index + 1}`,
        description: null,
        leaderId: null,
        status: "ACTIVE" as const,
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      }))
    ]);

    await page.goto("/app/administrador/equipes/lista");
    await page.waitForResponse(
      response =>
        response.url().includes("/api/v1/equipes") &&
        response.request().method() === "GET" &&
        response.ok()
    );
    await expect(page.locator(".ds-data-table")).toContainText("Alpha Ativa", {
      timeout: 15_000
    });

    await page.getByLabel("Status").click();
    await page.getByRole("option", { name: "Inativa", exact: true }).click();
    await page.getByRole("button", { name: "Aplicar filtros" }).click();

    await expect(page.getByText("Beta Inativa")).toBeVisible();
    await expect(page.getByText("Alpha Ativa")).not.toBeVisible();

    await page.getByLabel("Status").click();
    await page.getByRole("option", { name: "Ativa", exact: true }).click();
    await page.getByRole("button", { name: "Aplicar filtros" }).click();

    await expect(page.getByText("Alpha Ativa")).toBeVisible();
    await expect(page.locator(".ds-data-table")).toContainText("Ativa 11");

    const tableBottom = page.locator(".ds-data-table .q-table__bottom");
    const nextPage = tableBottom.getByRole("button", {
      name: "Próxima página"
    });
    await expect(nextPage).toBeEnabled();
    await nextPage.click();

    await expect(page.locator(".ds-data-table")).toContainText("Ativa 8");
    await expect(page.locator(".ds-data-table")).not.toContainText(
      "Alpha Ativa"
    );
  });
});

test.describe("AT-FE-EQUIPE-004 — Edição", () => {
  test("happy path atualiza equipe", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 7,
        areaId: 10,
        name: "Equipe Original",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      }
    ]);

    await gotoEquipeEdit(page, 7);
    await page.getByLabel("Nome").fill("Equipe Atualizada");
    await page.getByRole("button", { name: "Salvar alterações" }).click();

    await expect(page).toHaveURL("/app/administrador/equipes/7");
    await expect(
      page.getByRole("heading", { name: "Equipe Atualizada" })
    ).toBeVisible();
  });

  test("nome duplicado na edição exibe erro", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 1,
        areaId: 10,
        name: "Equipe A",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      },
      {
        id: 2,
        areaId: 10,
        name: "Equipe B",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      }
    ]);

    await gotoEquipeEdit(page, 2);
    await page.getByLabel("Nome").fill("Equipe A");
    await page.getByRole("button", { name: "Salvar alterações" }).click();

    await expect(
      page.getByRole("alert", {
        name: "Já existe equipe ativa com este nome na área"
      })
    ).toBeVisible();
  });
});

test.describe("AT-FE-EQUIPE-005 — Status", () => {
  test("inativação com sucesso atualiza badge", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 5,
        areaId: 10,
        name: "Equipe Ativa",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/equipes/5");
    await page.getByRole("button", { name: "Inativar" }).click();
    await page.getByRole("button", { name: "Inativar equipe" }).click();

    await expect(page.getByText("Equipe inativada com sucesso.")).toBeVisible();
    await expect(page.getByRole("status", { name: "Inativa" })).toBeVisible();
    await expect(
      page.getByRole("button", { name: "Ativar", exact: true })
    ).toBeVisible();
  });

  test("bloqueio 422 por colaboradores ativos", async ({ page }) => {
    await setupEquipeFeature(page, [
      {
        id: 8,
        areaId: 10,
        name: "Equipe Bloqueada",
        description: null,
        leaderId: null,
        status: "ACTIVE",
        createdAt: "2026-07-17T12:00:00Z",
        updatedAt: null,
        blockInactivation: true
      }
    ]);

    await page.goto("/app/administrador/equipes/8");
    await page.getByRole("button", { name: "Inativar" }).click();
    await page.getByRole("button", { name: "Inativar equipe" }).click();

    await expect(
      page.getByText("Equipe possui colaboradores ativos vinculados")
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Ativa" })).toBeVisible();
  });
});

test.describe("Hub administrativo", () => {
  test("exibe ações rápidas e navega para listagem", async ({ page }) => {
    await setupEquipeFeature(page);

    await page.goto("/app/administrador/equipes");
    await expect(page.getByText("Ações rápidas")).toBeVisible();
    await page.getByRole("button", { name: "Listar equipes" }).click();
    await expect(page).toHaveURL("/app/administrador/equipes/lista");
  });
});
