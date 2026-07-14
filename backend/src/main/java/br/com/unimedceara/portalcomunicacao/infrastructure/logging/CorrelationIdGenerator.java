package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import java.util.UUID;

/**
 * Gera identificadores de correlação para rastreamento de requisições.
 */
public final class CorrelationIdGenerator {

    private CorrelationIdGenerator() {
    }

    /**
     * Gera um novo Correlation ID no formato UUID padrão.
     *
     * @return Correlation ID gerado
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
