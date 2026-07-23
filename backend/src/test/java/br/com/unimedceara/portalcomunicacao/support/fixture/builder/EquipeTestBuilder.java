package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;

import java.time.Instant;

public final class EquipeTestBuilder {

    private long areaId;
    private String nome = "Equipe Teste";

    private EquipeTestBuilder() {}

    public static EquipeTestBuilder forArea(long areaId) {
        EquipeTestBuilder builder = new EquipeTestBuilder();
        builder.areaId = areaId;
        return builder;
    }

    public EquipeTestBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public EquipeEntity build() {
        EquipeEntity equipe = new EquipeEntity();
        equipe.setAreaId(areaId);
        equipe.setNome(nome);
        equipe.setAtivo(EquipeStatus.ACTIVE.toFlag());
        equipe.setDataCadastro(Instant.now());
        return equipe;
    }

    public EquipeEntity persist(EquipeRepository repository) {
        return repository.save(build());
    }
}
