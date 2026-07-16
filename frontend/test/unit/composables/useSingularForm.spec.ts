import { describe, expect, it } from "vitest";

import {
  createEmptySingularForm,
  mapSingularFieldErrors,
  mapSingularToForm,
  useSingularForm
} from "@/composables/organization/useSingularForm";
import type { SingularResponse } from "@/types/organization/singular.types";

describe("useSingularForm", () => {
  it("creates empty form with default federation", () => {
    expect(createEmptySingularForm()).toEqual({
      federationId: 1,
      name: "",
      acronym: "",
      unimedCode: ""
    });
  });

  it("maps singular response to form model", () => {
    const singular: SingularResponse = {
      id: 10,
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001",
      status: "ACTIVE",
      createdAt: "2026-07-16T12:00:00Z",
      updatedAt: null
    };

    expect(mapSingularToForm(singular)).toEqual({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001"
    });
  });

  it("validates required create fields", () => {
    const { form, validateCreate } = useSingularForm();
    form.name = "";
    form.acronym = "";
    form.unimedCode = "";

    const result = validateCreate();
    expect(result.valid).toBe(false);
    expect(result.errors.name).toBeTruthy();
    expect(result.errors.acronym).toBeTruthy();
    expect(result.errors.unimedCode).toBeTruthy();
  });

  it("builds trimmed create request payload", () => {
    const { form, validateCreate, toCreateRequest } = useSingularForm();
    form.federationId = 1;
    form.name = "  Unimed Ceará  ";
    form.acronym = " UNI-CE ";
    form.unimedCode = " UC001 ";

    expect(validateCreate().valid).toBe(true);
    expect(toCreateRequest()).toEqual({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001"
    });
  });

  it("validates required update fields", () => {
    const { form, validateUpdate } = useSingularForm({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001"
    });
    form.name = "";
    form.acronym = "";
    form.unimedCode = "";

    const result = validateUpdate();
    expect(result.valid).toBe(false);
    expect(result.errors.name).toBeTruthy();
    expect(result.errors.acronym).toBeTruthy();
    expect(result.errors.unimedCode).toBeTruthy();
  });

  it("builds trimmed update request payload without federationId", () => {
    const { form, validateUpdate, toUpdateRequest } = useSingularForm({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: "UC001"
    });
    form.name = "  Unimed Fortaleza  ";
    form.acronym = " UNI-FOR ";
    form.unimedCode = " UC002 ";

    expect(validateUpdate().valid).toBe(true);
    expect(toUpdateRequest()).toEqual({
      name: "Unimed Fortaleza",
      acronym: "UNI-FOR",
      unimedCode: "UC002"
    });
  });

  it("maps API field validation errors", () => {
    expect(
      mapSingularFieldErrors([
        { field: "acronym", message: "Sigla já cadastrada" },
        { field: "unimedCode", message: "Código duplicado" }
      ])
    ).toEqual({
      acronym: "Sigla já cadastrada",
      unimedCode: "Código duplicado"
    });
  });
});
