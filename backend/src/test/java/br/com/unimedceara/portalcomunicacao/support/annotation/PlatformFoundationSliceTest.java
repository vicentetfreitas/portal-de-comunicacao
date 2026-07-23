package br.com.unimedceara.portalcomunicacao.support.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Testes de fatia (Platform Foundation / wiring) sem Oracle nem JPA.
 * <p>
 * Perfil {@code test-slice}: ver {@code application-test-slice.yaml}. Não utilizar para Features com persistência
 * (DEC-DB-023) — usar {@link IntegrationTest}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
@ActiveProfiles("test-slice")
public @interface PlatformFoundationSliceTest {

    @AliasFor(annotation = SpringBootTest.class, attribute = "webEnvironment")
    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.MOCK;
}
