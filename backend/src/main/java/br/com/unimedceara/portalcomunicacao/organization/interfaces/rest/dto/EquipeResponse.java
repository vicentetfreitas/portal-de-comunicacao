package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;

import java.time.Instant;

/**
 * Representação de equipe na API.
 */
public record EquipeResponse(
        Long id,
        Long areaId,
        String name,
        String description,
        Long leaderId,
        EquipeStatus status,
        Instant createdAt,
        Instant updatedAt) {
}
