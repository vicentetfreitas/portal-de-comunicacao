import { test, expect } from "@playwright/test";

test.describe("bootstrap", () => {
  test("home page loads", async ({ page }) => {
    await page.goto("/");

    await expect(
      page.getByRole("heading", { name: "Portal de Comunicação" })
    ).toBeVisible();
    await expect(
      page.getByText("Infraestrutura frontend — Sprint 0")
    ).toBeVisible();
  });

  test("404 page renders for unknown routes", async ({ page }) => {
    await page.goto("/__foundation-not-found__");

    await expect(page.locator(".not-found-page__code")).toHaveText("404");
    await expect(page.getByText("Página não encontrada")).toBeVisible();
  });
});
