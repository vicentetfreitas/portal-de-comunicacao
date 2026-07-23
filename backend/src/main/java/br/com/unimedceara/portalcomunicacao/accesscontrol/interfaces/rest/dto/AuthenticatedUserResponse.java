package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import java.util.List;

/**
 * Resposta da identidade do colaborador autenticado (AUTH-API-003).
 */
public record AuthenticatedUserResponse(
        long id,
        String email,
        String name,
        List<String> permissions,
        String sessionId,
        ColaboradorOrganizationalLinksResponse organizationalLinks) {
}
