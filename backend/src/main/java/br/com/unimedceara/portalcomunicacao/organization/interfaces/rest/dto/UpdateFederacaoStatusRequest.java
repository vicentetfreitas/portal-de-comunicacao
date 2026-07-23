package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de alteração de status da federação.
 */
public record UpdateFederacaoStatusRequest(@NotNull FederacaoStatus status) {
}
