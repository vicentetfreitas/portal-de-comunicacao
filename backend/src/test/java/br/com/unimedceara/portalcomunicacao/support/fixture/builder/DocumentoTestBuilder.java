package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.DocumentoEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.DocumentoRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.TestIdSequence;

import java.time.Instant;

public final class DocumentoTestBuilder {

    private final long pastaId;
    private final long categoriaDocumentalId;
    private final long colaboradorId;
    private String titulo = "Documento Teste";
    private String status = DocumentoEntity.STATUS_ATIVO;

    private DocumentoTestBuilder(long pastaId, long categoriaDocumentalId, long colaboradorId) {
        this.pastaId = pastaId;
        this.categoriaDocumentalId = categoriaDocumentalId;
        this.colaboradorId = colaboradorId;
    }

    /**
     * {@code categoriaDocumentalId} e {@code colaboradorId} são obrigatórios porque
     * {@code DOCUMENTO} tem FK NOT NULL para ambos — precisam existir na base de teste
     * (ver {@link CategoriaDocumentalTestBuilder} e fixtures de colaborador já existentes).
     */
    public static DocumentoTestBuilder paraPasta(long pastaId, long categoriaDocumentalId, long colaboradorId) {
        return new DocumentoTestBuilder(pastaId, categoriaDocumentalId, colaboradorId);
    }

    public DocumentoTestBuilder titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public DocumentoTestBuilder status(String status) {
        this.status = status;
        return this;
    }

    public DocumentoEntity build() {
        DocumentoEntity documento = new DocumentoEntity();
        documento.setId(TestIdSequence.next());
        documento.setPastaId(pastaId);
        documento.setCategoriaDocumentalId(categoriaDocumentalId);
        documento.setColaboradorId(colaboradorId);
        documento.setTitulo(titulo);
        documento.setStatus(status);
        documento.setDataCadastro(Instant.now());
        return documento;
    }

    public DocumentoEntity persist(DocumentoRepository repository) {
        return repository.save(build());
    }
}
