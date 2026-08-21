package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity;

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
 * Entidade JPA de papel de autorização ({@code PAPEL}).
 * <p>
 * Somente leitura nesta evolução de FT-SESSION: o catálogo de papéis é
 * mantido fora deste escopo (seed em {@code database/ddl/008-initial-data.sql}).
 * Sem {@code @GeneratedValue}: a tabela não possui sequence homologada no
 * baseline 2026-07-22 ({@code database/model/03-physical-model.md}) e este
 * escopo não cria registros de {@code PAPEL}.
 */
@Getter
@Setter
@Entity
@Table(name = "PAPEL", schema = "UNMPORTCOM")
public class PapelEntity {

    @Id
    @Column(name = "COD_PAPEL", nullable = false)
    private Long id;

    @Column(name = "NOM_PAPEL", nullable = false, length = 100)
    private String nome;

    @Lob
    @Column(name = "DSC_PAPEL")
    private String descricao;

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
        PapelEntity that = (PapelEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
