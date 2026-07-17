import { reactive, type UnwrapNestedRefs } from "vue";

import { useFormValidation } from "@/composables/useFormValidation";
import type { FieldValidationError } from "@/types/api";
import type {
  CreateEquipeRequest,
  EquipeResponse,
  UpdateEquipeRequest
} from "@/types/organization/equipe.types";

export interface EquipeFormModel {
  areaId: number | null;
  name: string;
  description: string;
  leaderId: number | null;
}

export function createEmptyEquipeForm(): EquipeFormModel {
  return {
    areaId: null,
    name: "",
    description: "",
    leaderId: null
  };
}

export function mapEquipeToForm(equipe: EquipeResponse): EquipeFormModel {
  return {
    areaId: equipe.areaId,
    name: equipe.name,
    description: equipe.description ?? "",
    leaderId: equipe.leaderId
  };
}

export function mapEquipeFieldErrors(
  errors: readonly FieldValidationError[]
): Record<string, string> {
  const mapped: Record<string, string> = {};

  for (const item of errors) {
    mapped[item.field] = item.message;
  }

  return mapped;
}

export function useEquipeForm(initial?: Partial<EquipeFormModel>) {
  const { validateForm, required, maxLength } = useFormValidation();

  const form: UnwrapNestedRefs<EquipeFormModel> = reactive({
    ...createEmptyEquipeForm(),
    ...initial
  });

  function validateCreate(): {
    valid: boolean;
    errors: Record<string, string>;
  } {
    return validateForm({
      areaId: {
        value: form.areaId,
        rules: [required("Área é obrigatória")]
      },
      name: {
        value: form.name,
        rules: [required("Nome é obrigatório"), maxLength(200)]
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
        rules: [required("Nome é obrigatório"), maxLength(200)]
      }
    });
  }

  function toCreateRequest(): CreateEquipeRequest {
    if (form.areaId === null) {
      throw new Error("areaId is required for create");
    }

    const request: CreateEquipeRequest = {
      areaId: form.areaId,
      name: form.name.trim()
    };

    const description = form.description.trim();
    if (description.length > 0) {
      request.description = description;
    }
    if (form.leaderId !== null) {
      request.leaderId = form.leaderId;
    }

    return request;
  }

  function toUpdateRequest(): UpdateEquipeRequest {
    const request: UpdateEquipeRequest = {
      name: form.name.trim()
    };

    const description = form.description.trim();
    if (description.length > 0) {
      request.description = description;
    }
    if (form.leaderId !== null) {
      request.leaderId = form.leaderId;
    }

    return request;
  }

  function reset(next?: Partial<EquipeFormModel>): void {
    Object.assign(form, createEmptyEquipeForm(), next);
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
