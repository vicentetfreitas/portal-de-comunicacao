package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.NullOrSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para {@link NullOrSize}.
 * Aceita {@code null} ou strings com comprimento entre os limites configurados.
 */
public class NullOrSizeValidator implements ConstraintValidator<NullOrSize, String> {

    private int min;
    private int max;

    @Override
    public void initialize(NullOrSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int length = value.length();
        return length >= min && length <= max;
    }
}
