package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.SessionAdministrationService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints administrativos de gerenciamento de sessão (RF-AUTH-010).
 */
@RestController
@RequestMapping("/api/v1/admin/sessions")
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@Tag(name = "Session Administration", description = "Revogação administrativa de sessões")
public class AdminSessionController {

    private final SessionAdministrationService sessionAdministrationService;

    public AdminSessionController(SessionAdministrationService sessionAdministrationService) {
        this.sessionAdministrationService = sessionAdministrationService;
    }

    /**
     * Revoga sessão existente por {@code session_id} sem remover cookies do colaborador.
     */
    @DeleteMapping("/{sessionId}")
    @Operation(
            summary = "Revogar sessão administrativamente",
            description = "Invalida Refresh Token da sessão alvo; renovações futuras retornam HTTP 401")
    public ResponseEntity<Void> revokeSession(
            @PathVariable String sessionId,
            @AuthenticationPrincipal JwtAuthenticatedPrincipal principal) {
        sessionAdministrationService.revokeSessionAdministratively(sessionId, principal.colaboradorId());
        return ResponseEntity.noContent().build();
    }
}
