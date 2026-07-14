package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.Uuid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

/**
 * Validador para {@link Uuid}.
 * Aceita {@code null} e strings que representem um UUID válido.
 */
public class UuidValidator implements ConstraintValidator<Uuid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
