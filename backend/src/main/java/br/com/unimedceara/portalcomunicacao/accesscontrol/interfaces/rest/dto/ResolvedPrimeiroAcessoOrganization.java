package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

/**
 * Singular e Federação resolvidas no Primeiro Acesso (DEC-ORG-003). Sem Área/Equipe.
 */
public record ResolvedPrimeiroAcessoOrganization(
        Long singularId,
        Long federationId) {
}
