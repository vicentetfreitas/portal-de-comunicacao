export type ValidationRule = (value: unknown) => true | string;

export function required(message = "Campo obrigatório"): ValidationRule {
  return (value: unknown) => {
    if (value === null || value === undefined || value === "") {
      return message;
    }

    if (Array.isArray(value) && value.length === 0) {
      return message;
    }

    return true;
  };
}

export function email(message = "E-mail inválido"): ValidationRule {
  return (value: unknown) => {
    if (value === null || value === undefined || value === "") {
      return true;
    }

    const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const text =
      typeof value === "string"
        ? value
        : typeof value === "number"
          ? String(value)
          : "";
    return pattern.test(text) || message;
  };
}

export function minLength(min: number, message?: string): ValidationRule {
  return (value: unknown) => {
    if (value === null || value === undefined || value === "") {
      return true;
    }

    const text =
      typeof value === "string"
        ? value
        : typeof value === "number"
          ? String(value)
          : "";
    const length = text.length;
    return length >= min || (message ?? `Mínimo de ${min} caracteres`);
  };
}

export function maxLength(max: number, message?: string): ValidationRule {
  return (value: unknown) => {
    if (value === null || value === undefined || value === "") {
      return true;
    }

    const text =
      typeof value === "string"
        ? value
        : typeof value === "number"
          ? String(value)
          : "";
    const length = text.length;
    return length <= max || (message ?? `Máximo de ${max} caracteres`);
  };
}

export function runValidation(
  value: unknown,
  rules: readonly ValidationRule[]
): string | true {
  for (const rule of rules) {
    const result = rule(value);
    if (result !== true) {
      return result;
    }
  }

  return true;
}
