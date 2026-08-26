package br.com.unimedceara.portalcomunicacao.documento.application.service;

import java.io.InputStream;

/**
 * Resultado da resolução de download de um documento (RF-DOCUMENTO-002) — conteúdo já
 * autorizado (RF-DOCUMENTO-003) e não expirado (RF-DOCUMENTO-004), pronto para stream.
 */
public record DocumentoDownload(
        String nomeArquivo,
        String tipoMime,
        Long tamanhoBytes,
        InputStream conteudo) {
}
