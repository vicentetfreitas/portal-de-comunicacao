package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload de atualização de colaborador.
 */
public record UpdateColaboradorRequest(
        Long singularId,
        Long areaId,
        Long teamId,
        Long managerId,
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Email @Size(max = 255) String email,
        @Size(max = 100) String jobTitle,
        @Size(min = 11, max = 11) String cpf,
        String biography) {
}
