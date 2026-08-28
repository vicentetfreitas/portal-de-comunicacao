package br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.ArquivoBinarioEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoEntity;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto.DocumentoResponse;
import org.springframework.stereotype.Component;

/**
 * Conversão entre entidades JPA e o DTO de documento. {@code formato}/{@code tamanhoBytes}
 * vêm do binário da versão atual — nunca do documento diretamente (não há coluna própria).
 */
@Component
public class DocumentoMapper {

    public DocumentoResponse toResponse(DocumentoEntity documento, ArquivoBinarioEntity arquivoAtual) {
        return new DocumentoResponse(
                documento.getId(),
                documento.getTitulo(),
                arquivoAtual.getTipoMime(),
                arquivoAtual.getTamanhoBytes());
    }
}
