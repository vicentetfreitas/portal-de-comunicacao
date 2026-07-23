import { reactive, type UnwrapNestedRefs } from "vue";

import { DEFAULT_FEDERATION_ID } from "@/config/organization";
import { useFormValidation } from "@/composables/useFormValidation";
import type { FieldValidationError } from "@/types/api";
import type {
  ColaboradorResponse,
  CreateColaboradorRequest,
  UpdateColaboradorRequest
} from "@/types/organization/colaborador.types";

export interface ColaboradorFormModel {
  federationId: number;
  singularId: number | null;
  areaId: number | null;
  teamId: number | null;
  managerId: number | null;
  name: string;
  email: string;
  zimbraId: string;
  biography: string;
}

export function createEmptyColaboradorForm(): ColaboradorFormModel {
  return {
    federationId: DEFAULT_FEDERATION_ID,
    singularId: null,
    areaId: null,
    teamId: null,
    managerId: null,
    name: "",
    email: "",
    zimbraId: "",
    biography: ""
  };
}

export function mapColaboradorToForm(
  colaborador: ColaboradorResponse
): ColaboradorFormModel {
  return {
    federationId: colaborador.federationId,
    singularId: colaborador.singularId,
    areaId: colaborador.areaId,
    teamId: colaborador.teamId,
    managerId: colaborador.managerId,
    name: colaborador.name,
    email: colaborador.email,
    zimbraId: colaborador.zimbraId,
    biography: colaborador.biography ?? ""
  };
}

export function mapColaboradorFieldErrors(
  errors: readonly FieldValidationError[]
): Record<string, string> {
  const mapped: Record<string, string> = {};

  for (const item of errors) {
    mapped[item.field] = item.message;
  }

  return mapped;
}

function appendOptionalNumber(
  target: CreateColaboradorRequest | UpdateColaboradorRequest,
  key: "singularId" | "areaId" | "teamId" | "managerId",
  value: number | null
): void {
  if (value !== null) {
    target[key] = value;
  }
}

function appendOptionalString(
  target: CreateColaboradorRequest | UpdateColaboradorRequest,
  key: "biography",
  value: string
): void {
  const trimmed = value.trim();
  if (trimmed.length > 0) {
    target[key] = trimmed;
  }
}

export function useColaboradorForm(initial?: Partial<ColaboradorFormModel>) {
  const { validateForm, required, maxLength, email } = useFormValidation();

  const form: UnwrapNestedRefs<ColaboradorFormModel> = reactive({
    ...createEmptyColaboradorForm(),
    ...initial
  });

  function validateCreate(): {
    valid: boolean;
    errors: Record<string, string>;
  } {
    return validateForm({
      name: {
        value: form.name,
        rules: [required("Nome é obrigatório"), maxLength(255)]
      },
      email: {
        value: form.email,
        rules: [
          required("E-mail é obrigatório"),
          email("Informe um e-mail válido"),
          maxLength(255)
        ]
      },
      zimbraId: {
        value: form.zimbraId,
        rules: [required("Identificador Zimbra é obrigatório"), maxLength(255)]
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
        rules: [required("Nome é obrigatório"), maxLength(255)]
      },
      zimbraId: {
        value: form.zimbraId,
        rules: [required("Identificador Zimbra é obrigatório"), maxLength(255)]
      }
    });
  }

  function toCreateRequest(): CreateColaboradorRequest {
    const request: CreateColaboradorRequest = {
      federationId: form.federationId,
      name: form.name.trim(),
      email: form.email.trim(),
      zimbraId: form.zimbraId.trim()
    };

    appendOptionalNumber(request, "singularId", form.singularId);
    appendOptionalNumber(request, "areaId", form.areaId);
    appendOptionalNumber(request, "teamId", form.teamId);
    appendOptionalNumber(request, "managerId", form.managerId);
    appendOptionalString(request, "biography", form.biography);

    return request;
  }

  function toUpdateRequest(): UpdateColaboradorRequest {
    const request: UpdateColaboradorRequest = {
      name: form.name.trim(),
      zimbraId: form.zimbraId.trim()
    };

    appendOptionalNumber(request, "singularId", form.singularId);
    appendOptionalNumber(request, "areaId", form.areaId);
    appendOptionalNumber(request, "teamId", form.teamId);
    appendOptionalNumber(request, "managerId", form.managerId);
    appendOptionalString(request, "biography", form.biography);

    return request;
  }

  function reset(next?: Partial<ColaboradorFormModel>): void {
    Object.assign(form, createEmptyColaboradorForm(), next);
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
