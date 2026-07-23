package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA da área organizacional (FT-AREA).
 */
@Getter
@Setter
@Entity
@Table(name = "AREA", schema = "UNMPORTCOM")
public class AreaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_area")
    @SequenceGenerator(name = "sq_area", sequenceName = "SQ_AREA_COD_AREA", allocationSize = 1)
    @Column(name = "COD_AREA", nullable = false)
    private Long id;

    @Column(name = "COD_SINGULAR")
    private Long singularId;

    @Column(name = "NOM_AREA", nullable = false, length = 200)
    private String nome;

    @Column(name = "SIG_AREA", length = 30)
    private String sigla;

    @Lob
    @Column(name = "DSC_AREA")
    private String descricao;

    @Column(name = "COD_GESTOR")
    private Long gestorId;

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
        AreaEntity that = (AreaEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
