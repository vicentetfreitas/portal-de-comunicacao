import { beforeEach, describe, expect, it, vi } from "vitest";

import { ROUTE_PATHS } from "@/constants/routes";
import { usePrimeiroAcessoPage } from "@/composables/usePrimeiroAcessoPage";

const {
  listPrimeiroAcessoAreasMock,
  completePrimeiroAcessoMock,
  hydrateSessionMock,
  replaceMock,
  handleErrorMock,
  dsNotifySuccessMock,
  isBlocked,
  isReady
} = vi.hoisted(() => ({
  listPrimeiroAcessoAreasMock: vi.fn(),
  completePrimeiroAcessoMock: vi.fn(),
  hydrateSessionMock: vi.fn(),
  replaceMock: vi.fn(),
  handleErrorMock: vi.fn(),
  dsNotifySuccessMock: vi.fn(),
  isBlocked: { value: false },
  isReady: { value: false }
}));

vi.mock("vue-i18n", () => ({
  useI18n: () => ({
    t: (key: string) => key
  })
}));

vi.mock("vue-router", () => ({
  useRouter: () => ({
    replace: replaceMock
  })
}));

vi.mock("@/composables/useAuth", () => ({
  useAuth: () => ({
    user: { value: { email: "novo@unimedceara.com.br" } },
    logout: vi.fn(),
    hydrateSession: hydrateSessionMock
  })
}));

vi.mock("@/composables/useSession", () => ({
  useSession: () => ({
    isBlocked,
    isReady
  })
}));

vi.mock("@/composables/useStandardErrorHandling", () => ({
  useStandardErrorHandling: () => ({
    handleError: handleErrorMock
  })
}));

vi.mock("@/services/auth/auth.service", () => ({
  authService: {
    listPrimeiroAcessoAreas: listPrimeiroAcessoAreasMock,
    completePrimeiroAcesso: completePrimeiroAcessoMock
  }
}));

vi.mock("@/components/ds", () => ({
  dsNotifySuccess: dsNotifySuccessMock
}));

describe("usePrimeiroAcessoPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    isBlocked.value = false;
    isReady.value = false;
    listPrimeiroAcessoAreasMock.mockResolvedValue([
      { id: 20, name: "TI", acronym: "TI" }
    ]);
    completePrimeiroAcessoMock.mockResolvedValue({
      id: 9,
      primeiroAcesso: false
    });
    hydrateSessionMock.mockImplementation(async () => {
      isReady.value = true;
    });
  });

  it("loads areas of the resolved singular without sending singularId", async () => {
    const page = usePrimeiroAcessoPage();
    await page.loadAreas();

    expect(listPrimeiroAcessoAreasMock).toHaveBeenCalledWith();
    expect(page.areaOptions.value).toEqual([{ label: "TI (TI)", value: "20" }]);
  });

  it("does not load areas when primeiro acesso is blocked", async () => {
    isBlocked.value = true;
    const page = usePrimeiroAcessoPage();
    await page.loadAreas();

    expect(listPrimeiroAcessoAreasMock).not.toHaveBeenCalled();
  });

  it("requires an area before confirming", async () => {
    const page = usePrimeiroAcessoPage();
    await page.confirm();

    expect(page.areaError.value).toBe("layout.primeiroAcesso.areaRequired");
    expect(completePrimeiroAcessoMock).not.toHaveBeenCalled();
    expect(replaceMock).not.toHaveBeenCalled();
  });

  it("completes primeiro acesso then hydrates and navigates to app", async () => {
    const page = usePrimeiroAcessoPage();
    page.selectedAreaId.value = "20";

    await page.confirm();

    expect(completePrimeiroAcessoMock).toHaveBeenCalledWith({ areaId: 20 });
    expect(hydrateSessionMock).toHaveBeenCalledWith({ force: true });
    expect(replaceMock).toHaveBeenCalledWith(ROUTE_PATHS.APP);
    expect(dsNotifySuccessMock).toHaveBeenCalled();
  });

  it("does not navigate when hydration does not become ready", async () => {
    hydrateSessionMock.mockImplementation(async () => {
      isReady.value = false;
    });
    const page = usePrimeiroAcessoPage();
    page.selectedAreaId.value = "20";

    await page.confirm();

    expect(completePrimeiroAcessoMock).toHaveBeenCalled();
    expect(replaceMock).not.toHaveBeenCalled();
  });
});
