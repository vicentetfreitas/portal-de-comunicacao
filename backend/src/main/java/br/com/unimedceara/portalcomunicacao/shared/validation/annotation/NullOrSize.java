package br.com.unimedceara.portalcomunicacao.shared.validation.annotation;

import br.com.unimedceara.portalcomunicacao.shared.validation.validator.NullOrSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valida que um campo {@link String} seja {@code null} ou possua tamanho entre {@code min} e {@code max}.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrSizeValidator.class)
public @interface NullOrSize {

    /**
     * Mensagem exibida quando a validação falha.
     *
     * @return mensagem de erro
     */
    String message() default "size must be between {min} and {max}";

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

    /**
     * Tamanho mínimo permitido quando o valor não é {@code null}.
     *
     * @return tamanho mínimo
     */
    int min() default 0;

    /**
     * Tamanho máximo permitido quando o valor não é {@code null}.
     *
     * @return tamanho máximo
     */
    int max();
}
