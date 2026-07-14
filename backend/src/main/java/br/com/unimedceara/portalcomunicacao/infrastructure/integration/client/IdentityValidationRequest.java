package br.com.unimedceara.portalcomunicacao.infrastructure.integration.client;

import jakarta.validation.constraints.NotBlank;

/**
 * Requisição abstrata para validação de identidade no provedor externo (Zimbra).
 *
 * @param validationToken token ou código retornado pelo callback de autenticação
 */
public record IdentityValidationRequest(
        @NotBlank String validationToken) {
}
