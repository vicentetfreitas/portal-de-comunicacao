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
 * Entidade JPA de pasta documental (FT-DOCUMENTO). Somente leitura — sem geração de id,
 * a Feature não cria pastas.
 */
@Getter
@Setter
@Entity
@Table(name = "PASTA", schema = "UNMPORTCOM")
public class PastaEntity {

    @Id
    @Column(name = "COD_PASTA", nullable = false)
    private Long id;

    @Column(name = "COD_PASTA_PAI")
    private Long pastaPaiId;

    @Column(name = "NOM_PASTA", nullable = false, length = 200)
    private String nome;

    @Lob
    @Column(name = "DSC_PASTA")
    private String descricao;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "FLG_HERDA_PERMISSAO", nullable = false, length = 1)
    private String herdaPermissao;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "FLG_ATIVO", nullable = false, length = 1)
    private String ativo;

    @Column(name = "DAT_CADASTRO", nullable = false)
    private Instant dataCadastro;

    @Column(name = "DAT_ATUALIZACAO")
    private Instant dataAtualizacao;

    public boolean isAtivo() {
        return "S".equalsIgnoreCase(ativo);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PastaEntity that = (PastaEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
