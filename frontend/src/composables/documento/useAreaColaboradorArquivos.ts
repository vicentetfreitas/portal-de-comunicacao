import { computed, onMounted, ref } from "vue";

import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { documentoService, pastaService } from "@/services/documento";
import type {
  DocumentoResponse,
  PastaResponse
} from "@/types/documento/documento.types";
import { downloadBlob } from "@/utils/downloadBlob";

/**
 * FT-DOCUMENTO (TK-DOCUMENTO-003) — pastas/documentos visíveis ao Contexto Ativo do
 * colaborador (RF-DOCUMENTO-001/003/004). Reutiliza o padrão de
 * useAreaColaboradorEquipes.ts — só que aqui o backend já resolve o Contexto Ativo
 * internamente (nenhum id é passado como parâmetro, ver api.md).
 */
export function useAreaColaboradorArquivos() {
  const { handleError } = useStandardErrorHandling();

  const pastas = ref<PastaResponse[]>([]);
  const loading = ref(true);
  const downloadingId = ref<number | null>(null);

  const isEmpty = computed(() => !loading.value && pastas.value.length === 0);

  async function loadPastas(): Promise<void> {
    loading.value = true;
    pastas.value = [];

    // Contexto Ativo é resolvido no backend — sem checagem de areaId aqui como em
    // useAreaColaboradorEquipes.ts, mas a Feature ainda exige Contexto Ativo resolvido
    // (specification.md § Atores); requisição sem ele reflete o padrão já tratado por
    // guards de rota existentes (FT-AUTH/FT-SESSION), não redecidido aqui.
    try {
      const page = await pastaService.list();
      pastas.value = page.content;
    } catch (error) {
      handleError(error);
    } finally {
      loading.value = false;
    }
  }

  async function baixarDocumento(documento: DocumentoResponse): Promise<void> {
    downloadingId.value = documento.id;
    try {
      const { blob, filename } = await documentoService.download(documento.id);
      downloadBlob(blob, filename ?? documento.nome);
    } catch (error) {
      handleError(error);
    } finally {
      downloadingId.value = null;
    }
  }

  onMounted(() => {
    void loadPastas();
  });

  return {
    pastas,
    loading,
    isEmpty,
    downloadingId,
    loadPastas,
    baixarDocumento
  };
}
