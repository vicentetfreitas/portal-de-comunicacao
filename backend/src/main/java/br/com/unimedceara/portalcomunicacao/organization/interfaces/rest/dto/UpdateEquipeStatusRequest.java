package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de alteração de status da equipe.
 */
public record UpdateEquipeStatusRequest(@NotNull EquipeStatus status) {
}
