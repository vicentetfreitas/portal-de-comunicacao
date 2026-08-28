package br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto.DocumentoResponse;
import br.com.unimedceara.portalcomunicacao.documento.interfaces.rest.dto.PastaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Conversão entre {@link PastaEntity} e {@link PastaResponse}.
 */
@Component
public class PastaMapper {

    public PastaResponse toResponse(PastaEntity pasta, List<DocumentoResponse> documentos) {
        return new PastaResponse(pasta.getId(), pasta.getNome(), documentos);
    }
}
