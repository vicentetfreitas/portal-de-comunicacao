package br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model;

/**
 * Claims validados do Access Token JWT.
 */
public record JwtClaims(
        long colaboradorId,
        String sessionId,
        String email,
        String name,
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId) {
}
