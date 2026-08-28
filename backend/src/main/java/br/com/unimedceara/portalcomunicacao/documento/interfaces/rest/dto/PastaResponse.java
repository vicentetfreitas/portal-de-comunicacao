package br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto;

import java.util.List;

/**
 * Representação de pasta na API (api.md — FT-DOCUMENTO). {@code documentos} não é paginado —
 * volume esperado baixo por pasta (decisão registrada em api.md).
 */
public record PastaResponse(
        Long id,
        String nome,
        List<DocumentoResponse> documentos) {
}
