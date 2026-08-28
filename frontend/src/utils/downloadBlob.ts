/**
 * Aciona o download de um Blob pelo navegador. Não existia utilitário equivalente no
 * projeto (FT-DOCUMENTO é a primeira Feature com download de arquivo binário).
 *
 * Usa um link temporário `<a download>` + Object URL — padrão para blobs já em memória
 * (obtidos via axios `responseType: "blob"`, autenticados pelo client HTTP existente,
 * não por `<a href>` direto, que não carregaria cookies/CSRF do client).
 */
export function downloadBlob(blob: Blob, filename: string): void {
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}
