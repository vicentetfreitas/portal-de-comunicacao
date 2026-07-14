package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de atualização de colaborador (e-mail imutável — RN-009).
 */
public record UpdateColaboradorRequest(
        @NotBlank @Size(max = 200) String name,
        Long singularId,
        Long areaId,
        Long teamId,
        Long managerId,
        @Size(max = 100) String jobTitle,
        @Size(max = 11) String cpf,
        String biography) {
}
