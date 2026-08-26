package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PastaRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.TestIdSequence;

import java.time.Instant;

public final class PastaTestBuilder {

    private String nome = "Pasta Teste";
    private String ativo = "S";
    private String herdaPermissao = "S";

    private PastaTestBuilder() {
    }

    public static PastaTestBuilder nova() {
        return new PastaTestBuilder();
    }

    public PastaTestBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public PastaTestBuilder inativa() {
        this.ativo = "N";
        return this;
    }

    public PastaEntity build() {
        PastaEntity pasta = new PastaEntity();
        pasta.setId(TestIdSequence.next());
        pasta.setNome(nome);
        pasta.setAtivo(ativo);
        pasta.setHerdaPermissao(herdaPermissao);
        pasta.setDataCadastro(Instant.now());
        return pasta;
    }

    public PastaEntity persist(PastaRepository repository) {
        return repository.save(build());
    }
}
