import { reactive, type UnwrapNestedRefs } from "vue";

import { DEFAULT_FEDERATION_ID } from "@/config/organization";
import { useFormValidation } from "@/composables/useFormValidation";
import type {
  CreateSingularRequest,
  SingularResponse,
  UpdateSingularRequest
} from "@/types/organization/singular.types";
import type { FieldValidationError } from "@/types/api";

export interface SingularFormModel {
  federationId: number | null;
  name: string;
  acronym: string;
  unimedCode: string;
}

export function createEmptySingularForm(): SingularFormModel {
  return {
    federationId: DEFAULT_FEDERATION_ID,
    name: "",
    acronym: "",
    unimedCode: ""
  };
}

export function mapSingularToForm(singular: SingularResponse): SingularFormModel {
  return {
    federationId: singular.federationId,
    name: singular.name,
    acronym: singular.acronym,
    unimedCode: singular.unimedCode
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

  function validateCreate(): { valid: boolean; errors: Record<string, string> } {
    return validateForm({
      federationId: {
        value: form.federationId,
        rules: [required("Federação é obrigatória")]
      },
      name: {
        value: form.name,
        rules: [required("Nome é obrigatório"), maxLength(200)]
      },
      acronym: {
        value: form.acronym,
        rules: [required("Sigla é obrigatória"), maxLength(30)]
      },
      unimedCode: {
        value: form.unimedCode,
        rules: [required("Código Unimed é obrigatório"), maxLength(20)]
      }
    });
  }

  function validateUpdate(): { valid: boolean; errors: Record<string, string> } {
    return validateForm({
      name: {
        value: form.name,
        rules: [required("Nome é obrigatório"), maxLength(200)]
      },
      acronym: {
        value: form.acronym,
        rules: [required("Sigla é obrigatória"), maxLength(30)]
      },
      unimedCode: {
        value: form.unimedCode,
        rules: [required("Código Unimed é obrigatório"), maxLength(20)]
      }
    });
  }

  function toCreateRequest(): CreateSingularRequest {
    if (form.federationId === null) {
      throw new Error("federationId is required for create");
    }

    return {
      federationId: form.federationId,
      name: form.name.trim(),
      acronym: form.acronym.trim(),
      unimedCode: form.unimedCode.trim()
    };
  }

  function toUpdateRequest(): UpdateSingularRequest {
    return {
      name: form.name.trim(),
      acronym: form.acronym.trim(),
      unimedCode: form.unimedCode.trim()
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
