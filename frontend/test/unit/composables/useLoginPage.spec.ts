import { describe, expect, it, vi } from "vitest";

import { ApiError } from "@/types/api";
import { ROUTE_PATHS } from "@/constants/routes";
import { useLoginPage } from "@/composables/useLoginPage";

const loginMock = vi.fn();
const hydrateSessionMock = vi.fn();
const replaceMock = vi.fn();

const sessionStoreState = {
  needsPrimeiroAcesso: false,
  isBlocked: false
};

vi.mock("vue-i18n", () => ({
  useI18n: () => ({
    t: (key: string) => key
  })
}));

vi.mock("vue-router", () => ({
  useRoute: () => ({
    query: {}
  }),
  useRouter: () => ({
    replace: replaceMock
  })
}));

vi.mock("@/stores/session.store", () => ({
  useSessionStore: () => sessionStoreState
}));

vi.mock("@/composables/useAuth", () => ({
  useAuth: () => ({
    login: loginMock,
    hydrateSession: hydrateSessionMock
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

  it("hydrates session and navigates to app after successful login", async () => {
    loginMock.mockResolvedValue(undefined);
    hydrateSessionMock.mockResolvedValue(undefined);
    sessionStoreState.needsPrimeiroAcesso = false;
    sessionStoreState.isBlocked = false;

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
    expect(hydrateSessionMock).toHaveBeenCalledWith({ force: true });
    expect(replaceMock).toHaveBeenCalledWith(ROUTE_PATHS.APP);
    expect(page.isSubmitting.value).toBe(false);
  });

  it("navigates to primeiro acesso after successful login when session requires onboarding", async () => {
    loginMock.mockResolvedValue(undefined);
    hydrateSessionMock.mockResolvedValue(undefined);
    sessionStoreState.needsPrimeiroAcesso = true;
    sessionStoreState.isBlocked = false;

    const page = useLoginPage();
    page.usuario.value = "novo@unimedceara.com.br";
    page.senha.value = "secret";

    await page.handleSubmit();

    expect(hydrateSessionMock).toHaveBeenCalledWith({ force: true });
    expect(replaceMock).toHaveBeenCalledWith(ROUTE_PATHS.PRIMEIRO_ACESSO);
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
    expect(hydrateSessionMock).not.toHaveBeenCalled();
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
