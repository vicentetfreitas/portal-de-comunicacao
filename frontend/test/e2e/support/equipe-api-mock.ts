import type { Page, Route } from "@playwright/test";

export type MockEquipeStatus = "ACTIVE" | "INACTIVE";

export interface MockArea {
  id: number;
  singularId: number;
  parentAreaId: number | null;
  name: string;
  acronym: string;
  description: string | null;
  managerId: number | null;
  status: MockEquipeStatus;
  createdAt: string;
  updatedAt: string | null;
}

export interface MockEquipe {
  id: number;
  areaId: number;
  name: string;
  description: string | null;
  leaderId: number | null;
  status: MockEquipeStatus;
  createdAt: string;
  updatedAt: string | null;
  blockInactivation?: boolean;
}

export interface MockEquipeStore {
  equipes: MockEquipe[];
  areas: MockArea[];
  nextId: number;
}

function successEnvelope<T>(data: T) {
  return {
    timestamp: new Date().toISOString(),
    success: true,
    data
  };
}

function validationError(path: string, message: string, field: string) {
  return {
    timestamp: new Date().toISOString(),
    status: 422,
    error: "VALIDATION_ERROR",
    message,
    path,
    errors: [{ field, message }]
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

function parseEquipeId(pathname: string): number | null {
  const match = pathname.match(/\/equipes\/(\d+)/);
  if (!match) {
    return null;
  }

  const id = Number(match[1]);
  return Number.isFinite(id) ? id : null;
}

function findDuplicateNameInArea(
  store: MockEquipeStore,
  name: string,
  areaId: number,
  excludeId?: number
): MockEquipe | undefined {
  return store.equipes.find(
    item =>
      item.areaId === areaId &&
      item.status === "ACTIVE" &&
      item.name.toLowerCase() === name.trim().toLowerCase() &&
      item.id !== excludeId
  );
}

function filterEquipes(
  store: MockEquipeStore,
  params: URLSearchParams
): MockEquipe[] {
  let items = [...store.equipes];

  const status = params.get("status");
  if (status === "ACTIVE" || status === "INACTIVE") {
    items = items.filter(item => item.status === status);
  }

  const areaId = params.get("areaId");
  if (areaId) {
    items = items.filter(item => item.areaId === Number(areaId));
  }

  const name = params.get("name");
  if (name) {
    items = items.filter(item =>
      item.name.toLowerCase().includes(name.toLowerCase())
    );
  }

  const sort = params.get("sort") ?? "name,asc";
  const [field, direction] = sort.split(",");
  items.sort((left, right) => {
    const leftValue = String(left[field as keyof MockEquipe] ?? "");
    const rightValue = String(right[field as keyof MockEquipe] ?? "");
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

const DEFAULT_AREAS: MockArea[] = [
  {
    id: 10,
    singularId: 1,
    parentAreaId: null,
    name: "Área Comunicação",
    acronym: "COM",
    description: null,
    managerId: null,
    status: "ACTIVE",
    createdAt: "2026-07-17T12:00:00Z",
    updatedAt: null
  }
];

export function createMockEquipeStore(
  seed: MockEquipe[] = [],
  areas: MockArea[] = DEFAULT_AREAS
): MockEquipeStore {
  const maxId = seed.reduce((current, item) => Math.max(current, item.id), 0);

  return {
    equipes: seed.map(item => ({ ...item })),
    areas: areas.map(item => ({ ...item })),
    nextId: maxId + 1
  };
}

export async function installEquipeApiMock(
  page: Page,
  store: MockEquipeStore
): Promise<void> {
  await page.route("**/api/v1/areas**", async route => {
    const request = route.request();
    const url = new URL(request.url());
    const method = request.method();
    const pathname = url.pathname.replace(/\/api\/v1/, "");

    if (method === "GET" && pathname === "/areas") {
      const pageNumber = Number(url.searchParams.get("page") ?? "0");
      const size = Number(url.searchParams.get("size") ?? "100");
      const content = store.areas.slice(
        pageNumber * size,
        pageNumber * size + size
      );

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(
          successEnvelope({
            content,
            page: pageNumber,
            size,
            totalElements: store.areas.length,
            totalPages: 1,
            first: true,
            last: true
          })
        )
      });
      return;
    }

    await route.fulfill({ status: 404 });
  });

  await page.route("**/api/v1/equipes**", async route => {
    const request = route.request();
    const url = new URL(request.url());
    const method = request.method();
    const pathname = url.pathname.replace(/\/api\/v1/, "");
    const equipeId = parseEquipeId(pathname);

    if (method === "GET" && pathname === "/equipes") {
      const filtered = filterEquipes(store, url.searchParams);
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
      equipeId !== null &&
      !pathname.endsWith("/status")
    ) {
      const equipe = store.equipes.find(item => item.id === equipeId);
      if (!equipe) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(404, "NOT_FOUND", "Equipe não encontrada", pathname)
          )
        });
        return;
      }

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(equipe))
      });
      return;
    }

    if (method === "POST" && pathname === "/equipes") {
      const body = await readJsonBody<{
        areaId: number;
        name: string;
        description?: string;
        leaderId?: number;
      }>(route);

      if (!body) {
        await route.fulfill({ status: 400 });
        return;
      }

      if (findDuplicateNameInArea(store, body.name, body.areaId)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            validationError(
              pathname,
              "Já existe equipe ativa com este nome na área",
              "name"
            )
          )
        });
        return;
      }

      const created: MockEquipe = {
        id: store.nextId++,
        areaId: body.areaId,
        name: body.name.trim(),
        description: body.description?.trim() ?? null,
        leaderId: body.leaderId ?? null,
        status: "ACTIVE",
        createdAt: new Date().toISOString(),
        updatedAt: null
      };
      store.equipes.push(created);

      await route.fulfill({
        status: 201,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(created))
      });
      return;
    }

    if (method === "PUT" && equipeId !== null) {
      const equipe = store.equipes.find(item => item.id === equipeId);
      const body = await readJsonBody<{
        name: string;
        description?: string;
        leaderId?: number;
      }>(route);

      if (!equipe || !body) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(404, "NOT_FOUND", "Equipe não encontrada", pathname)
          )
        });
        return;
      }

      if (findDuplicateNameInArea(store, body.name, equipe.areaId, equipeId)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            validationError(
              pathname,
              "Já existe equipe ativa com este nome na área",
              "name"
            )
          )
        });
        return;
      }

      equipe.name = body.name.trim();
      equipe.description = body.description?.trim() ?? null;
      equipe.leaderId = body.leaderId ?? null;
      equipe.updatedAt = new Date().toISOString();

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(equipe))
      });
      return;
    }

    if (
      method === "PATCH" &&
      pathname.endsWith("/status") &&
      equipeId !== null
    ) {
      const equipe = store.equipes.find(item => item.id === equipeId);
      const body = await readJsonBody<{ status: MockEquipeStatus }>(route);

      if (!equipe || !body) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(404, "NOT_FOUND", "Equipe não encontrada", pathname)
          )
        });
        return;
      }

      if (body.status === "INACTIVE" && equipe.blockInactivation === true) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(
              422,
              "BUSINESS_RULE_VIOLATION",
              "Equipe possui colaboradores ativos vinculados",
              pathname
            )
          )
        });
        return;
      }

      equipe.status = body.status;
      equipe.updatedAt = new Date().toISOString();

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(equipe))
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
