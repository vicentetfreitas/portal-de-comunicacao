package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Resposta da identidade autenticada (AUTH-API-003 / PA-API-005).
 * <p>
 * {@code eligibleAssignments} lista as atribuições de papel (PAPEL_ATRIBUICAO) elegíveis
 * do colaborador; {@code activeAssignment} identifica qual delas é o contexto operacional
 * ativo (nulo quando nenhuma foi selecionada — 0 elegíveis, ou mais de 1 sem seleção
 * explícita ainda feita pelo colaborador).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthenticatedUserResponse(
        Long id,
        String email,
        String name,
        List<String> permissions,
        String sessionId,
        ColaboradorOrganizationalLinksResponse organizationalLinks,
        boolean primeiroAcesso,
        ResolvedPrimeiroAcessoOrganization resolvedOrganization,
        String primeiroAcessoBlockCode,
        List<PapelAtribuicaoResponse> eligibleAssignments,
        PapelAtribuicaoResponse activeAssignment) {
}
