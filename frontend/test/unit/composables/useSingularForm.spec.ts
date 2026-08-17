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
      unimedCode: null,
      registroAns: ""
    });
  });

  it("maps singular response to form model", () => {
    const singular: SingularResponse = {
      id: 10,
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: 42,
      registroAns: "123456",
      status: "ACTIVE",
      createdAt: "2026-07-16T12:00:00Z",
      updatedAt: null
    };

    expect(mapSingularToForm(singular)).toEqual({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: 42,
      registroAns: "123456"
    });
  });

  it("validates required create fields", () => {
    const { form, validateCreate } = useSingularForm();
    form.name = "";
    form.acronym = "";
    form.unimedCode = null;
    form.registroAns = "";

    const result = validateCreate();
    expect(result.valid).toBe(false);
    expect(result.errors.name).toBeTruthy();
    expect(result.errors.acronym).toBeTruthy();
    expect(result.errors.unimedCode).toBeTruthy();
    expect(result.errors.registroAns).toBeTruthy();
  });

  it("builds create request payload", () => {
    const { form, validateCreate, toCreateRequest } = useSingularForm();
    form.federationId = 1;
    form.name = "  Unimed Ceará  ";
    form.acronym = " UNI-CE ";
    form.unimedCode = 42;
    form.registroAns = "12345678901234567890";

    expect(validateCreate().valid).toBe(true);
    expect(toCreateRequest()).toEqual({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: 42,
      registroAns: "12345678901234567890"
    });
  });

  it("validates required update fields", () => {
    const { form, validateUpdate } = useSingularForm({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: 42,
      registroAns: "123456"
    });
    form.name = "";
    form.acronym = "";
    form.unimedCode = null;
    form.registroAns = "";

    const result = validateUpdate();
    expect(result.valid).toBe(false);
    expect(result.errors.name).toBeTruthy();
    expect(result.errors.acronym).toBeTruthy();
    expect(result.errors.unimedCode).toBeTruthy();
    expect(result.errors.registroAns).toBeTruthy();
  });

  it("builds update request payload without federationId", () => {
    const { form, validateUpdate, toUpdateRequest } = useSingularForm({
      federationId: 1,
      name: "Unimed Ceará",
      acronym: "UNI-CE",
      unimedCode: 42,
      registroAns: "123456"
    });
    form.name = "  Unimed Fortaleza  ";
    form.acronym = " UNI-FOR ";
    form.unimedCode = 43;
    form.registroAns = " 654321 ";

    expect(validateUpdate().valid).toBe(true);
    expect(toUpdateRequest()).toEqual({
      name: "Unimed Fortaleza",
      acronym: "UNI-FOR",
      unimedCode: 43,
      registroAns: "654321"
    });
  });

  it("rejects unimedCode outside 1-999", () => {
    const { form, validateCreate } = useSingularForm();
    form.federationId = 1;
    form.name = "Unimed Ceará";
    form.acronym = "UNI-CE";
    form.unimedCode = 1000;
    form.registroAns = "123456";

    const result = validateCreate();
    expect(result.valid).toBe(false);
    expect(result.errors.unimedCode).toBeTruthy();
  });

  it("rejects registroAns exceeding 20 characters", () => {
    const { form, validateCreate } = useSingularForm();
    form.federationId = 1;
    form.name = "Unimed Ceará";
    form.acronym = "UNI-CE";
    form.unimedCode = 42;
    form.registroAns = "123456789012345678901";

    const result = validateCreate();
    expect(result.valid).toBe(false);
    expect(result.errors.registroAns).toBeTruthy();
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
