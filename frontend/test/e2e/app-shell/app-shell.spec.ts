import { expect, test, type Page } from "@playwright/test";

import { mockAuthenticatedAdmin } from "../support/auth-mock";

/**
 * App Shell (header/sidebar) + Home ("Fique por dentro") visual/structural
 * reconciliation round against the Figma "Home" frame (node 7:3, file
 * WHDHRAMXXslmxOIzK2dbJG). Per this repo's E2E policy
 * (construction/17-frontend-e2e-behavior-policy.md): asserts user-visible
 * behavior (text, roles, navigation), not incidental CSS — the one
 * exception is the horizontal-scrollbar checks, which assert against a
 * concrete regression class this round fixed at its root cause (the mini
 * sidebar rail's width formula going negative), not styling detail.
 */

async function gotoApp(page: Page): Promise<void> {
  await mockAuthenticatedAdmin(page);
  await page.goto("/app");
  await expect(
    page.getByRole("heading", { name: "Fique por dentro" })
  ).toBeVisible();
}

async function hasHorizontalOverflow(page: Page): Promise<boolean> {
  return page.evaluate(
    () => document.documentElement.scrollWidth > window.innerWidth
  );
}

async function sidebarOverflowsItsOwnWidth(page: Page): Promise<boolean> {
  return page.evaluate(() => {
    const sidebar = document.querySelector(".app-sidebar");
    return sidebar ? sidebar.scrollWidth > sidebar.clientWidth : false;
  });
}

test.describe("App Shell — toolbar e navegação", () => {
  test("renderiza o app autenticado com toolbar e sidebar", async ({
    page
  }) => {
    await gotoApp(page);

    await expect(
      page.getByRole("link", { name: "Portal de Comunicação" })
    ).toBeVisible();
    // Hamburger is desktop-hidden by design (see next test) — the sidebar
    // is permanently open at this width, so there's nothing to toggle.
    await expect(
      page.getByRole("button", { name: "Alternar menu" })
    ).toBeHidden();
    await expect(await hasHorizontalOverflow(page)).toBe(false);
  });

  test("saudação da sidebar mostra somente o primeiro nome", async ({
    page
  }) => {
    await gotoApp(page);

    // ADMIN_USER mock name is "Administrador" (single token) — exercised
    // separately by unit tests (utils/user-display-name.spec.ts) for the
    // multi-token/email-fallback cases.
    await expect(page.getByText("Olá,")).toBeVisible();
    await expect(page.locator(".ds-profile-summary__name")).toHaveText(
      "Administrador"
    );
  });

  test("ordem de navegação: Página inicial, Federação, Singulares, Sistemas e Serviços", async ({
    page
  }) => {
    await gotoApp(page);

    const sidebar = page.locator(".app-sidebar");
    const labels = await sidebar
      .locator(".ds-nav-item__label, .sidebar-directory__label")
      .allTextContents();

    // "Áreas" removed from the sidebar (explicit product decision,
    // 2026-08-26) — "Federação" now covers the same ground and more.
    expect(labels).toEqual([
      "Página inicial",
      "Federação",
      "Singulares",
      "Sistemas e Serviços"
    ]);
  });

  test("sidebar permanece sempre aberta no desktop; sem controle de recolhimento redundante", async ({
    page
  }) => {
    await gotoApp(page);

    await expect(page.getByText("Recolher menu")).toHaveCount(0);
    await expect(page.locator(".app-sidebar__footer")).toHaveCount(0);
    // No collapse affordance at all at this width (see previous test) —
    // recolhimento only exists below the sidebar's own breakpoint (mobile).
    await expect(page.locator(".app-sidebar")).toBeVisible();
  });

  test("Serviços é exibido, porém inerte (FT-SERVICOS ainda é DRAFT)", async ({
    page
  }) => {
    await gotoApp(page);

    await expect(page.getByRole("link", { name: "Serviços" })).toHaveCount(0);
    const servicos = page.locator(
      '.app-sidebar [aria-disabled="true"]:has-text("Serviços")'
    );
    await expect(servicos).toBeVisible();
  });

  test("Federação e Singular expandem sem causar overflow horizontal", async ({
    page
  }) => {
    await gotoApp(page);

    await page.getByRole("button", { name: "Federação" }).click();
    await expect(page.getByPlaceholder("Buscar área...")).toBeVisible();
    await page.getByRole("button", { name: "Singular" }).click();
    await expect(page.getByPlaceholder("Buscar singular...")).toBeVisible();

    expect(await hasHorizontalOverflow(page)).toBe(false);
  });

  test("'Editar perfil' navega para /app/perfil com formulário funcional (persistência local)", async ({
    page
  }) => {
    await gotoApp(page);

    await page.getByRole("button", { name: "Editar perfil" }).click();
    await expect(page).toHaveURL("/app/perfil");
    await expect(
      page.getByRole("heading", { name: "Meu perfil" })
    ).toBeVisible();

    // E-mail de login é real (sessão) e somente leitura; Nome é seedado da
    // sessão mas editável — nenhum dos dois é fabricado.
    await expect(page.getByLabel("E-mail de login")).toHaveValue(
      "admin@unimedceara.com.br"
    );
    await expect(page.getByLabel("E-mail de login")).toBeDisabled();
    await expect(page.getByLabel("Nome completo")).toHaveValue("Administrador");

    // Cargo/e-mail adicional/telefones/ramais/celulares não existem em
    // nenhum contrato aprovado — persistidos só neste navegador
    // (localStorage), não no backend. See usePerfilLocalFields.ts.
    await page.getByLabel("Cargo").fill("Analista de Comunicação");
    await page.getByRole("button", { name: "Salvar" }).click();
    await expect(page.getByText("Perfil salvo neste navegador.")).toBeVisible();

    await page.reload();
    await expect(page.getByLabel("Cargo")).toHaveValue(
      "Analista de Comunicação"
    );
  });

  test("nome/sigla longos e sem quebra natural não estouram a largura do sidebar", async ({
    page
  }) => {
    await mockAuthenticatedAdmin(page);
    await page.route("**/api/v1/singulares**", route =>
      route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify({
          success: true,
          data: {
            content: [
              {
                id: 1,
                name: "Unimedicalíssimasupercalifragilisticexpialidociosamentegigante",
                acronym: "SUPERLONGACRONYMWITHOUTSPACES"
              }
            ],
            totalElements: 1
          }
        })
      })
    );
    await page.goto("/app");
    await expect(
      page.getByRole("heading", { name: "Fique por dentro" })
    ).toBeVisible();

    await page.getByRole("button", { name: "Singular" }).click();
    await expect(
      page.getByText(
        "Unimedicalíssimasupercalifragilisticexpialidociosamentegigante"
      )
    ).toBeVisible();

    expect(await sidebarOverflowsItsOwnWidth(page)).toBe(false);
    expect(await hasHorizontalOverflow(page)).toBe(false);
  });

  test("Federação destaca a própria área do colaborador no topo da lista", async ({
    page
  }) => {
    await mockAuthenticatedAdmin(page);
    // ADMIN_USER's own organizationalLinks.areaId is null — override with a
    // real vínculo área so the highlight/sort-to-top logic has something to
    // match against.
    await page.route("**/api/v1/auth/me", route =>
      route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify({
          success: true,
          data: {
            id: 1,
            email: "admin@unimedceara.com.br",
            name: "Administrador",
            permissions: [],
            sessionId: "session-e2e",
            primeiroAcesso: false,
            roles: ["ADMIN"],
            organizationalLinks: {
              federationId: 1,
              singularId: 10,
              areaId: 200,
              teamId: null
            }
          }
        })
      })
    );
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
            content: [
              { id: 100, name: "Comunicação", acronym: "COM" },
              { id: 200, name: "Recursos Humanos", acronym: "RH" }
            ],
            totalElements: 2
          }
        })
      })
    );

    await page.goto("/app");
    await expect(
      page.getByRole("heading", { name: "Fique por dentro" })
    ).toBeVisible();

    await page.getByRole("button", { name: "Federação" }).click();
    const items = page.locator(".sidebar-directory__item-name");
    await expect(items.first()).toHaveText("Recursos Humanos");
    // Bold, not a "Minha área" badge/subtitle — no singular name ("Unimed
    // Fortaleza") or badge text should render for any item.
    const highlightedName = page.locator(
      ".sidebar-directory__item--highlighted .sidebar-directory__item-name"
    );
    await expect(highlightedName).toHaveCSS("font-weight", "700");
    await expect(page.getByText("Minha área")).toHaveCount(0);
    await expect(page.getByText("Unimed Fortaleza")).toHaveCount(0);
  });

  test("Singular não lista Unimed Ceará (representa a própria federação)", async ({
    page
  }) => {
    await mockAuthenticatedAdmin(page);
    await page.route("**/api/v1/singulares**", route =>
      route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify({
          success: true,
          data: {
            content: [
              { id: 1, name: "Unimed Ceará", acronym: "UCE" },
              { id: 11, name: "Unimed Sobral", acronym: "USOB" }
            ],
            totalElements: 2
          }
        })
      })
    );

    await page.goto("/app");
    await expect(
      page.getByRole("heading", { name: "Fique por dentro" })
    ).toBeVisible();

    await page.getByRole("button", { name: "Singular" }).click();
    const names = page.locator(".sidebar-directory__item-name");
    await expect(names).toHaveText(["Unimed Sobral"]);
  });

  test("botão de voltar não aparece mais no header (perto da logo)", async ({
    page
  }) => {
    await gotoApp(page);

    // Removed from AppHeader.vue entirely (explicit product decision) — it
    // now lives beside each page's own title (DsPageHeader `show-back`),
    // opt-in per page, not a global header control. See
    // test/e2e/federacao/federacao.spec.ts for the pages that opt in.
    await expect(page.getByRole("button", { name: "Voltar" })).toHaveCount(0);

    await page.route("**/api/v1/colaboradores**", route =>
      route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify({
          success: true,
          data: { content: [], totalElements: 0 }
        })
      })
    );
    await page.goto("/app/administrador/colaboradores");
    await expect(page.getByRole("button", { name: "Voltar" })).toHaveCount(0);
  });
});

test.describe("Home — Fique por dentro", () => {
  test("exibe o card principal e dois cards secundários", async ({ page }) => {
    await gotoApp(page);

    await expect(
      page
        .getByRole("heading", {
          name: "Atenção Integral à Saúde lança Momento Bem-estar"
        })
        .first()
    ).toBeVisible();
    await expect(
      page.getByText("Descritivo pequeno sobre a notícia")
    ).toHaveCount(2);
  });
});

test.describe("Mobile", () => {
  test.use({ viewport: { width: 390, height: 844 } });

  test("drawer abre/fecha sem overflow horizontal", async ({ page }) => {
    await gotoApp(page);

    const sidebar = page.locator(".app-sidebar");
    const menuToggle = page.getByRole("button", { name: "Alternar menu" });

    await expect(sidebar).toBeHidden();
    expect(await hasHorizontalOverflow(page)).toBe(false);

    await menuToggle.click();
    await expect(sidebar).toBeVisible();
    expect(await hasHorizontalOverflow(page)).toBe(false);

    const sidebarBox = await sidebar.boundingBox();
    expect(sidebarBox).not.toBeNull();
    expect(sidebarBox!.x + sidebarBox!.width).toBeLessThanOrEqual(390);

    // The open drawer overlays the header on mobile (`q-drawer--on-top`),
    // covering the toggle button itself — closing goes through Quasar's
    // own backdrop-click dismissal instead of a second click on a
    // now-hidden target.
    await page
      .locator(".q-drawer__backdrop")
      .click({ position: { x: 370, y: 10 } });
    await expect(sidebar).toBeHidden();
  });
});
