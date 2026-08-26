package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA de documento (FT-DOCUMENTO). Somente leitura — sem geração de id,
 * a Feature não cria documentos.
 */
@Getter
@Setter
@Entity
@Table(name = "DOCUMENTO", schema = "UNMPORTCOM")
public class DocumentoEntity {

    public static final String STATUS_ATIVO = "ATIVO";
    public static final String STATUS_ARQUIVADO = "ARQUIVADO";
    public static final String STATUS_EXPIRADO = "EXPIRADO";

    @Id
    @Column(name = "COD_DOCUMENTO", nullable = false)
    private Long id;

    @Column(name = "COD_CATEGORIA_DOCUMENTAL", nullable = false)
    private Long categoriaDocumentalId;

    @Column(name = "COD_PASTA", nullable = false)
    private Long pastaId;

    @Column(name = "COD_COLABORADOR", nullable = false)
    private Long colaboradorId;

    @Column(name = "TIT_DOCUMENTO", nullable = false, length = 300)
    private String titulo;

    @Lob
    @Column(name = "DSC_DOCUMENTO")
    private String descricao;

    @Column(name = "STA_DOCUMENTO", nullable = false, length = 30)
    private String status;

    @Column(name = "DAT_PUBLICACAO")
    private Instant dataPublicacao;

    @Column(name = "DAT_EXPIRACAO")
    private Instant dataExpiracao;

    @Column(name = "DAT_CADASTRO", nullable = false)
    private Instant dataCadastro;

    @Column(name = "DAT_ATUALIZACAO")
    private Instant dataAtualizacao;

    public boolean isExpirado() {
        return STATUS_EXPIRADO.equalsIgnoreCase(status);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        DocumentoEntity that = (DocumentoEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
