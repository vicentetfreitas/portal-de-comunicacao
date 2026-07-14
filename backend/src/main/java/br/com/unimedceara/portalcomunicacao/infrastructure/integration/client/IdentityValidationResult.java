package br.com.unimedceara.portalcomunicacao.infrastructure.integration.client;

import jakarta.validation.constraints.NotBlank;

/**
 * Resultado mínimo de identidade autenticada retornado pelo provedor externo.
 *
 * @param email       e-mail corporativo do colaborador
 * @param displayName nome de exibição
 * @param zimbraId    identificador único no Zimbra
 */
public record IdentityValidationResult(
        @NotBlank String email,
        @NotBlank String displayName,
        @NotBlank String zimbraId) {
}
