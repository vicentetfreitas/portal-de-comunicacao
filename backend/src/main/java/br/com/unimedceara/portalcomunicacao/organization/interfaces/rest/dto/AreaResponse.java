package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;

import java.time.Instant;

/**
 * Representação de área na API.
 */
public record AreaResponse(
        Long id,
        Long singularId,
        Long parentAreaId,
        String name,
        String acronym,
        String description,
        Long managerId,
        AreaStatus status,
        Instant createdAt,
        Instant updatedAt) {
}
