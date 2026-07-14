package br.com.unimedceara.portalcomunicacao.shared.validation.validator;

import br.com.unimedceara.portalcomunicacao.shared.validation.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validador para {@link EnumValue}.
 * Aceita {@code null} e strings que correspondam aos nomes das constantes do enum configurado.
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    private Set<String> acceptedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.ignoreCase = constraintAnnotation.ignoreCase();
        this.acceptedValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (ignoreCase) {
            return acceptedValues.stream().anyMatch(accepted -> accepted.equalsIgnoreCase(value));
        }

        return acceptedValues.contains(value);
    }
}
