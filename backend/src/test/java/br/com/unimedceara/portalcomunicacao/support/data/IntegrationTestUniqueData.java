package br.com.unimedceara.portalcomunicacao.support.data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Valores únicos para testes de integração em Oracle compartilhado (sem rollback automático).
 * <p>
 * Evita violações de UK em {@code SIG_SINGULAR}, {@code COD_UNIMED}, e-mail, etc.
 */
public final class IntegrationTestUniqueData {

    private static final AtomicInteger SEQUENCE =
            new AtomicInteger((int) (System.nanoTime() % 800_000) + 100_000);

    private IntegrationTestUniqueData() {}

    /**
     * Sigla única para {@code SINGULAR.SIG_SINGULAR} (máx. 30 caracteres).
     */
    public static String singularSigla(String prefix) {
        String p = prefix == null || prefix.isBlank() ? "T" : prefix;
        String sigla = p + SEQUENCE.incrementAndGet();
        return sigla.length() <= 30 ? sigla : sigla.substring(0, 30);
    }

    /**
     * Código Unimed único (modelo físico: NUMBER(3) — faixa 100–999).
     */
    public static int singularUnimedCode() {
        return 100 + (SEQUENCE.incrementAndGet() % 900);
    }

    public static String registroAnsForUnimedCode(int unimedCode) {
        return "ANS" + String.format("%04d", unimedCode % 10_000);
    }

    /**
     * E-mail único para colaboradores de teste.
     */
    public static String colaboradorEmail(String localPartPrefix) {
        String prefix = localPartPrefix == null || localPartPrefix.isBlank() ? "user" : localPartPrefix;
        return prefix + "-" + SEQUENCE.incrementAndGet() + "@unimedceara.com.br";
    }

    /**
     * Identificador numérico único para entidades sem sequence Oracle homologada
     * (ex.: {@code PAPEL}, {@code PAPEL_ATRIBUICAO} — ver {@code database/model/03-physical-model.md}).
     * Válido apenas dentro do escopo de um teste com rollback ({@code @IntegrationTest}).
     */
    public static long uniqueId() {
        return SEQUENCE.incrementAndGet();
    }
}
