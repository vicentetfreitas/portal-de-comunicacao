import { reactive, type UnwrapNestedRefs } from "vue";

import { DEFAULT_FEDERATION_ID } from "@/config/organization";
import { useFormValidation } from "@/composables/useFormValidation";
import type {
  CreateSingularRequest,
  SingularResponse,
  UpdateSingularRequest
} from "@/types/organization/singular.types";
import type { FieldValidationError } from "@/types/api";
import type { ValidationRule } from "@/utils/validation/rules";

export interface SingularFormModel {
  federationId: number | null;
  name: string;
  acronym: string;
  unimedCode: number | null;
  registroAns: string;
}

function unimedCodeRange(
  message = "Código Unimed deve estar entre 1 e 999"
): ValidationRule {
  return (value: unknown) => {
    if (value === null || value === undefined || value === "") {
      return true;
    }

    const numeric =
      typeof value === "number" ? value : Number(String(value).trim());
    if (!Number.isInteger(numeric) || numeric < 1 || numeric > 999) {
      return message;
    }

    return true;
  };
}

export function createEmptySingularForm(): SingularFormModel {
  return {
    federationId: DEFAULT_FEDERATION_ID,
    name: "",
    acronym: "",
    unimedCode: null,
    registroAns: ""
  };
}

export function mapSingularToForm(
  singular: SingularResponse
): SingularFormModel {
  return {
    federationId: singular.federationId,
    name: singular.name,
    acronym: singular.acronym,
    unimedCode: singular.unimedCode,
    registroAns: singular.registroAns
  };
}

export function mapSingularFieldErrors(
  errors: readonly FieldValidationError[]
): Record<string, string> {
  const mapped: Record<string, string> = {};

  for (const item of errors) {
    mapped[item.field] = item.message;
  }

  return mapped;
}

export function useSingularForm(initial?: Partial<SingularFormModel>) {
  const { validateForm, required, maxLength } = useFormValidation();

  const form: UnwrapNestedRefs<SingularFormModel> = reactive({
    ...createEmptySingularForm(),
    ...initial
  });

  const registroAnsRules = [
    required("Registro ANS é obrigatório"),
    maxLength(20, "Registro ANS deve ter no máximo 20 caracteres")
  ];

  function validateCreate(): {
    valid: boolean;
    errors: Record<string, string>;
  } {
    return validateForm({
      federationId: {
        value: form.federationId,
        rules: [required("Federação é obrigatória")]
      },
      name: {
        value: form.name,
        rules: [required("Nome é obrigatório")]
      },
      acronym: {
        value: form.acronym,
        rules: [required("Sigla é obrigatória")]
      },
      unimedCode: {
        value: form.unimedCode,
        rules: [required("Código Unimed é obrigatório"), unimedCodeRange()]
      },
      registroAns: {
        value: form.registroAns,
        rules: registroAnsRules
      }
    });
  }

  function validateUpdate(): {
    valid: boolean;
    errors: Record<string, string>;
  } {
    return validateForm({
      name: {
        value: form.name,
        rules: [required("Nome é obrigatório")]
      },
      acronym: {
        value: form.acronym,
        rules: [required("Sigla é obrigatória")]
      },
      unimedCode: {
        value: form.unimedCode,
        rules: [required("Código Unimed é obrigatório"), unimedCodeRange()]
      },
      registroAns: {
        value: form.registroAns,
        rules: registroAnsRules
      }
    });
  }

  function toCreateRequest(): CreateSingularRequest {
    if (form.federationId === null) {
      throw new Error("federationId is required for create");
    }
    if (form.unimedCode === null) {
      throw new Error("unimedCode is required for create");
    }

    return {
      federationId: form.federationId,
      name: form.name.trim(),
      acronym: form.acronym.trim(),
      unimedCode: form.unimedCode,
      registroAns: form.registroAns.trim()
    };
  }

  function toUpdateRequest(): UpdateSingularRequest {
    if (form.unimedCode === null) {
      throw new Error("unimedCode is required for update");
    }

    return {
      name: form.name.trim(),
      acronym: form.acronym.trim(),
      unimedCode: form.unimedCode,
      registroAns: form.registroAns.trim()
    };
  }

  function reset(next?: Partial<SingularFormModel>): void {
    Object.assign(form, createEmptySingularForm(), next);
  }

  return {
    form,
    validateCreate,
    validateUpdate,
    toCreateRequest,
    toUpdateRequest,
    reset
  };
}
