package br.com.unimedceara.portalcomunicacao.shared.validation.annotation;

import br.com.unimedceara.portalcomunicacao.shared.validation.validator.UuidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valida que um valor {@link String} seja {@code null} ou um UUID válido.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface Uuid {

    /**
     * Mensagem exibida quando a validação falha.
     *
     * @return mensagem de erro
     */
    String message() default "must be a valid UUID";

    /**
     * Grupos de validação aos quais esta constraint pertence.
     *
     * @return grupos de validação
     */
    Class<?>[] groups() default {};

    /**
     * Payloads associados a esta constraint.
     *
     * @return payloads de validação
     */
    Class<? extends Payload>[] payload() default {};
}
