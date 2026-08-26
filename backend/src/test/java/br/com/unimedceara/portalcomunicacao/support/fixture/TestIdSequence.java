package br.com.unimedceara.portalcomunicacao.support.fixture;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Geração de ids únicos para fixtures de entidades sem sequence Oracle própria
 * (ex.: {@code PASTA}, {@code PERMISSAO_PASTA}, {@code DOCUMENTO_VERSAO}, {@code ARQUIVO_BINARIO}
 * — a app nunca insere essas tabelas em produção, só em teste). Offset alto evita colisão com
 * ids de outras fixtures/dados de teste.
 */
public final class TestIdSequence {

    private static final AtomicLong COUNTER = new AtomicLong(900_000_000L);

    private TestIdSequence() {
    }

    public static long next() {
        return COUNTER.incrementAndGet();
    }
}
