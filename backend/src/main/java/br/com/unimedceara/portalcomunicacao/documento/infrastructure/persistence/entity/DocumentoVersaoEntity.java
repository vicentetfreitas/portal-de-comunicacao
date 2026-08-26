package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA de versão de documento (FT-DOCUMENTO). O binário de um documento é sempre
 * resolvido via a versão com {@code versaoAtual = "S"} — nunca uma coluna direta em
 * {@link DocumentoEntity}.
 */
@Getter
@Setter
@Entity
@Table(name = "DOCUMENTO_VERSAO", schema = "UNMPORTCOM")
public class DocumentoVersaoEntity {

    public static final String VERSAO_ATUAL_SIM = "S";

    @Id
    @Column(name = "COD_DOCUMENTO_VERSAO", nullable = false)
    private Long id;

    @Column(name = "COD_DOCUMENTO", nullable = false)
    private Long documentoId;

    @Column(name = "COD_ARQUIVO_BINARIO", nullable = false)
    private Long arquivoBinarioId;

    @Column(name = "COD_COLABORADOR", nullable = false)
    private Long colaboradorId;

    @Column(name = "NUM_VERSAO", nullable = false)
    private Integer numeroVersao;

    @Lob
    @Column(name = "DSC_ALTERACAO")
    private String descricaoAlteracao;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "FLG_VERSAO_ATUAL", nullable = false, length = 1)
    private String versaoAtual;

    @Column(name = "DAT_VERSAO", nullable = false)
    private Instant dataVersao;

    public boolean isAtual() {
        return VERSAO_ATUAL_SIM.equalsIgnoreCase(versaoAtual);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        DocumentoVersaoEntity that = (DocumentoVersaoEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
