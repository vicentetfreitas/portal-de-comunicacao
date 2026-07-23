package br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model;

import java.security.Principal;

/**
 * Principal autenticado extraído do JWT validado criptograficamente.
 */
public record JwtAuthenticatedPrincipal(
        long colaboradorId,
        String sessionId,
        String email,
        String name,
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId) implements Principal {

    @Override
    public String getName() {
        return String.valueOf(colaboradorId);
    }
}
