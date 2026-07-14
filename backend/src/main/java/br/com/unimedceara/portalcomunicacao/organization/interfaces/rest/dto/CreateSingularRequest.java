package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criação de singular.
 */
public record CreateSingularRequest(
        @NotNull Long federationId,
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 30) String acronym,
        @NotBlank @Size(max = 20) String unimedCode) {
}
