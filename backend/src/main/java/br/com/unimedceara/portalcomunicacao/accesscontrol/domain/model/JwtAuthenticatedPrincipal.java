package br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model;

import java.security.Principal;

/**
 * Principal autenticado extraído do JWT validado criptograficamente.
 */
public record JwtAuthenticatedPrincipal(
        Long colaboradorId,
        String sessionId,
        String email,
        String name,
        String zimbraId,
        boolean primeiroAcesso,
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId,
        Long papelAtribuicaoId) implements Principal {

    @Override
    public String getName() {
        if (colaboradorId != null) {
            return String.valueOf(colaboradorId);
        }
        return email;
    }
}
