package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;

import java.time.Instant;

public final class AreaTestBuilder {

    private long singularId;
    private String nome = "Área Teste";
    private String ativo = AreaStatus.ACTIVE.toFlag();

    private AreaTestBuilder() {}

    public static AreaTestBuilder forSingular(long singularId) {
        AreaTestBuilder builder = new AreaTestBuilder();
        builder.singularId = singularId;
        return builder;
    }

    public AreaTestBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public AreaTestBuilder inativa() {
        this.ativo = AreaStatus.INACTIVE.toFlag();
        return this;
    }

    public AreaEntity build() {
        AreaEntity area = new AreaEntity();
        area.setSingularId(singularId);
        area.setNome(nome);
        area.setAtivo(ativo);
        area.setDataCadastro(Instant.now());
        return area;
    }

    public AreaEntity persist(AreaRepository repository) {
        return repository.save(build());
    }
}
