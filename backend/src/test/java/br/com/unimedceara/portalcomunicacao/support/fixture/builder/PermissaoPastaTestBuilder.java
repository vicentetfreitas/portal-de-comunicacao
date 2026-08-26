package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.PermissaoPastaEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.PermissaoPastaRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.TestIdSequence;

import java.time.Instant;

public final class PermissaoPastaTestBuilder {

    private final long pastaId;
    private String tipoDestinatario = PermissaoPastaEntity.DESTINATARIO_AREA;
    private long codigoDestinatario;
    private String tipoAcesso = PermissaoPastaEntity.ACESSO_LEITURA;

    private PermissaoPastaTestBuilder(long pastaId) {
        this.pastaId = pastaId;
    }

    public static PermissaoPastaTestBuilder paraPasta(long pastaId) {
        return new PermissaoPastaTestBuilder(pastaId);
    }

    public PermissaoPastaTestBuilder destinatario(String tipoDestinatario, long codigoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
        this.codigoDestinatario = codigoDestinatario;
        return this;
    }

    public PermissaoPastaTestBuilder acesso(String tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
        return this;
    }

    public PermissaoPastaEntity build() {
        PermissaoPastaEntity permissao = new PermissaoPastaEntity();
        permissao.setId(TestIdSequence.next());
        permissao.setPastaId(pastaId);
        permissao.setTipoDestinatario(tipoDestinatario);
        permissao.setCodigoDestinatario(codigoDestinatario);
        permissao.setTipoAcesso(tipoAcesso);
        permissao.setDataCadastro(Instant.now());
        return permissao;
    }

    public PermissaoPastaEntity persist(PermissaoPastaRepository repository) {
        return repository.save(build());
    }
}
