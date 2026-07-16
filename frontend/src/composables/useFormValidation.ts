import {
  email,
  maxLength,
  minLength,
  required,
  runValidation,
  type ValidationRule
} from "@/utils/validation/rules";

export interface FormFieldValidation {
  value: unknown;
  rules: readonly ValidationRule[];
}

export function useFormValidation() {
  function validateField(
    value: unknown,
    rules: readonly ValidationRule[]
  ): string | true {
    return runValidation(value, rules);
  }

  function validateForm(fields: Record<string, FormFieldValidation>): {
    valid: boolean;
    errors: Record<string, string>;
  } {
    const errors: Record<string, string> = {};

    for (const [fieldName, field] of Object.entries(fields)) {
      const result = validateField(field.value, field.rules);
      if (result !== true) {
        errors[fieldName] = result;
      }
    }

    return {
      valid: Object.keys(errors).length === 0,
      errors
    };
  }

  return {
    validateField,
    validateForm,
    required,
    email,
    minLength,
    maxLength
  };
}
