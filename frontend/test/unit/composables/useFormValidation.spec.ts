import { describe, expect, it } from "vitest";

import { useFormValidation } from "@/composables/useFormValidation";

describe("useFormValidation", () => {
  const { validateField, validateForm, required, email } = useFormValidation();

  it("validates required fields", () => {
    expect(validateField("", [required()])).toBe("Campo obrigatório");
    expect(validateField("value", [required()])).toBe(true);
  });

  it("validates email format", () => {
    expect(validateField("invalid", [email()])).toBe("E-mail inválido");
    expect(validateField("user@example.com", [email()])).toBe(true);
  });

  it("validates form object with multiple fields", () => {
    const result = validateForm({
      name: { value: "", rules: [required()] },
      mail: { value: "invalid", rules: [email()] }
    });

    expect(result.valid).toBe(false);
    expect(result.errors.name).toBe("Campo obrigatório");
    expect(result.errors.mail).toBe("E-mail inválido");
  });
});
