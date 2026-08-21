package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.PapelRepository;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;

import java.time.Instant;

/**
 * Builder de teste para {@link PapelEntity}. Sem sequence homologada (ver
 * {@link IntegrationTestUniqueData#uniqueId()}) — identificador atribuído explicitamente.
 */
public final class PapelTestBuilder {

    private long id = IntegrationTestUniqueData.uniqueId();
    private String nome = "PAPEL_TESTE_" + id;
    private String descricao = "Papel de teste";
    private String ativo = "S";

    private PapelTestBuilder() {}

    public static PapelTestBuilder named(String nome) {
        PapelTestBuilder builder = new PapelTestBuilder();
        builder.nome = nome;
        return builder;
    }

    public PapelTestBuilder ativo(String ativo) {
        this.ativo = ativo;
        return this;
    }

    public PapelEntity build() {
        PapelEntity papel = new PapelEntity();
        papel.setId(id);
        papel.setNome(nome);
        papel.setDescricao(descricao);
        papel.setAtivo(ativo);
        papel.setDataCadastro(Instant.now());
        return papel;
    }

    public PapelEntity persist(PapelRepository repository) {
        return repository.save(build());
    }
}
