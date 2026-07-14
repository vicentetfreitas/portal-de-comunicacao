package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de alteração de status da singular.
 */
public record UpdateSingularStatusRequest(@NotNull SingularStatus status) {
}
