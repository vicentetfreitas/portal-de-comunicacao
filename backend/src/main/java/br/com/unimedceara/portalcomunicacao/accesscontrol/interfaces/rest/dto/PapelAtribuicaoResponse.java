package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Representação de uma atribuição de papel (PAPEL_ATRIBUICAO) elegível ou ativa
 * para o colaborador autenticado. Não contém permissões — apenas identificação
 * do papel e escopo organizacional da atribuição.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PapelAtribuicaoResponse(
        Long id,
        String papel,
        Long federacaoId,
        Long singularId,
        Long areaId,
        Long equipeId) {
}
