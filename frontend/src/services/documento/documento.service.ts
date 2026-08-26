import { DOCUMENTO_API_PATHS, PASTA_API_PATHS } from "@/config/documento";
import { BaseApiClient, getHttpClient } from "@/services/http";
import type { PageResponse } from "@/types/api";
import type { PastaResponse } from "@/types/documento/documento.types";

/**
 * FT-DOCUMENTO HTTP service — GET /api/v1/pastas. Contexto Ativo nunca é parâmetro de
 * request — sempre derivado da sessão no backend (api.md § Regras Específicas da API).
 */
export class PastaApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  list(): Promise<PageResponse<PastaResponse>> {
    return this.getPage<PastaResponse>(PASTA_API_PATHS.base);
  }
}

/**
 * FT-DOCUMENTO HTTP service — GET /api/v1/documentos/{id}/download. Retorna o binário
 * bruto (não um ApiResponse) — backend nunca expõe a URL do Object Storage (ADR-004).
 */
export class DocumentoApiService extends BaseApiClient {
  constructor() {
    super(getHttpClient());
  }

  download(id: number): Promise<{ blob: Blob; filename: string | null }> {
    return this.getBlob(`${DOCUMENTO_API_PATHS.base}/${id}/download`);
  }
}

export const pastaService = new PastaApiService();
export const documentoService = new DocumentoApiService();
