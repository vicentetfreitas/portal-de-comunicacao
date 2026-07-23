package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;

import java.time.Instant;

/**
 * Representação de singular na API.
 */
public record SingularResponse(
        Long id,
        Long federationId,
        String name,
        String acronym,
        Integer unimedCode,
        String registroAns,
        SingularStatus status,
        Instant createdAt,
        Instant updatedAt) {
}
