package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * Payload de atualização de colaborador.
 */
public record UpdateColaboradorRequest(
        Long singularId,
        Long areaId,
        Long teamId,
        Long managerId,
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String zimbraId,
        @Size(max = 4000) String biography,
        Instant birthDate,
        Instant hireDate) {
}
