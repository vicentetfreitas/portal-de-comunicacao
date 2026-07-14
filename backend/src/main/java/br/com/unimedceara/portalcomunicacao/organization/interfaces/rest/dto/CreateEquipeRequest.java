package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload de criação de equipe.
 */
public record CreateEquipeRequest(
        @NotNull Long areaId,
        @NotBlank @Size(max = 200) String name,
        String description,
        Long leaderId) {
}
