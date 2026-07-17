import { describe, expect, it } from "vitest";

import {
  createEmptyEquipeForm,
  useEquipeForm
} from "@/composables/organization/useEquipeForm";

describe("useEquipeForm", () => {
  it("rejects create without area and name", () => {
    const { form, validateCreate } = useEquipeForm();
    Object.assign(form, createEmptyEquipeForm());

    const result = validateCreate();

    expect(result.valid).toBe(false);
    expect(result.errors.areaId).toBeDefined();
    expect(result.errors.name).toBeDefined();
  });

  it("builds create request with optional fields", () => {
    const { validateCreate, toCreateRequest } = useEquipeForm({
      areaId: 5,
      name: " Equipe Beta ",
      description: "Descrição",
      leaderId: 99
    });

    expect(validateCreate().valid).toBe(true);
    expect(toCreateRequest()).toEqual({
      areaId: 5,
      name: "Equipe Beta",
      description: "Descrição",
      leaderId: 99
    });
  });
});
