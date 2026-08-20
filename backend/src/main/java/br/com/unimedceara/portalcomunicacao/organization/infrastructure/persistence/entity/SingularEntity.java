package br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA da singular organizacional (FT-SINGULAR; scaffold inicial FT-AREA).
 */
@Getter
@Setter
@Entity
@Table(name = "SINGULAR", schema = "UNMPORTCOM")
public class SingularEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_singular")
    @SequenceGenerator(name = "sq_singular", sequenceName = "SQ_SINGULAR_COD_SINGULAR", allocationSize = 1)
    @Column(name = "COD_SINGULAR", nullable = false)
    private Long id;

    @Column(name = "COD_FEDERACAO", nullable = false)
    private Long federacaoId;

    @Column(name = "NOM_SINGULAR", nullable = false, length = 200)
    private String nome;

    @Column(name = "SIG_SINGULAR", nullable = false, length = 30)
    private String sigla;

    @Column(name = "COD_UNIMED", nullable = false, precision = 3, scale = 0)
    private Integer codigoUnimed;

    @Column(name = "NUM_REGISTRO_ANS", nullable = false, length = 20)
    private String registroAns;

    @Column(name = "DES_DOMINIO_EMAIL", length = 255)
    private String dominioEmail;

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
        SingularEntity that = (SingularEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
