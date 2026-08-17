import { describe, expect, it, vi } from "vitest";

import { ApiError } from "@/types/api";
import { useLoginPage } from "@/composables/useLoginPage";

const loginMock = vi.fn();

vi.mock("vue-i18n", () => ({
  useI18n: () => ({
    t: (key: string) => key
  })
}));

vi.mock("vue-router", () => ({
  useRoute: () => ({
    query: {}
  })
}));

vi.mock("@/composables/useAuth", () => ({
  useAuth: () => ({
    login: loginMock
  })
}));

describe("useLoginPage", () => {
  it("blocks submit when fields are empty", () => {
    const page = useLoginPage();

    page.handleSubmit();

    expect(loginMock).not.toHaveBeenCalled();
    expect(page.fieldErrors.value.usuario).toBeDefined();
    expect(page.fieldErrors.value.senha).toBeDefined();
  });

  it("redirects via auth login when fields are filled", async () => {
    loginMock.mockResolvedValue(undefined);
    const page = useLoginPage();
    page.usuario.value = "colaborador@unimedceara.com.br";
    page.senha.value = "secret";
    page.rememberMe.value = true;

    await page.handleSubmit();

    expect(loginMock).toHaveBeenCalledWith({
      rememberMe: true,
      email: "colaborador@unimedceara.com.br",
      password: "secret"
    });
  });

  it("shows a user-friendly banner when login fails", async () => {
    loginMock.mockRejectedValue(
      new ApiError({
        status: 401,
        code: "UNAUTHORIZED",
        message: "Autenticação não realizada",
        category: "authentication"
      })
    );
    const page = useLoginPage();
    page.usuario.value = "colaborador@unimedceara.com.br";
    page.senha.value = "wrong";

    await page.handleSubmit();

    expect(page.bannerMessage.value).toBe(
      "layout.auth.errors.invalidCredentials"
    );
    expect(page.isSubmitting.value).toBe(false);
  });

  it("shows portal access message when backend returns forbidden", async () => {
    loginMock.mockRejectedValue(
      new ApiError({
        status: 403,
        code: "FORBIDDEN",
        message: "Colaborador sem autorização para acessar o Portal",
        category: "authorization"
      })
    );
    const page = useLoginPage();
    page.usuario.value = "colaborador@unimedceara.com.br";
    page.senha.value = "secret";

    await page.handleSubmit();

    expect(page.bannerMessage.value).toBe(
      "layout.auth.errors.portalAccessDenied"
    );
  });
});
