package br.com.unimedceara.portalcomunicacao.organization.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de atualização de equipe.
 */
public record UpdateEquipeRequest(
        @NotBlank @Size(max = 200) String name,
        String description,
        Long leaderId) {
}
