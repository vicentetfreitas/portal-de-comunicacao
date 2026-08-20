package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

/**
 * Área disponível para seleção no Primeiro Acesso (PA-API-006).
 */
public record PrimeiroAcessoAreaResponse(
        Long id,
        String name,
        String acronym) {
}
