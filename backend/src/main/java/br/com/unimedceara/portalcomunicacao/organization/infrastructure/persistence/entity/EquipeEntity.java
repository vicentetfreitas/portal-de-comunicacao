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

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA mínima de equipe para validação de inativação de área (FT-AREA).
 */
@Getter
@Setter
@Entity
@Table(name = "EQUIPE", schema = "UNMPORTCOM")
public class EquipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_equipe")
    @SequenceGenerator(name = "sq_equipe", sequenceName = "SQ_EQUIPE_COD_EQUIPE", allocationSize = 1)
    @Column(name = "COD_EQUIPE", nullable = false)
    private Long id;

    @Column(name = "COD_AREA", nullable = false)
    private Long areaId;

    @Column(name = "NOM_EQUIPE", nullable = false, length = 200)
    private String nome;

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
        EquipeEntity that = (EquipeEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
