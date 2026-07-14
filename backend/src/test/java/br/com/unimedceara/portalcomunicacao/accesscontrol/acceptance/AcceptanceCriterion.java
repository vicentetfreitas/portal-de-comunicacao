package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca testes automatizados com o identificador do critério de aceite FT-AUTH.
 * Rastreabilidade: {@code specs/features/authentication/acceptance-tests.md}
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag("acceptance")
public @interface AcceptanceCriterion {

    /**
     * Identificador do critério (ex.: {@code AC-AUTH-001}).
     */
    String value();

    /**
     * Tipo do teste para documentação da matriz de rastreabilidade.
     */
    TestType type() default TestType.INTEGRATION;

    enum TestType {
        UNIT,
        INTEGRATION,
        API
    }
}
