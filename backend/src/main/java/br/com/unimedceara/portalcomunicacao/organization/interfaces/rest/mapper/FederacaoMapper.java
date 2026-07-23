package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.FederacaoResponse;
import org.springframework.stereotype.Component;

/**
 * Conversão entre entidade JPA e DTOs de federação.
 */
@Component
public class FederacaoMapper {

    public FederacaoResponse toResponse(FederacaoEntity entity) {
        return new FederacaoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getSigla(),
                entity.getCodigoUnimed(),
                entity.getRegistroAns(),
                entity.getUrlSite(),
                entity.getDescricao(),
                FederacaoStatus.fromFlag(entity.getAtivo()),
                entity.getDataCadastro(),
                entity.getDataAtualizacao());
    }
}
