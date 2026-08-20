package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Resposta da identidade autenticada (AUTH-API-003 / PA-API-005).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthenticatedUserResponse(
        Long id,
        String email,
        String name,
        List<String> permissions,
        String sessionId,
        ColaboradorOrganizationalLinksResponse organizationalLinks,
        boolean primeiroAcesso,
        ResolvedPrimeiroAcessoOrganization resolvedOrganization,
        String primeiroAcessoBlockCode) {
}
