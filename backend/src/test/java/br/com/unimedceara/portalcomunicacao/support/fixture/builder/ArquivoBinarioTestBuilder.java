package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity.ArquivoBinarioEntity;
import br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository.ArquivoBinarioRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.TestIdSequence;

import java.time.Instant;

public final class ArquivoBinarioTestBuilder {

    private String nomeArquivo = "arquivo-teste.pdf";
    private String urlArquivo = "documentos/arquivo-teste.pdf";
    private String tipoMime = "application/pdf";
    private long tamanhoBytes = 1024L;
    private String hash;

    private ArquivoBinarioTestBuilder() {
    }

    public static ArquivoBinarioTestBuilder novo() {
        return new ArquivoBinarioTestBuilder();
    }

    public ArquivoBinarioTestBuilder nomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        return this;
    }

    public ArquivoBinarioTestBuilder tipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
        return this;
    }

    public ArquivoBinarioTestBuilder tamanhoBytes(long tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
        return this;
    }

    public ArquivoBinarioEntity build() {
        long id = TestIdSequence.next();
        ArquivoBinarioEntity arquivo = new ArquivoBinarioEntity();
        arquivo.setId(id);
        arquivo.setNomeArquivo(nomeArquivo);
        arquivo.setUrlArquivo(urlArquivo);
        arquivo.setTipoMime(tipoMime);
        arquivo.setTamanhoBytes(tamanhoBytes);
        arquivo.setHash(hash != null ? hash : "hash-teste-" + id);
        arquivo.setDataCadastro(Instant.now());
        return arquivo;
    }

    public ArquivoBinarioEntity persist(ArquivoBinarioRepository repository) {
        return repository.save(build());
    }
}
