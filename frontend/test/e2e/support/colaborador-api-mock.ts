import type { Page, Route } from "@playwright/test";

export type MockColaboradorStatus = "ACTIVE" | "INACTIVE";

export interface MockColaborador {
  id: number;
  federationId: number;
  singularId: number | null;
  areaId: number | null;
  teamId: number | null;
  managerId: number | null;
  name: string;
  email: string;
  zimbraId: string;
  biography: string | null;
  status: MockColaboradorStatus;
  birthDate: string | null;
  hireDate: string | null;
  lastAccessAt: string | null;
  createdAt: string;
  updatedAt: string | null;
}

export interface MockColaboradorStore {
  colaboradores: MockColaborador[];
  nextId: number;
}

function successEnvelope<T>(data: T) {
  return {
    timestamp: new Date().toISOString(),
    success: true,
    data
  };
}

function businessRuleError(path: string, message: string) {
  return {
    timestamp: new Date().toISOString(),
    status: 422,
    error: "BUSINESS_RULE_VIOLATION",
    message,
    path
  };
}

function errorResponse(
  status: number,
  error: string,
  message: string,
  path: string
) {
  return {
    timestamp: new Date().toISOString(),
    status,
    error,
    message,
    path
  };
}

function parseIdFromPath(pathname: string): number | null {
  const match = pathname.match(/\/colaboradores\/(\d+)/);
  if (!match) {
    return null;
  }

  const id = Number(match[1]);
  return Number.isFinite(id) ? id : null;
}

function findByEmail(
  store: MockColaboradorStore,
  email: string,
  excludeId?: number
): MockColaborador | undefined {
  return store.colaboradores.find(
    item =>
      item.email.toLowerCase() === email.toLowerCase() && item.id !== excludeId
  );
}

function findByZimbraId(
  store: MockColaboradorStore,
  zimbraId: string,
  excludeId?: number
): MockColaborador | undefined {
  return store.colaboradores.find(
    item => item.zimbraId === zimbraId && item.id !== excludeId
  );
}

/** RN-008 — não inativar colaborador com subordinados ativos. */
function hasActiveSubordinates(
  store: MockColaboradorStore,
  managerId: number
): boolean {
  return store.colaboradores.some(
    item => item.managerId === managerId && item.status === "ACTIVE"
  );
}

function filterColaboradores(
  store: MockColaboradorStore,
  params: URLSearchParams
): MockColaborador[] {
  let items = [...store.colaboradores];

  const status = params.get("status");
  if (status === "ACTIVE" || status === "INACTIVE") {
    items = items.filter(item => item.status === status);
  }

  const singularId = params.get("singularId");
  if (singularId) {
    items = items.filter(item => item.singularId === Number(singularId));
  }

  const areaId = params.get("areaId");
  if (areaId) {
    items = items.filter(item => item.areaId === Number(areaId));
  }

  const teamId = params.get("teamId");
  if (teamId) {
    items = items.filter(item => item.teamId === Number(teamId));
  }

  const name = params.get("name");
  if (name) {
    items = items.filter(item =>
      item.name.toLowerCase().includes(name.toLowerCase())
    );
  }

  const email = params.get("email");
  if (email) {
    items = items.filter(item =>
      item.email.toLowerCase().includes(email.toLowerCase())
    );
  }

  const sort = params.get("sort") ?? "name,asc";
  const [field, direction] = sort.split(",");
  items.sort((left, right) => {
    const leftValue = String(left[field as keyof MockColaborador] ?? "");
    const rightValue = String(right[field as keyof MockColaborador] ?? "");
    const comparison = leftValue.localeCompare(rightValue, "pt-BR");
    return direction === "desc" ? -comparison : comparison;
  });

  return items;
}

async function readJsonBody<T>(route: Route): Promise<T | undefined> {
  try {
    return (await route.request().postDataJSON()) as T;
  } catch {
    return undefined;
  }
}

export function createMockColaboradorStore(
  seed: MockColaborador[] = []
): MockColaboradorStore {
  const maxId = seed.reduce((current, item) => Math.max(current, item.id), 0);

  return {
    colaboradores: seed.map(item => ({ ...item })),
    nextId: maxId + 1
  };
}

export async function installColaboradorApiMock(
  page: Page,
  store: MockColaboradorStore
): Promise<void> {
  await page.route("**/api/v1/colaboradores**", async route => {
    const request = route.request();
    const url = new URL(request.url());
    const method = request.method();
    const pathname = url.pathname.replace(/\/api\/v1/, "");
    const colaboradorId = parseIdFromPath(pathname);

    if (method === "GET" && pathname === "/colaboradores") {
      const filtered = filterColaboradores(store, url.searchParams);
      const pageNumber = Number(url.searchParams.get("page") ?? "0");
      const size = Number(url.searchParams.get("size") ?? "10");
      const start = pageNumber * size;
      const content = filtered.slice(start, start + size);
      const totalElements = filtered.length;
      const totalPages = Math.max(Math.ceil(totalElements / size), 1);

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(
          successEnvelope({
            content,
            page: pageNumber,
            size,
            totalElements,
            totalPages,
            first: pageNumber === 0,
            last: pageNumber >= totalPages - 1
          })
        )
      });
      return;
    }

    if (
      method === "GET" &&
      colaboradorId !== null &&
      !pathname.endsWith("/status")
    ) {
      const colaborador = store.colaboradores.find(
        item => item.id === colaboradorId
      );
      if (!colaborador) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(
              404,
              "NOT_FOUND",
              "Colaborador não encontrado",
              pathname
            )
          )
        });
        return;
      }

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(colaborador))
      });
      return;
    }

    if (method === "POST" && pathname === "/colaboradores") {
      const body = await readJsonBody<{
        federationId: number;
        singularId?: number;
        areaId?: number;
        teamId?: number;
        managerId?: number;
        name: string;
        email: string;
        zimbraId: string;
        biography?: string;
      }>(route);

      if (!body) {
        await route.fulfill({ status: 400 });
        return;
      }

      if (findByEmail(store, body.email)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            businessRuleError(pathname, "Já existe colaborador com este e-mail")
          )
        });
        return;
      }

      if (findByZimbraId(store, body.zimbraId)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            businessRuleError(
              pathname,
              "Já existe colaborador com este identificador Zimbra"
            )
          )
        });
        return;
      }

      const created: MockColaborador = {
        id: store.nextId++,
        federationId: body.federationId,
        singularId: body.singularId ?? null,
        areaId: body.areaId ?? null,
        teamId: body.teamId ?? null,
        managerId: body.managerId ?? null,
        name: body.name.trim(),
        email: body.email.trim(),
        zimbraId: body.zimbraId.trim(),
        biography: body.biography?.trim() || null,
        status: "ACTIVE",
        birthDate: null,
        hireDate: null,
        lastAccessAt: null,
        createdAt: new Date().toISOString(),
        updatedAt: null
      };
      store.colaboradores.push(created);

      await route.fulfill({
        status: 201,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(created))
      });
      return;
    }

    if (method === "PUT" && colaboradorId !== null) {
      const colaborador = store.colaboradores.find(
        item => item.id === colaboradorId
      );
      const body = await readJsonBody<{
        name: string;
        singularId?: number;
        areaId?: number;
        teamId?: number;
        managerId?: number;
        zimbraId: string;
        biography?: string;
      }>(route);

      if (!colaborador || !body) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(
              404,
              "NOT_FOUND",
              "Colaborador não encontrado",
              pathname
            )
          )
        });
        return;
      }

      if (findByZimbraId(store, body.zimbraId, colaboradorId)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            businessRuleError(
              pathname,
              "Já existe colaborador com este identificador Zimbra"
            )
          )
        });
        return;
      }

      colaborador.name = body.name.trim();
      colaborador.singularId = body.singularId ?? null;
      colaborador.areaId = body.areaId ?? null;
      colaborador.teamId = body.teamId ?? null;
      colaborador.managerId = body.managerId ?? null;
      colaborador.zimbraId = body.zimbraId.trim();
      colaborador.biography = body.biography?.trim() || null;
      colaborador.updatedAt = new Date().toISOString();

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(colaborador))
      });
      return;
    }

    if (
      method === "PATCH" &&
      pathname.endsWith("/status") &&
      colaboradorId !== null
    ) {
      const colaborador = store.colaboradores.find(
        item => item.id === colaboradorId
      );
      const body = await readJsonBody<{ status: MockColaboradorStatus }>(route);

      if (!colaborador || !body) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(
              404,
              "NOT_FOUND",
              "Colaborador não encontrado",
              pathname
            )
          )
        });
        return;
      }

      if (
        body.status === "INACTIVE" &&
        hasActiveSubordinates(store, colaboradorId)
      ) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            businessRuleError(
              pathname,
              "Colaborador possui subordinados ativos"
            )
          )
        });
        return;
      }

      colaborador.status = body.status;
      colaborador.updatedAt = new Date().toISOString();

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(colaborador))
      });
      return;
    }

    await route.fulfill({
      status: 404,
      contentType: "application/json",
      body: JSON.stringify(
        errorResponse(404, "NOT_FOUND", "Rota não encontrada", pathname)
      )
    });
  });
}

/**
 * Singular/Área/Equipe filter/organizational-link options — ColaboradorForm
 * (create/edit) and ColaboradorFilters (listagem) both load these via
 * their own already-approved list endpoints. Empty by default (deterministic,
 * no dependency on a real backend); pass content to exercise cascading
 * selects if a scenario needs it.
 */
export async function installColaboradorOrgOptionsMock(
  page: Page,
  options: {
    singulares?: { id: number; name: string; acronym: string }[];
    areas?: { id: number; name: string; acronym: string }[];
    equipes?: { id: number; name: string }[];
  } = {}
): Promise<void> {
  await page.route("**/api/v1/singulares**", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify(
        successEnvelope({
          content: options.singulares ?? [],
          totalElements: options.singulares?.length ?? 0
        })
      )
    })
  );
  await page.route("**/api/v1/areas**", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify(
        successEnvelope({
          content: options.areas ?? [],
          totalElements: options.areas?.length ?? 0
        })
      )
    })
  );
  await page.route("**/api/v1/equipes**", route =>
    route.fulfill({
      status: 200,
      contentType: "application/json",
      body: JSON.stringify(
        successEnvelope({
          content: options.equipes ?? [],
          totalElements: options.equipes?.length ?? 0
        })
      )
    })
  );
}
