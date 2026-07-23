package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.mapper;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class FederacaoMapperTest {

    private final FederacaoMapper mapper = new FederacaoMapper();

    @Test
    void shouldMapEntityToResponse() {
        FederacaoEntity entity = new FederacaoEntity();
        entity.setId(1L);
        entity.setNome("Unimed Federação");
        entity.setSigla("UNMFED");
        entity.setCodigoUnimed(979);
        entity.setRegistroAns("32195-8");
        entity.setUrlSite("https://unimedceara.com.br");
        entity.setDescricao("Descrição");
        entity.setAtivo(FederacaoStatus.ACTIVE.toFlag());
        entity.setDataCadastro(Instant.parse("2026-01-01T00:00:00Z"));
        entity.setDataAtualizacao(null);

        var response = mapper.toResponse(entity);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.unimedCode()).isEqualTo(979);
        assertThat(response.ansRegistration()).isEqualTo("32195-8");
        assertThat(response.status()).isEqualTo(FederacaoStatus.ACTIVE);
    }
}
