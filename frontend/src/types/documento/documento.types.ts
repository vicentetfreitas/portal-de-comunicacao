/**
 * FT-DOCUMENTO response types (api.md — PastaResponse/DocumentoResponse).
 */
export interface DocumentoResponse {
  id: number;
  nome: string;
  formato: string;
  tamanhoBytes: number;
}

export interface PastaResponse {
  id: number;
  nome: string;
  documentos: DocumentoResponse[];
}
