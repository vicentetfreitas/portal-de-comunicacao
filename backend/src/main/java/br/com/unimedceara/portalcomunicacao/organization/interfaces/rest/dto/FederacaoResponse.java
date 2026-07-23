package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;

import java.time.Instant;

/**
 * Representação de federação na API.
 */
public record FederacaoResponse(
        Long id,
        String name,
        String acronym,
        Integer unimedCode,
        String ansRegistration,
        String websiteUrl,
        String description,
        FederacaoStatus status,
        Instant createdAt,
        Instant updatedAt) {
}
