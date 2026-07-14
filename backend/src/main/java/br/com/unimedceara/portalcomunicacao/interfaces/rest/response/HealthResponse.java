package br.com.unimedceara.portalcomunicacao.interfaces.rest.response;

/**
 * Dados de saúde da aplicação retornados pelo endpoint de health.
 *
 * @param status      status operacional da aplicação
 * @param application nome da aplicação
 * @param version     versão da aplicação
 */
public record HealthResponse(
        String status,
        String application,
        String version) {
}
