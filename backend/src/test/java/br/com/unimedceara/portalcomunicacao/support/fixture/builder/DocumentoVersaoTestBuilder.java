package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoVersaoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoVersaoRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.TestIdSequence;

import java.time.Instant;

public final class DocumentoVersaoTestBuilder {

    private final long documentoId;
    private final long arquivoBinarioId;
    private final long colaboradorId;
    private int numeroVersao = 1;
    private String versaoAtual = "S";

    private DocumentoVersaoTestBuilder(long documentoId, long arquivoBinarioId, long colaboradorId) {
        this.documentoId = documentoId;
        this.arquivoBinarioId = arquivoBinarioId;
        this.colaboradorId = colaboradorId;
    }

    public static DocumentoVersaoTestBuilder atual(long documentoId, long arquivoBinarioId, long colaboradorId) {
        return new DocumentoVersaoTestBuilder(documentoId, arquivoBinarioId, colaboradorId);
    }

    public DocumentoVersaoEntity build() {
        DocumentoVersaoEntity versao = new DocumentoVersaoEntity();
        versao.setId(TestIdSequence.next());
        versao.setDocumentoId(documentoId);
        versao.setArquivoBinarioId(arquivoBinarioId);
        versao.setColaboradorId(colaboradorId);
        versao.setNumeroVersao(numeroVersao);
        versao.setVersaoAtual(versaoAtual);
        versao.setDataVersao(Instant.now());
        return versao;
    }

    public DocumentoVersaoEntity persist(DocumentoVersaoRepository repository) {
        return repository.save(build());
    }
}
