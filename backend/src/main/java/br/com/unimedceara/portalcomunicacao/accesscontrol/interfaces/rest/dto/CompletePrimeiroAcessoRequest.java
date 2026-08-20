package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Payload de conclusão do Primeiro Acesso (PA-API-007).
 * Identidade e Singular vêm do JWT autenticado — não do cliente.
 */
public record CompletePrimeiroAcessoRequest(
        @NotNull Long areaId,
        Long teamId) {
}
