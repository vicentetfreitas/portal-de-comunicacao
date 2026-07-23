import { describe, expect, it, vi } from "vitest";

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

  it("redirects via auth login when fields are filled", () => {
    const page = useLoginPage();
    page.usuario.value = "colaborador@unimedceara.com.br";
    page.senha.value = "secret";
    page.rememberMe.value = true;

    page.handleSubmit();

    expect(loginMock).toHaveBeenCalledWith({ rememberMe: true });
  });
});
