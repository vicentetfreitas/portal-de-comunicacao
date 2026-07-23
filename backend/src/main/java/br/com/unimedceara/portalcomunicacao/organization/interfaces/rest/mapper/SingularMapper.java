package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.SingularResponse;
import org.springframework.stereotype.Component;

/**
 * Conversão entre entidade JPA e DTOs de singular.
 */
@Component
public class SingularMapper {

    public SingularResponse toResponse(SingularEntity entity) {
        return new SingularResponse(
                entity.getId(),
                entity.getFederacaoId(),
                entity.getNome(),
                entity.getSigla(),
                entity.getCodigoUnimed(),
                entity.getRegistroAns(),
                SingularStatus.fromFlag(entity.getAtivo()),
                entity.getDataCadastro(),
                entity.getDataAtualizacao());
    }
}
