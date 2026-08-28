package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.CategoriaDocumentalEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.CategoriaDocumentalRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.TestIdSequence;

import java.time.Instant;

public final class CategoriaDocumentalTestBuilder {

    private String nome = "Categoria Teste";

    private CategoriaDocumentalTestBuilder() {
    }

    public static CategoriaDocumentalTestBuilder nova() {
        return new CategoriaDocumentalTestBuilder();
    }

    public CategoriaDocumentalEntity build() {
        CategoriaDocumentalEntity categoria = new CategoriaDocumentalEntity();
        categoria.setId(TestIdSequence.next());
        categoria.setNome(nome);
        categoria.setAtivo("S");
        categoria.setDataCadastro(Instant.now());
        return categoria;
    }

    public CategoriaDocumentalEntity persist(CategoriaDocumentalRepository repository) {
        return repository.save(build());
    }
}
