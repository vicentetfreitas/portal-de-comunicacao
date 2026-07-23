package br.com.unimedceara.portalcomunicacao.support.database;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * Política central de limpeza de <strong>dados</strong> em testes de integração (não altera estrutura).
 * <p>
 * Desabilitada no fluxo Oracle padrão (DEC-DB-023). Preservada para reativação futura via
 * {@link br.com.unimedceara.portalcomunicacao.support.annotation.EnableIntegrationTestDatabaseCleanup}.
 */
public class IntegrationTestDatabaseCleaner {

    private final AuthSessaoRepository authSessaoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final EquipeRepository equipeRepository;
    private final AreaRepository areaRepository;
    private final SingularRepository singularRepository;
    private final FederacaoRepository federacaoRepository;
    private final EntityManager entityManager;

    public IntegrationTestDatabaseCleaner(
            AuthSessaoRepository authSessaoRepository,
            ColaboradorRepository colaboradorRepository,
            EquipeRepository equipeRepository,
            AreaRepository areaRepository,
            SingularRepository singularRepository,
            FederacaoRepository federacaoRepository,
            EntityManager entityManager) {
        this.authSessaoRepository = authSessaoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.equipeRepository = equipeRepository;
        this.areaRepository = areaRepository;
        this.singularRepository = singularRepository;
        this.federacaoRepository = federacaoRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void clean() {
        authSessaoRepository.deleteAllInBatch();

        entityManager.createQuery("""
                update ColaboradorEntity c
                set c.gestorId = null, c.equipeId = null, c.areaId = null, c.singularId = null
                """).executeUpdate();
        entityManager.createQuery("update EquipeEntity e set e.liderId = null").executeUpdate();
        entityManager.createQuery("update AreaEntity a set a.gestorId = null").executeUpdate();
        entityManager.flush();

        colaboradorRepository.deleteAllInBatch();
        equipeRepository.deleteAllInBatch();
        areaRepository.deleteAllInBatch();
        singularRepository.deleteAllInBatch();
        federacaoRepository.deleteAllInBatch();
    }
}
