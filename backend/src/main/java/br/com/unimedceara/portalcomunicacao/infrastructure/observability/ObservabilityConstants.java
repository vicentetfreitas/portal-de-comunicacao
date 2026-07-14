package br.com.unimedceara.portalcomunicacao.infrastructure.observability;

/**
 * Convenção de naming para métricas Micrometer (CD-S1A-005 — opção A).
 */
public final class ObservabilityConstants {

    /**
     * Prefixo padrão das métricas da plataforma.
     */
    public static final String METRIC_PREFIX = "portal";

    /**
     * Contador total de requisições HTTP.
     */
    public static final String HTTP_REQUESTS_TOTAL = "portal.http.requests.total";

    /**
     * Timer de duração das requisições HTTP.
     */
    public static final String HTTP_REQUESTS_DURATION = "portal.http.requests.duration";

    private ObservabilityConstants() {
    }
}
