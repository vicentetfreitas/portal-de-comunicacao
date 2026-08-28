import { reactive, watch, type Ref } from "vue";

/**
 * "Editar perfil" self-service — `cargo`, e-mail adicional, telefones,
 * ramais e celulares não existem em nenhum contrato aprovado
 * (`ColaboradorResponse`/`UpdateColaboradorRequest`); e mesmo `name`, que
 * existe, não pode ser persistido pelo colaborador comum: `PUT /api/v1/
 * colaboradores/{id}` (`ColaboradorApplicationService.update`) chama
 * `ensureOrganizationAdministrator`, que checa uma allowlist fixa de
 * e-mails admin (`SessionAdministratorAuthorizationService`) sem exceção
 * para "o próprio colaborador" — um colaborador comum editando o próprio
 * perfil recebe 403 Forbidden.
 *
 * Decisão explícita do usuário (2026-08-26): persistir os seis campos só no
 * navegador (`localStorage`, por colaborador), até existir um endpoint/
 * regra de autorização real de self-service. Não é fingir uma escrita real
 * — é um rascunho local, explícito no código e na UI, que sobrevive a
 * reloads/sessões *neste* navegador mas não sincroniza entre dispositivos
 * nem aparece para nenhum outro colaborador/admin.
 */
export interface PerfilLocalFields {
  name: string;
  cargo: string;
  additionalEmail: string;
  phones: string;
  ramais: string;
  celulares: string;
}

const STORAGE_PREFIX = "portal:perfil-local:";

function storageKey(userId: number): string {
  return `${STORAGE_PREFIX}${userId}`;
}

function emptyFields(): PerfilLocalFields {
  return {
    name: "",
    cargo: "",
    additionalEmail: "",
    phones: "",
    ramais: "",
    celulares: ""
  };
}

function readStorage(userId: number): Partial<PerfilLocalFields> | null {
  try {
    const raw = window.localStorage.getItem(storageKey(userId));
    return raw ? (JSON.parse(raw) as Partial<PerfilLocalFields>) : null;
  } catch {
    // Private browsing / storage disabled — degrade to session-only state
    // instead of throwing.
    return null;
  }
}

function writeStorage(userId: number, fields: PerfilLocalFields): void {
  try {
    window.localStorage.setItem(storageKey(userId), JSON.stringify(fields));
  } catch {
    // Same as above — silently no-op rather than break the form.
  }
}

export function usePerfilLocalFields(
  userId: Ref<number | null>,
  sessionName: Ref<string>
) {
  const fields = reactive<PerfilLocalFields>(emptyFields());
  const saved = reactive({ at: null as string | null });

  function load(): void {
    const id = userId.value;
    if (id == null) {
      Object.assign(fields, emptyFields());
      return;
    }

    const stored = readStorage(id);
    Object.assign(fields, emptyFields(), stored ?? { name: sessionName.value });
  }

  function save(): void {
    const id = userId.value;
    if (id == null) {
      return;
    }
    writeStorage(id, { ...fields });
    saved.at = new Date().toISOString();
  }

  watch(userId, load, { immediate: true });

  return { fields, saved, save };
}
