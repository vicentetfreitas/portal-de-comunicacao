package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.EquipeResponse;
import org.springframework.stereotype.Component;

/**
 * Conversão entre entidade JPA e DTOs de equipe.
 */
@Component
public class EquipeMapper {

    public EquipeResponse toResponse(EquipeEntity entity) {
        return new EquipeResponse(
                entity.getId(),
                entity.getAreaId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getLiderId(),
                EquipeStatus.fromFlag(entity.getAtivo()),
                entity.getDataCadastro(),
                entity.getDataAtualizacao());
    }
}
