package br.com.unimedceara.portalcomunicacao.support.fixture;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.AreaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.EquipeTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.SingularTestBuilder;

/**
 * Monta hierarquia organizacional mínima por teste (rollback via {@code @IntegrationTest}).
 */
public final class OrganizationalTestFixtures {

    private OrganizationalTestFixtures() {}

    public record Hierarchy(SingularEntity singular, AreaEntity area, EquipeEntity equipe) {

        public long singularId() {
            return singular.getId();
        }

        public long areaId() {
            return area.getId();
        }

        public long equipeId() {
            return equipe.getId();
        }
    }

    public static Hierarchy persistMinimalHierarchy(
            long federationId,
            SingularRepository singularRepository,
            AreaRepository areaRepository,
            EquipeRepository equipeRepository) {
        SingularEntity singular =
                SingularTestBuilder.forFederation(federationId).persist(singularRepository);
        AreaEntity area = AreaTestBuilder.forSingular(singular.getId()).persist(areaRepository);
        EquipeEntity equipe = EquipeTestBuilder.forArea(area.getId()).persist(equipeRepository);
        return new Hierarchy(singular, area, equipe);
    }
}
