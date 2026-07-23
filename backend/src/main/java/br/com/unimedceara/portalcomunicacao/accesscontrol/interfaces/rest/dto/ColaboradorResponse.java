package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;

import java.time.Instant;

/**
 * Representação de colaborador na API.
 */
public record ColaboradorResponse(
        Long id,
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId,
        Long managerId,
        String name,
        String email,
        String zimbraId,
        String biography,
        ColaboradorStatus status,
        Instant birthDate,
        Instant hireDate,
        Instant lastAccessAt,
        Instant createdAt,
        Instant updatedAt) {
}
