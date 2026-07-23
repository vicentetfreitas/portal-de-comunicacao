package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto.SingularResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SingularMapperTest {

    private final SingularMapper singularMapper = new SingularMapper();

    @Test
    void shouldMapEntityToResponse() {
        Instant createdAt = Instant.parse("2026-07-14T12:00:00Z");
        Instant updatedAt = Instant.parse("2026-07-14T13:00:00Z");

        SingularEntity entity = new SingularEntity();
        entity.setId(1L);
        entity.setFederacaoId(10L);
        entity.setNome("Unimed Ceará");
        entity.setSigla("UNI-CE");
        entity.setCodigoUnimed(2);
        entity.setRegistroAns("123456");
        entity.setAtivo(SingularStatus.ACTIVE.toFlag());
        entity.setDataCadastro(createdAt);
        entity.setDataAtualizacao(updatedAt);

        SingularResponse response = singularMapper.toResponse(entity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.federationId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Unimed Ceará");
        assertThat(response.acronym()).isEqualTo("UNI-CE");
        assertThat(response.unimedCode()).isEqualTo(2);
        assertThat(response.registroAns()).isEqualTo("123456");
        assertThat(response.status()).isEqualTo(SingularStatus.ACTIVE);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
    }
}
