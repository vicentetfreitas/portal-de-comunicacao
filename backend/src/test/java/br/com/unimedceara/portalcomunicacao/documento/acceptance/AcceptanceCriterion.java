package br.com.unimedceara.portalcomunicacao.documento.acceptance;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca testes automatizados com o identificador do critério de aceite FT-DOCUMENTO.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag("acceptance")
public @interface AcceptanceCriterion {

    String value();

    TestType type() default TestType.INTEGRATION;

    enum TestType {
        UNIT,
        INTEGRATION,
        API
    }
}
