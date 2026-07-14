package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de alteração de status da área.
 */
public record UpdateAreaStatusRequest(@NotNull AreaStatus status) {
}
