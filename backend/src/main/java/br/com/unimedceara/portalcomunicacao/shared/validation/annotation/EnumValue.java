package br.com.unimedceara.portalcomunicacao.shared.validation.annotation;

import br.com.unimedceara.portalcomunicacao.shared.validation.validator.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valida que um valor {@link String} corresponda a um dos nomes das constantes do {@link Enum} informado.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {

    /**
     * Mensagem exibida quando a validação falha.
     *
     * @return mensagem de erro
     */
    String message() default "must be a valid enum value";

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
     * Classe do {@link Enum} cujos nomes de constantes são aceitos.
     *
     * @return classe do enum de referência
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * Indica se a comparação deve ignorar diferenças de maiúsculas e minúsculas.
     *
     * @return {@code true} para comparação case-insensitive
     */
    boolean ignoreCase() default false;
}
