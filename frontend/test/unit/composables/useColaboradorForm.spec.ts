import { describe, expect, it } from "vitest";

import {
  createEmptyColaboradorForm,
  mapColaboradorFieldErrors,
  mapColaboradorToForm,
  useColaboradorForm
} from "@/composables/organization/useColaboradorForm";
import type { ColaboradorResponse } from "@/types/organization/colaborador.types";

describe("useColaboradorForm", () => {
  it("creates empty form with default federation and no organizational binding", () => {
    expect(createEmptyColaboradorForm()).toEqual({
      federationId: 1,
      singularId: null,
      areaId: null,
      teamId: null,
      managerId: null,
      name: "",
      email: "",
      zimbraId: "",
      biography: ""
    });
  });

  it("maps colaborador response to form model", () => {
    const colaborador: ColaboradorResponse = {
      id: 10,
      federationId: 1,
      singularId: 2,
      areaId: 3,
      teamId: 4,
      managerId: null,
      name: "Maria Souza",
      email: "maria.souza@example.com",
      zimbraId: "maria.souza",
      biography: null,
      status: "ACTIVE",
      birthDate: null,
      hireDate: null,
      lastAccessAt: null,
      createdAt: "2026-07-16T12:00:00Z",
      updatedAt: null
    };

    expect(mapColaboradorToForm(colaborador)).toEqual({
      federationId: 1,
      singularId: 2,
      areaId: 3,
      teamId: 4,
      managerId: null,
      name: "Maria Souza",
      email: "maria.souza@example.com",
      zimbraId: "maria.souza",
      biography: ""
    });
  });

  it("rejects create without name, email and zimbraId", () => {
    const { form, validateCreate } = useColaboradorForm();
    form.name = "";
    form.email = "";
    form.zimbraId = "";

    const result = validateCreate();

    expect(result.valid).toBe(false);
    expect(result.errors.name).toBeTruthy();
    expect(result.errors.email).toBeTruthy();
    expect(result.errors.zimbraId).toBeTruthy();
  });

  it("rejects create with invalid email", () => {
    const { form, validateCreate } = useColaboradorForm();
    form.name = "Maria Souza";
    form.email = "not-an-email";
    form.zimbraId = "maria.souza";

    const result = validateCreate();

    expect(result.valid).toBe(false);
    expect(result.errors.email).toBeTruthy();
  });

  it("builds create request with organizational binding and trimmed fields", () => {
    const { form, validateCreate, toCreateRequest } = useColaboradorForm();
    form.singularId = 2;
    form.areaId = 3;
    form.teamId = 4;
    form.name = "  Maria Souza  ";
    form.email = "maria.souza@example.com";
    form.zimbraId = " maria.souza ";

    expect(validateCreate().valid).toBe(true);
    expect(toCreateRequest()).toEqual({
      federationId: 1,
      name: "Maria Souza",
      email: "maria.souza@example.com",
      zimbraId: "maria.souza",
      singularId: 2,
      areaId: 3,
      teamId: 4
    });
  });

  it("builds create request without optional organizational binding", () => {
    const { form, validateCreate, toCreateRequest } = useColaboradorForm();
    form.name = "Maria Souza";
    form.email = "maria.souza@example.com";
    form.zimbraId = "maria.souza";

    expect(validateCreate().valid).toBe(true);
    expect(toCreateRequest()).toEqual({
      federationId: 1,
      name: "Maria Souza",
      email: "maria.souza@example.com",
      zimbraId: "maria.souza"
    });
  });

  it("maps API field validation errors", () => {
    expect(
      mapColaboradorFieldErrors([
        { field: "email", message: "E-mail já cadastrado" },
        { field: "zimbraId", message: "Identificador Zimbra já cadastrado" }
      ])
    ).toEqual({
      email: "E-mail já cadastrado",
      zimbraId: "Identificador Zimbra já cadastrado"
    });
  });
});
