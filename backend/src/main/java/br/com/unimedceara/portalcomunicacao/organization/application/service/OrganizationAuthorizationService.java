package br.com.unimedceara.portalcomunicacao.organization.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.SessionAdministratorAuthorizationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Autorização administrativa para operações de organização (RNF-AREA-002 incremental).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OrganizationAuthorizationService {

    private final SessionAdministratorAuthorizationService sessionAdministratorAuthorizationService;

    public OrganizationAuthorizationService(
            SessionAdministratorAuthorizationService sessionAdministratorAuthorizationService) {
        this.sessionAdministratorAuthorizationService = sessionAdministratorAuthorizationService;
    }

    /**
     * Garante que o colaborador possui perfil administrativo para operações de escrita.
     */
    public void ensureOrganizationAdministrator(long colaboradorId) {
        sessionAdministratorAuthorizationService.ensureSessionAdministrator(colaboradorId);
    }
}
