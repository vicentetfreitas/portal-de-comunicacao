package br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model;

/**
 * Claims validados do Access Token JWT.
 */
public record JwtClaims(
        Long colaboradorId,
        String sessionId,
        String email,
        String name,
        String zimbraId,
        boolean primeiroAcesso,
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId) {
}
