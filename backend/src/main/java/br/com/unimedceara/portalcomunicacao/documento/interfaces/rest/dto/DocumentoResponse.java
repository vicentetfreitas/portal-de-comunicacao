package br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto;

/**
 * Representação de documento na API (api.md — FT-DOCUMENTO). {@code formato}/{@code tamanhoBytes}
 * são resolvidos da versão atual ({@code DOCUMENTO_VERSAO.FLG_VERSAO_ATUAL='S'} →
 * {@code ARQUIVO_BINARIO}) — nunca expõe {@code URL_ARQUIVO} (ADR-004).
 */
public record DocumentoResponse(
        Long id,
        String nome,
        String formato,
        Long tamanhoBytes) {
}
