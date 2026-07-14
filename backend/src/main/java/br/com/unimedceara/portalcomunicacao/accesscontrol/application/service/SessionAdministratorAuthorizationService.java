package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Valida autorização para operações administrativas de sessão (RN-AUTH-013).
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SessionAdministratorAuthorizationService {

    private static final String FORBIDDEN_MESSAGE =
            "Colaborador sem autorização para revogar sessões administrativamente";

    private final ColaboradorService colaboradorService;
    private final AuthProperties authProperties;

    public SessionAdministratorAuthorizationService(
            ColaboradorService colaboradorService,
            AuthProperties authProperties) {
        this.colaboradorService = colaboradorService;
        this.authProperties = authProperties;
    }

    /**
     * Garante que o colaborador possui perfil de administrador de sessão.
     *
     * @param colaboradorId identificador do solicitante
     * @throws ForbiddenException quando o colaborador não é administrador autorizado
     */
    public void ensureSessionAdministrator(long colaboradorId) {
        if (!isSessionAdministrator(colaboradorId)) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }
    }

    private boolean isSessionAdministrator(long colaboradorId) {
        ColaboradorEntity colaborador = colaboradorService.findById(colaboradorId);
        String email = colaborador.getEmail().toLowerCase();
        return authProperties.sessionAdministratorEmails().stream()
                .map(String::toLowerCase)
                .anyMatch(email::equals);
    }
}
