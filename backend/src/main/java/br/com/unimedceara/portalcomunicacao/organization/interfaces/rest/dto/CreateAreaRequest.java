package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criação de área.
 */
public record CreateAreaRequest(
        @NotNull Long singularId,
        Long parentAreaId,
        @NotBlank @Size(max = 200) String name,
        @Size(max = 30) String acronym,
        String description,
        Long managerId) {
}
