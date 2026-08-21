package br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model;

/**
 * Claims validados do Access Token JWT.
 * <p>
 * {@code federationId}/{@code singularId}/{@code areaId}/{@code teamId} representam o
 * vínculo cadastral do colaborador (DH-02). {@code papelAtribuicaoId} representa a
 * atribuição de papel (PAPEL_ATRIBUICAO) ativa como contexto operacional — ortogonal
 * ao vínculo (DEC-DB-020); nunca reutiliza os claims de vínculo.
 */
public record JwtClaims(
        Long colaboradorId,
        String sessionId,
        String email,
        String name,
        String zimbraId,
        boolean primeiroAcesso,
        Long federationId,
        Long singularId,
        Long areaId,
        Long teamId,
        Long papelAtribuicaoId) {
}
