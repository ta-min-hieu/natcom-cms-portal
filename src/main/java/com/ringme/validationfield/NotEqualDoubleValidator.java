package com.ringme.validationfield;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NotEqualDoubleValidator implements ConstraintValidator<NotEqualDouble, Double> {

    private double forbiddenValue;
    private boolean required;

    @Override
    public void initialize(NotEqualDouble constraintAnnotation) {
        forbiddenValue = constraintAnnotation.value();
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Double rootValue, ConstraintValidatorContext context) {
        if (rootValue == null) {
            return !required;
        }
        return !Objects.equals(rootValue, forbiddenValue);
    }
}
