import { expect, test, type Page } from "@playwright/test";

import { mockAuthenticatedAdmin } from "../support/auth-mock";
import {
  createMockSingularStore,
  installSingularApiMock,
  type MockSingularStore
} from "../support/singular-api-mock";

function singularFieldValue(page: Page, label: string) {
  return page
    .locator(".singular-info-card__item")
    .filter({ hasText: label })
    .locator("dd");
}

async function setupSingularFeature(
  page: Page,
  seed: MockSingularStore["singulares"] = []
): Promise<MockSingularStore> {
  const store = createMockSingularStore(seed);
  await mockAuthenticatedAdmin(page);
  await installSingularApiMock(page, store);
  return store;
}

test.describe("AT-FE-SINGULAR-001 — Cadastro", () => {
  test("happy path redireciona para detalhe", async ({ page }) => {
    await setupSingularFeature(page);

    await page.goto("/app/administrador/singulares/novo");
    await page.getByLabel("Nome").fill("Unimed Fortaleza");
    await page.getByLabel("Sigla").fill("UNI-FOR");
    await page.getByLabel("Código Unimed").fill("UC010");
    await page.getByRole("button", { name: "Cadastrar singular" }).click();

    await expect(page).toHaveURL(/\/app\/administrador\/singulares\/\d+$/);
    await expect(
      page.getByRole("heading", { name: "Unimed Fortaleza" })
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Ativa" })).toBeVisible();
  });

  test("sigla duplicada exibe erro de validação", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 1,
        federationId: 1,
        name: "Unimed Ceará",
        acronym: "UNI-CE",
        unimedCode: "UC001",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/singulares/novo");
    await page.getByLabel("Nome").fill("Outra Singular");
    await page.getByLabel("Sigla").fill("UNI-CE");
    await page.getByLabel("Código Unimed").fill("UC999");
    await page.getByRole("button", { name: "Cadastrar singular" }).click();

    await expect(
      page.getByRole("alert", { name: "Sigla já cadastrada" })
    ).toBeVisible();
  });
});

test.describe("AT-FE-SINGULAR-002 — Detalhe", () => {
  test("consulta detalhe da singular", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 42,
        federationId: 1,
        name: "Unimed Sul",
        acronym: "UNI-SUL",
        unimedCode: "UC042",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/singulares/42");
    await expect(
      page.getByRole("heading", { name: "Unimed Sul" })
    ).toBeVisible();
    await expect(singularFieldValue(page, "Sigla")).toHaveText("UNI-SUL");
    await expect(singularFieldValue(page, "Código Unimed")).toHaveText("UC042");
  });

  test("404 exibe estado amigável", async ({ page }) => {
    await setupSingularFeature(page);

    await page.goto("/app/administrador/singulares/999999");
    await expect(page.getByText("Singular não encontrada")).toBeVisible();
  });
});

test.describe("AT-FE-SINGULAR-003 — Listagem", () => {
  test("filtro por status e paginação", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 1,
        federationId: 1,
        name: "Alpha Ativa",
        acronym: "ALP-A",
        unimedCode: "001",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      },
      {
        id: 2,
        federationId: 1,
        name: "Beta Inativa",
        acronym: "BET-I",
        unimedCode: "002",
        status: "INACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      },
      ...Array.from({ length: 11 }, (_, index) => ({
        id: index + 3,
        federationId: 1,
        name: `Ativa ${index + 1}`,
        acronym: `ATV-${index + 1}`,
        unimedCode: `00${index + 3}`,
        status: "ACTIVE" as const,
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }))
    ]);

    await page.goto("/app/administrador/singulares/lista");
    await page.waitForResponse(
      response =>
        response.url().includes("/api/v1/singulares") &&
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

test.describe("AT-FE-SINGULAR-004 — Edição", () => {
  test("happy path atualiza singular", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 7,
        federationId: 1,
        name: "Unimed Original",
        acronym: "ORG",
        unimedCode: "007",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/singulares/7/editar");
    await page.getByLabel("Nome").fill("Unimed Atualizada");
    await page.getByLabel("Sigla").fill("ATU");
    await page.getByLabel("Código Unimed").fill("007A");
    await page.getByRole("button", { name: "Salvar alterações" }).click();

    await expect(page).toHaveURL("/app/administrador/singulares/7");
    await expect(
      page.getByRole("heading", { name: "Unimed Atualizada" })
    ).toBeVisible();
    await expect(singularFieldValue(page, "Sigla")).toHaveText("ATU");
  });

  test("sigla duplicada na edição exibe erro", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 1,
        federationId: 1,
        name: "Singular A",
        acronym: "SIG-A",
        unimedCode: "001",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      },
      {
        id: 2,
        federationId: 1,
        name: "Singular B",
        acronym: "SIG-B",
        unimedCode: "002",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/singulares/2/editar");
    await page.getByLabel("Sigla").fill("SIG-A");
    await page.getByRole("button", { name: "Salvar alterações" }).click();

    await expect(
      page.getByRole("alert", { name: "Sigla já cadastrada" })
    ).toBeVisible();
  });
});

test.describe("AT-FE-SINGULAR-005 — Status", () => {
  test("inativação com sucesso atualiza badge", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 5,
        federationId: 1,
        name: "Singular Ativa",
        acronym: "ATV",
        unimedCode: "005",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null
      }
    ]);

    await page.goto("/app/administrador/singulares/5");
    await page.getByRole("button", { name: "Inativar" }).click();
    await page.getByRole("button", { name: "Inativar singular" }).click();

    await expect(
      page.getByText("Singular inativada com sucesso.")
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Inativa" })).toBeVisible();
    await expect(
      page.getByRole("button", { name: "Ativar", exact: true })
    ).toBeVisible();
  });

  test("bloqueio 422 por áreas ativas", async ({ page }) => {
    await setupSingularFeature(page, [
      {
        id: 8,
        federationId: 1,
        name: "Singular Bloqueada",
        acronym: "BLK",
        unimedCode: "008",
        status: "ACTIVE",
        createdAt: "2026-07-16T12:00:00Z",
        updatedAt: null,
        blockInactivation: true
      }
    ]);

    await page.goto("/app/administrador/singulares/8");
    await page.getByRole("button", { name: "Inativar" }).click();
    await page.getByRole("button", { name: "Inativar singular" }).click();

    await expect(
      page.getByText(
        "Não é possível inativar singular com áreas ativas vinculadas"
      )
    ).toBeVisible();
    await expect(page.getByRole("status", { name: "Ativa" })).toBeVisible();
  });
});

test.describe("Hub administrativo", () => {
  test("exibe ações rápidas e navega para listagem", async ({ page }) => {
    await setupSingularFeature(page);

    await page.goto("/app/administrador/singulares");
    await expect(page.getByText("Ações rápidas")).toBeVisible();
    await page.getByRole("button", { name: "Listar singulares" }).click();
    await expect(page).toHaveURL("/app/administrador/singulares/lista");
  });
});
