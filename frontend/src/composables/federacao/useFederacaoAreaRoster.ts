import { computed, onMounted, ref, watch, type Ref } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { colaboradorService } from "@/services/organization";

const ROSTER_PAGE_SIZE = 100;

export interface FederacaoAreaRosterMember {
  id: number;
  name: string;
  /** Real (`GET /api/v1/colaboradores`) — array-shaped for a future multi-email colaborador, currently always length 1. */
  emails: string[];
  /** MOCK — no `cargo` field exists in `ColaboradorResponse` today. See module comment. */
  cargo: string;
  /** MOCK — no telefone field exists in any approved contract today. */
  phones: string[];
  /** MOCK — same as `phones`. */
  ramais: string[];
}

export interface FederacaoAreaContatoSetorial {
  email: string;
  phone: string;
}

/**
 * Federação > Área > Equipe — roster de colaboradores ativos vinculados à
 * Área (`GET /api/v1/colaboradores?areaId=…`, já `APPROVED`/`FT-COLABORADOR`
 * — mesmo endpoint que `ColaboradorListPage.vue` usa, sem novo contrato).
 *
 * `cargo`, `phones`/`ramais` e o "Contato setorial" da Área **não existem**
 * em nenhum contrato aprovado hoje (`ColaboradorResponse` só tem `name`/
 * `email`; `AreaResponse` não tem contato institucional) — mockados aqui de
 * propósito, por decisão explícita do usuário ("pode mocar os dados por
 * enquanto"), até existir uma fonte real (provavelmente exige mudança de
 * contrato de backend — ver `specs/features/federacao-colaborador/
 * specification.md`, "Decisões de produto"). `name`/`emails` continuam
 * vindo da API real, sem mock.
 */
const MOCK_CARGOS = [
  "Analista",
  "Coordenador(a)",
  "Assistente",
  "Especialista",
  "Supervisor(a)"
];

function mockCargo(id: number): string {
  return MOCK_CARGOS[id % MOCK_CARGOS.length]!;
}

function mockPhone(id: number): string {
  const line = 1000 + (id % 9000);
  return `(85) 9${8000 + (id % 1000)}-${line}`;
}

function mockRamal(id: number): string {
  return String(1000 + (id % 9000));
}

export function useFederacaoAreaRoster(areaId: Ref<string>) {
  const { handleError } = useStandardErrorHandling();

  const members = ref<FederacaoAreaRosterMember[]>([]);
  const loading = ref(true);

  const numericAreaId = computed(() => Number(areaId.value));
  const isEmpty = computed(() => !loading.value && members.value.length === 0);

  // MOCK, area-level (not per-person) — see module comment above.
  const contatoSetorial = computed<FederacaoAreaContatoSetorial>(() => ({
    email: `contato.area${numericAreaId.value || ""}@unimedceara.com.br`,
    phone: "(85) 3255-6000"
  }));

  async function loadRoster(): Promise<void> {
    loading.value = true;
    members.value = [];

    if (!Number.isFinite(numericAreaId.value) || numericAreaId.value <= 0) {
      loading.value = false;
      return;
    }

    try {
      const page = await colaboradorService.list({
        areaId: numericAreaId.value,
        status: "ACTIVE",
        size: ROSTER_PAGE_SIZE,
        sort: "name,asc"
      });
      members.value = page.content.map(colaborador => ({
        id: colaborador.id,
        name: colaborador.name,
        emails: [colaborador.email],
        cargo: mockCargo(colaborador.id),
        phones: [mockPhone(colaborador.id)],
        ramais: [mockRamal(colaborador.id)]
      }));
    } catch (error) {
      handleError(error);
    } finally {
      loading.value = false;
    }
  }

  onMounted(() => {
    void loadRoster();
  });

  watch(areaId, () => {
    void loadRoster();
  });

  return {
    members,
    loading,
    isEmpty,
    contatoSetorial,
    loadRoster
  };
}
