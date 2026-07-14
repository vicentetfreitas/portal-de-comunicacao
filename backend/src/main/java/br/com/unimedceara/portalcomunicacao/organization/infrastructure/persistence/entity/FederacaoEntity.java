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

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA mínima de federação para validações referenciais (FT-SINGULAR).
 */
@Getter
@Setter
@Entity
@Table(name = "FEDERACAO", schema = "UNMPORTCOM")
public class FederacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_federacao")
    @SequenceGenerator(name = "sq_federacao", sequenceName = "SQ_FEDERACAO_COD_FEDERACAO", allocationSize = 1)
    @Column(name = "COD_FEDERACAO", nullable = false)
    private Long id;

    @Column(name = "NOM_FEDERACAO", nullable = false, length = 200)
    private String nome;

    @Column(name = "SIG_FEDERACAO", nullable = false, length = 30)
    private String sigla;

    @Column(name = "COD_UNIMED", nullable = false, length = 20)
    private String codigoUnimed;

    @Column(name = "NUM_REGISTRO_ANS", nullable = false, length = 20)
    private String numeroRegistroAns;

    @Column(name = "URL_SITE", length = 300)
    private String urlSite;

    @Lob
    @Column(name = "DSC_FEDERACAO")
    private String descricao;

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
        FederacaoEntity that = (FederacaoEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
