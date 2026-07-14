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

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA do colaborador (mínimo FT-AUTH).
 */
@Getter
@Setter
@Entity
@Table(name = "COLABORADOR", schema = "UNMPORTCOM")
public class ColaboradorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_colaborador")
    @SequenceGenerator(name = "sq_colaborador", sequenceName = "SQ_COLABORADOR_COD_COLABORADOR", allocationSize = 1)
    @Column(name = "COD_COLABORADOR", nullable = false)
    private Long id;

    @Column(name = "DES_EMAIL", nullable = false, length = 255)
    private String email;

    @Column(name = "NOM_COLABORADOR", nullable = false, length = 200)
    private String nome;

    @Column(name = "ID_ZIMBRA", length = 255)
    private String zimbraId;

    @Column(name = "FLG_ATIVO", nullable = false, length = 1)
    private String ativo;

    @Column(name = "COD_FEDERACAO", nullable = false)
    private Long federacaoId;

    @Column(name = "COD_EQUIPE")
    private Long equipeId;

    @Column(name = "DAT_CADASTRO", nullable = false)
    private Instant dataCadastro;

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
