package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;

import java.time.Instant;

/**
 * Representação de singular na API.
 */
public record SingularResponse(
        Long id,
        Long federacaoId,
        String name,
        String acronym,
        String codigoUnimed,
        SingularStatus status,
        Instant createdAt,
        Instant updatedAt) {
}
