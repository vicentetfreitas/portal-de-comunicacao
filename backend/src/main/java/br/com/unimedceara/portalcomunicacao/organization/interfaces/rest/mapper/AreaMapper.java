package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.AreaResponse;
import org.springframework.stereotype.Component;

/**
 * Conversão entre entidade JPA e DTOs de área.
 */
@Component
public class AreaMapper {

    public AreaResponse toResponse(AreaEntity entity) {
        return new AreaResponse(
                entity.getId(),
                entity.getSingularId(),
                entity.getNome(),
                entity.getSigla(),
                entity.getDescricao(),
                entity.getGestorId(),
                AreaStatus.fromFlag(entity.getAtivo()),
                entity.getDataCadastro(),
                entity.getDataAtualizacao());
    }
}
