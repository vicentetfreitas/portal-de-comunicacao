package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity;

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
 * Entidade JPA do colaborador (FT-AUTH scaffold evoluído por FT-COLABORADOR).
 */
@Getter
@Setter
@Entity
@Table(name = "COLABORADOR", schema = "UNMPORTCOM")
public class ColaboradorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_colaborador")
    @SequenceGenerator(name = "sq_colaborador", sequenceName = "SQ_COLABORADOR", allocationSize = 1)
    @Column(name = "COD_COLABORADOR", nullable = false)
    private Long id;

    @Column(name = "COD_FEDERACAO", nullable = false)
    private Long federacaoId;

    @Column(name = "COD_SINGULAR")
    private Long singularId;

    @Column(name = "COD_AREA")
    private Long areaId;

    @Column(name = "COD_EQUIPE")
    private Long equipeId;

    @Column(name = "COD_GESTOR")
    private Long gestorId;

    @Column(name = "NOM_COLABORADOR", nullable = false, length = 255)
    private String nome;

    @Column(name = "DES_EMAIL", nullable = false, length = 255)
    private String email;

    @Column(name = "ID_ZIMBRA", nullable = false, length = 255)
    private String zimbraId;

    @Column(name = "DES_BIOGRAFIA", length = 4000)
    private String biografia;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "FLG_ATIVO", nullable = false, length = 1)
    private String ativo;

    @Column(name = "DAT_NASCIMENTO")
    private Instant dataNascimento;

    @Column(name = "DAT_CONTRATACAO")
    private Instant dataContratacao;

    @Column(name = "DAT_ULTIMO_ACESSO")
    private Instant dataUltimoAcesso;

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
        ColaboradorEntity that = (ColaboradorEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
