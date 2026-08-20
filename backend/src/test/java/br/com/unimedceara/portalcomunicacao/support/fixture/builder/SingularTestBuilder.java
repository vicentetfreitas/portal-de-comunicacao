package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;

import java.time.Instant;

public final class SingularTestBuilder {

    private long federacaoId;
    private String nome = "Singular Teste";
    private String sigla = IntegrationTestUniqueData.singularSigla("SN");
    private int codigoUnimed = IntegrationTestUniqueData.singularUnimedCode();
    private String registroAns;
    private String dominioEmail;

    private SingularTestBuilder() {}

    public static SingularTestBuilder forFederation(long federacaoId) {
        SingularTestBuilder builder = new SingularTestBuilder();
        builder.federacaoId = federacaoId;
        return builder;
    }

    public SingularTestBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public SingularTestBuilder sigla(String sigla) {
        this.sigla = sigla;
        return this;
    }

    public SingularTestBuilder dominioEmail(String dominioEmail) {
        this.dominioEmail = dominioEmail;
        return this;
    }

    public SingularTestBuilder codigoUnimed(int codigoUnimed) {
        this.codigoUnimed = codigoUnimed;
        this.registroAns = IntegrationTestUniqueData.registroAnsForUnimedCode(codigoUnimed);
        return this;
    }

    public SingularEntity build() {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(federacaoId);
        singular.setNome(nome);
        singular.setSigla(sigla);
        singular.setCodigoUnimed(codigoUnimed);
        singular.setRegistroAns(
                registroAns != null ? registroAns : IntegrationTestUniqueData.registroAnsForUnimedCode(codigoUnimed));
        singular.setDominioEmail(dominioEmail);
        singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        return singular;
    }

    public SingularEntity persist(SingularRepository repository) {
        return repository.save(build());
    }
}
