package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

/**
 * Vínculos organizacionais do colaborador autenticado (fonte: COLABORADOR).
 */
public record ColaboradorOrganizationalLinksResponse(
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId) {
}
