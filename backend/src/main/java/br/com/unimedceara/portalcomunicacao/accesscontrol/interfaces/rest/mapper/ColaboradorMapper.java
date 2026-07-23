package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.ColaboradorResponse;
import org.springframework.stereotype.Component;

/**
 * Conversão entre entidade JPA e DTOs de colaborador.
 */
@Component
public class ColaboradorMapper {

    public ColaboradorResponse toResponse(ColaboradorEntity entity) {
        return new ColaboradorResponse(
                entity.getId(),
                entity.getFederacaoId(),
                entity.getSingularId(),
                entity.getAreaId(),
                entity.getEquipeId(),
                entity.getGestorId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getZimbraId(),
                entity.getBiografia(),
                ColaboradorStatus.fromFlag(entity.getAtivo()),
                entity.getDataNascimento(),
                entity.getDataContratacao(),
                entity.getDataUltimoAcesso(),
                entity.getDataCadastro(),
                entity.getDataAtualizacao());
    }
}
