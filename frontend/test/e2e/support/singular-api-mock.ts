import type { Page, Route } from "@playwright/test";

export type MockSingularStatus = "ACTIVE" | "INACTIVE";

export interface MockSingular {
  id: number;
  federationId: number;
  name: string;
  acronym: string;
  unimedCode: string;
  status: MockSingularStatus;
  createdAt: string;
  updatedAt: string | null;
  blockInactivation?: boolean;
}

export interface MockSingularStore {
  singulares: MockSingular[];
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

function parseIdFromPath(pathname: string): number | null {
  const match = pathname.match(/\/singulares\/(\d+)/);
  if (!match) {
    return null;
  }

  const id = Number(match[1]);
  return Number.isFinite(id) ? id : null;
}

function findByAcronym(
  store: MockSingularStore,
  acronym: string,
  excludeId?: number
): MockSingular | undefined {
  return store.singulares.find(
    item =>
      item.acronym.toLowerCase() === acronym.toLowerCase() &&
      item.id !== excludeId
  );
}

function filterSingulares(
  store: MockSingularStore,
  params: URLSearchParams
): MockSingular[] {
  let items = [...store.singulares];

  const status = params.get("status");
  if (status === "ACTIVE" || status === "INACTIVE") {
    items = items.filter(item => item.status === status);
  }

  const federationId = params.get("federationId");
  if (federationId) {
    items = items.filter(item => item.federationId === Number(federationId));
  }

  const name = params.get("name");
  if (name) {
    items = items.filter(item =>
      item.name.toLowerCase().includes(name.toLowerCase())
    );
  }

  const acronym = params.get("acronym");
  if (acronym) {
    items = items.filter(item =>
      item.acronym.toLowerCase().includes(acronym.toLowerCase())
    );
  }

  const unimedCode = params.get("unimedCode");
  if (unimedCode) {
    items = items.filter(item => item.unimedCode.includes(unimedCode));
  }

  const sort = params.get("sort") ?? "name,asc";
  const [field, direction] = sort.split(",");
  items.sort((left, right) => {
    const leftValue = String(left[field as keyof MockSingular] ?? "");
    const rightValue = String(right[field as keyof MockSingular] ?? "");
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

export function createMockSingularStore(
  seed: MockSingular[] = []
): MockSingularStore {
  const maxId = seed.reduce((current, item) => Math.max(current, item.id), 0);

  return {
    singulares: seed.map(item => ({ ...item })),
    nextId: maxId + 1
  };
}

export async function installSingularApiMock(
  page: Page,
  store: MockSingularStore
): Promise<void> {
  await page.route("**/api/v1/singulares**", async route => {
    const request = route.request();
    const url = new URL(request.url());
    const method = request.method();
    const pathname = url.pathname.replace(/\/api\/v1/, "");
    const singularId = parseIdFromPath(pathname);

    if (method === "GET" && pathname === "/singulares") {
      const filtered = filterSingulares(store, url.searchParams);
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
      singularId !== null &&
      !pathname.endsWith("/status")
    ) {
      const singular = store.singulares.find(item => item.id === singularId);
      if (!singular) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(404, "NOT_FOUND", "Singular não encontrada", pathname)
          )
        });
        return;
      }

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(singular))
      });
      return;
    }

    if (method === "POST" && pathname === "/singulares") {
      const body = await readJsonBody<{
        federationId: number;
        name: string;
        acronym: string;
        unimedCode: string;
      }>(route);

      if (!body) {
        await route.fulfill({ status: 400 });
        return;
      }

      if (findByAcronym(store, body.acronym)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            validationError(pathname, "Sigla já cadastrada", "acronym")
          )
        });
        return;
      }

      const created: MockSingular = {
        id: store.nextId++,
        federationId: body.federationId,
        name: body.name.trim(),
        acronym: body.acronym.trim(),
        unimedCode: body.unimedCode.trim(),
        status: "ACTIVE",
        createdAt: new Date().toISOString(),
        updatedAt: null
      };
      store.singulares.push(created);

      await route.fulfill({
        status: 201,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(created))
      });
      return;
    }

    if (method === "PUT" && singularId !== null) {
      const singular = store.singulares.find(item => item.id === singularId);
      const body = await readJsonBody<{
        name: string;
        acronym: string;
        unimedCode: string;
      }>(route);

      if (!singular || !body) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(404, "NOT_FOUND", "Singular não encontrada", pathname)
          )
        });
        return;
      }

      if (findByAcronym(store, body.acronym, singularId)) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            validationError(pathname, "Sigla já cadastrada", "acronym")
          )
        });
        return;
      }

      singular.name = body.name.trim();
      singular.acronym = body.acronym.trim();
      singular.unimedCode = body.unimedCode.trim();
      singular.updatedAt = new Date().toISOString();

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(singular))
      });
      return;
    }

    if (
      method === "PATCH" &&
      pathname.endsWith("/status") &&
      singularId !== null
    ) {
      const singular = store.singulares.find(item => item.id === singularId);
      const body = await readJsonBody<{ status: MockSingularStatus }>(route);

      if (!singular || !body) {
        await route.fulfill({
          status: 404,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(404, "NOT_FOUND", "Singular não encontrada", pathname)
          )
        });
        return;
      }

      if (body.status === "INACTIVE" && singular.blockInactivation === true) {
        await route.fulfill({
          status: 422,
          contentType: "application/json",
          body: JSON.stringify(
            errorResponse(
              422,
              "BUSINESS_RULE_VIOLATION",
              "Não é possível inativar singular com áreas ativas vinculadas",
              pathname
            )
          )
        });
        return;
      }

      singular.status = body.status;
      singular.updatedAt = new Date().toISOString();

      await route.fulfill({
        status: 200,
        contentType: "application/json",
        body: JSON.stringify(successEnvelope(singular))
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
