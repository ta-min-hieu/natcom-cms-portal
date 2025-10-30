package com.ringme.validationfield;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean check = false;
        if (s.matches("^(670)+\\d{8}$") || s.matches("^(670)+\\d{9}$")) {
            check = true;
        }
        return check;
    }
}
