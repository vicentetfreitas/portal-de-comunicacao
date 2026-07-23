package br.com.unimedceara.portalcomunicacao.support.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-anotação para testes de integração com contexto Spring completo e perfil {@code test}.
 * <p>
 * Banco: Oracle provisionado pelo DBA ({@code SPRING_DATASOURCE_*}, usuário {@code UNMPORTCOM_APP},
 * DEC-DB-024). Configuração única: {@code application-test.yaml} + {@code ALTER SESSION SET CURRENT_SCHEMA}.
 * <p>
 * <strong>Isolamento de dados:</strong> {@link Transactional} + {@link Rollback} revertem inserts/updates/deletes
 * ao fim de cada método de teste (incluindo dados criados via MockMvc). Sequences Oracle continuam avançando —
 * testes não devem depender de valores de ID/sequence.
 * <p>
 * Limpeza batch ({@code integration-test-cleanup}) permanece desabilitada (DEC-DB-023); não é necessária com rollback.
 * <p>
 * Testes de fatia sem Oracle: {@link PlatformFoundationSliceTest}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Rollback
public @interface IntegrationTest {

    /**
     * Alias para {@link SpringBootTest#webEnvironment()}.
     *
     * @return ambiente web do teste
     */
    @AliasFor(annotation = SpringBootTest.class, attribute = "webEnvironment")
    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.RANDOM_PORT;
}
