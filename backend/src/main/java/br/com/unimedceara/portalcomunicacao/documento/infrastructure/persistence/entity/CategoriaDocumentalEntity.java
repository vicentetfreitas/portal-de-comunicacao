package br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA de categoria documental — FK obrigatória de {@link DocumentoEntity}
 * (COD_CATEGORIA_DOCUMENTAL NOT NULL). Não faz parte do contrato de resposta desta Feature
 * (metadado não exposto, ver {@code specification.md} § Modelo de Dados); mapeada só para
 * satisfazer a integridade referencial ao ler/testar {@code DOCUMENTO}.
 */
@Getter
@Setter
@Entity
@Table(name = "CATEGORIA_DOCUMENTAL", schema = "UNMPORTCOM")
public class CategoriaDocumentalEntity {

    @Id
    @Column(name = "COD_CATEGORIA_DOCUMENTAL", nullable = false)
    private Long id;

    @Column(name = "NOM_CATEGORIA", nullable = false, length = 150)
    private String nome;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "FLG_ATIVO", nullable = false, length = 1)
    private String ativo;

    @Column(name = "DAT_CADASTRO", nullable = false)
    private Instant dataCadastro;

    @Column(name = "DAT_ATUALIZACAO")
    private Instant dataAtualizacao;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        CategoriaDocumentalEntity that = (CategoriaDocumentalEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
