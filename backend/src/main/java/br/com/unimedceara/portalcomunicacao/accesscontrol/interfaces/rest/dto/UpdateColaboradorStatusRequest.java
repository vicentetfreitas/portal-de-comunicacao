package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de alteração de status do colaborador.
 */
public record UpdateColaboradorStatusRequest(@NotNull ColaboradorStatus status) {
}
