package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA do binário de um arquivo (FT-DOCUMENTO). {@code urlArquivo} é a referência
 * interna ao objeto no Object Storage (DEC-013) — nunca exposta diretamente ao cliente
 * (ADR-004); o download é sempre mediado pelo backend.
 */
@Getter
@Setter
@Entity
@Table(name = "ARQUIVO_BINARIO", schema = "UNMPORTCOM")
public class ArquivoBinarioEntity {

    @Id
    @Column(name = "COD_ARQUIVO_BINARIO", nullable = false)
    private Long id;

    @Column(name = "NOM_ARQUIVO", nullable = false, length = 500)
    private String nomeArquivo;

    @Column(name = "URL_ARQUIVO", nullable = false, length = 2000)
    private String urlArquivo;

    @Column(name = "TIP_MIME", nullable = false, length = 200)
    private String tipoMime;

    @Column(name = "QTD_TAMANHO_BYTES", nullable = false)
    private Long tamanhoBytes;

    @Column(name = "HASH_ARQUIVO", nullable = false, length = 128)
    private String hash;

    @Column(name = "DAT_CADASTRO", nullable = false)
    private Instant dataCadastro;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        ArquivoBinarioEntity that = (ArquivoBinarioEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
