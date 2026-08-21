package br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;

/**
 * Entidade JPA de atribuição de papel a colaborador ({@code PAPEL_ATRIBUICAO}).
 * <p>
 * Representa o contexto operacional (autorização) do colaborador — ortogonal
 * ao vínculo cadastral único de {@link ColaboradorEntity} (DEC-DB-020, DH-02).
 * Um colaborador pode possuir N atribuições; a atribuição selecionada como
 * ativa é exposta via claim no Access Token (não persistida em
 * {@code AUTH_SESSAO} — REF-DB-CTX-01 aplica-se ao vínculo organizacional,
 * não a esta entidade).
 * <p>
 * Somente leitura nesta evolução de FT-SESSION: a criação/gestão de
 * atribuições pertence a uma Feature futura de administração de papéis.
 * Sem {@code @GeneratedValue}: a tabela não possui sequence homologada no
 * baseline 2026-07-22 ({@code database/model/03-physical-model.md}) e este
 * escopo não cria registros de {@code PAPEL_ATRIBUICAO}.
 */
@Getter
@Setter
@Entity
@Table(name = "PAPEL_ATRIBUICAO", schema = "UNMPORTCOM")
public class PapelAtribuicaoEntity {

    @Id
    @Column(name = "COD_PAPEL_ATRIBUICAO", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_COLABORADOR", nullable = false)
    private ColaboradorEntity colaborador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_PAPEL", nullable = false)
    private PapelEntity papel;

    @Column(name = "COD_FEDERACAO")
    private Long federacaoId;

    @Column(name = "COD_SINGULAR")
    private Long singularId;

    @Column(name = "COD_AREA")
    private Long areaId;

    @Column(name = "COD_EQUIPE")
    private Long equipeId;

    @Column(name = "DAT_INICIO_VIGENCIA", nullable = false)
    private Instant dataInicioVigencia;

    @Column(name = "DAT_FIM_VIGENCIA")
    private Instant dataFimVigencia;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "FLG_ATIVO", nullable = false, length = 1)
    private String ativo;

    public boolean isAtivo() {
        return "S".equalsIgnoreCase(ativo);
    }

    /**
     * Elegibilidade para seleção como contexto operacional: ativa e dentro da vigência.
     * Não valida pertencimento ao colaborador — responsabilidade do chamador (query/serviço).
     */
    public boolean isElegivel(Instant reference) {
        if (!isAtivo()) {
            return false;
        }
        if (dataInicioVigencia != null && dataInicioVigencia.isAfter(reference)) {
            return false;
        }
        return dataFimVigencia == null || dataFimVigencia.isAfter(reference);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        PapelAtribuicaoEntity that = (PapelAtribuicaoEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
