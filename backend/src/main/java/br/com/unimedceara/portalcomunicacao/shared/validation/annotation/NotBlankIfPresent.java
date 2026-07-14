package br.com.unimedceara.portalcomunicacao.shared.validation.annotation;

import br.com.unimedceara.portalcomunicacao.shared.validation.validator.NotBlankIfPresentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valida que um campo {@link String} opcional, quando informado, não seja vazio nem composto
 * apenas por espaços em branco.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
public @interface NotBlankIfPresent {

    /**
     * Mensagem exibida quando a validação falha.
     *
     * @return mensagem de erro
     */
    String message() default "must not be blank when present";

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
