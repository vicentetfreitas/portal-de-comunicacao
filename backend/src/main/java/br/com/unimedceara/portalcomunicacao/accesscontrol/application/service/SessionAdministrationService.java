package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.shared.exception.ResourceNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Operações administrativas sobre sessões de autenticação (RF-AUTH-010).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SessionAdministrationService {

    private static final String SESSION_NOT_FOUND_MESSAGE = "Sessão não encontrada";

    private final AuthSessaoRepository authSessaoRepository;
    private final SessionService sessionService;
    private final AuthAuditService authAuditService;
    private final SessionAdministratorAuthorizationService sessionAdministratorAuthorizationService;

    public SessionAdministrationService(
            AuthSessaoRepository authSessaoRepository,
            SessionService sessionService,
            AuthAuditService authAuditService,
            SessionAdministratorAuthorizationService sessionAdministratorAuthorizationService) {
        this.authSessaoRepository = authSessaoRepository;
        this.sessionService = sessionService;
        this.authAuditService = authAuditService;
        this.sessionAdministratorAuthorizationService = sessionAdministratorAuthorizationService;
    }

    /**
     * Revoga administrativamente uma sessão pelo {@code session_id} (RN-AUTH-011, RN-AUTH-013).
     *
     * @param sessionId identificador público da sessão
     * @param administratorColaboradorId colaborador que executa a revogação
     */
    @Transactional
    public void revokeSessionAdministratively(String sessionId, long administratorColaboradorId) {
        sessionAdministratorAuthorizationService.ensureSessionAdministrator(administratorColaboradorId);

        AuthSessaoEntity sessao = authSessaoRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(SESSION_NOT_FOUND_MESSAGE));

        long targetColaboradorId = sessao.getColaborador().getId();

        if (!sessao.isRevogada()) {
            sessionService.revokeBySessionId(sessionId);
            authAuditService.logAdministrativeRevocation(
                    administratorColaboradorId,
                    sessionId,
                    targetColaboradorId);
        }
    }
}
