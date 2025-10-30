package com.ringme.validationfield;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullConstraintValidator implements ConstraintValidator<NotNullCus, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            if (!s.trim().equals("")) {
                return true;
            }
        }
        return false;
    }
}
