package br.com.unimedceara.portalcomunicacao.support.fixture.builder;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelAtribuicaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.PapelAtribuicaoRepository;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;

import java.time.Instant;

/**
 * Builder de teste para {@link PapelAtribuicaoEntity}. Sem sequence homologada (ver
 * {@link IntegrationTestUniqueData#uniqueId()}) — identificador atribuído explicitamente.
 */
public final class PapelAtribuicaoTestBuilder {

    private final long id = IntegrationTestUniqueData.uniqueId();
    private final ColaboradorEntity colaborador;
    private final PapelEntity papel;
    private Long federacaoId;
    private Long singularId;
    private Long areaId;
    private Long equipeId;
    private Instant dataInicioVigencia = Instant.now().minusSeconds(60);
    private Instant dataFimVigencia;
    private String ativo = "S";

    private PapelAtribuicaoTestBuilder(ColaboradorEntity colaborador, PapelEntity papel) {
        this.colaborador = colaborador;
        this.papel = papel;
    }

    public static PapelAtribuicaoTestBuilder of(ColaboradorEntity colaborador, PapelEntity papel) {
        return new PapelAtribuicaoTestBuilder(colaborador, papel);
    }

    public PapelAtribuicaoTestBuilder scope(Long federacaoId, Long singularId, Long areaId, Long equipeId) {
        this.federacaoId = federacaoId;
        this.singularId = singularId;
        this.areaId = areaId;
        this.equipeId = equipeId;
        return this;
    }

    public PapelAtribuicaoTestBuilder ativo(String ativo) {
        this.ativo = ativo;
        return this;
    }

    public PapelAtribuicaoTestBuilder dataInicioVigencia(Instant dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
        return this;
    }

    public PapelAtribuicaoTestBuilder dataFimVigencia(Instant dataFimVigencia) {
        this.dataFimVigencia = dataFimVigencia;
        return this;
    }

    /** Vigência iniciando no futuro — ainda não elegível. */
    public PapelAtribuicaoTestBuilder futura() {
        this.dataInicioVigencia = Instant.now().plusSeconds(3600);
        return this;
    }

    /** Vigência encerrada no passado — não mais elegível. */
    public PapelAtribuicaoTestBuilder expirada() {
        this.dataInicioVigencia = Instant.now().minusSeconds(7200);
        this.dataFimVigencia = Instant.now().minusSeconds(60);
        return this;
    }

    public PapelAtribuicaoEntity build() {
        PapelAtribuicaoEntity atribuicao = new PapelAtribuicaoEntity();
        atribuicao.setId(id);
        atribuicao.setColaborador(colaborador);
        atribuicao.setPapel(papel);
        atribuicao.setFederacaoId(federacaoId);
        atribuicao.setSingularId(singularId);
        atribuicao.setAreaId(areaId);
        atribuicao.setEquipeId(equipeId);
        atribuicao.setDataInicioVigencia(dataInicioVigencia);
        atribuicao.setDataFimVigencia(dataFimVigencia);
        atribuicao.setAtivo(ativo);
        return atribuicao;
    }

    public PapelAtribuicaoEntity persist(PapelAtribuicaoRepository repository) {
        return repository.save(build());
    }
}
